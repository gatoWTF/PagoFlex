package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.AnularRequest;
import com.pagoflex.compromisos.dto.CambiarEstadoRequest;
import com.pagoflex.compromisos.dto.ComprobanteResponse;
import com.pagoflex.compromisos.dto.CompromisoResponse;
import com.pagoflex.compromisos.dto.NuevoCompromisoRequest;
import com.pagoflex.compromisos.dto.PagoRequest;
import com.pagoflex.compromisos.dto.PagoResponse;
import com.pagoflex.compromisos.model.CanalPago;
import com.pagoflex.compromisos.model.Comprobante;
import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.model.MotivoAnulacion;
import com.pagoflex.compromisos.model.Rut;
import com.pagoflex.compromisos.repository.ComprobanteRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompromisoService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final int MAXIMO_CUOTAS = 120;

    private final CompromisoRepository compromisoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final VencimientoService vencimientoService;

    public List<CompromisoResponse> listarPorUsuario(String rut) {
        vencimientoService.actualizarVencidos();
        return ordenar(compromisoRepository.findByRutClave(Rut.clave(rut)));
    }

    public List<CompromisoResponse> listarPorEmpresa(String empresaCodigo) {
        vencimientoService.actualizarVencidos();
        return ordenar(compromisoRepository.findByEmpresaCodigo(empresaCodigo));
    }

    public CompromisoResponse obtener(String folio) {
        vencimientoService.actualizarVencidos();
        return CompromisoResponse.desde(buscar(folio));
    }

    public CompromisoResponse registrar(NuevoCompromisoRequest datos) {
        if (vacio(datos.rutUsuario()) || vacio(datos.nombreUsuario()) || vacio(datos.empresaCodigo())
                || vacio(datos.concepto()) || vacio(datos.registradoPor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUT, nombre, empresa, concepto y ejecutivo son obligatorios");
        }
        Integer total = datos.totalCuotas();
        if (total == null || total < 1 || total > MAXIMO_CUOTAS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad de cuotas debe estar entre 1 y " + MAXIMO_CUOTAS);
        }
        Integer inicial = datos.cuotaInicial();
        if (inicial == null || inicial < 1 || inicial > total) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuota inicial debe estar entre 1 y " + total);
        }
        if (datos.monto() == null || datos.monto() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto de la cuota debe ser mayor a cero");
        }
        validarFecha(datos.fechaVencimiento());

        Compromiso nuevo = Compromiso.builder()
                .folio(siguienteFolio())
                .codigoCredito(siguienteCodigoCredito())
                .rutUsuario(datos.rutUsuario().trim())
                .rutClave(Rut.clave(datos.rutUsuario()))
                .nombreUsuario(datos.nombreUsuario().trim())
                .empresaCodigo(datos.empresaCodigo().trim())
                .concepto(datos.concepto().trim())
                .cuotaActual(inicial)
                .totalCuotas(total)
                .monto(datos.monto())
                .fechaVencimiento(datos.fechaVencimiento().trim())
                .estado(EstadoCompromiso.PENDIENTE)
                .recargoAplicado(0)
                .registradoPor(datos.registradoPor().trim())
                .build();
        return CompromisoResponse.desde(compromisoRepository.save(nuevo));
    }

    public CompromisoResponse cambiarEstado(String folio, CambiarEstadoRequest datos) {
        Compromiso compromiso = buscar(folio);
        EstadoCompromiso actual = compromiso.getEstado();
        EstadoCompromiso nuevo = datos.estado();

        if (nuevo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el nuevo estado");
        }
        if (nuevo == EstadoCompromiso.PAGADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para pagar un compromiso usa la opción de pago");
        }
        if (nuevo == EstadoCompromiso.ANULADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para anular un compromiso usa la opción de anular, con su motivo");
        }
        boolean vigente = actual == EstadoCompromiso.PENDIENTE || actual == EstadoCompromiso.VENCIDO;
        boolean permitido = switch (nuevo) {
            case EN_REVISION -> vigente;
            case PENDIENTE, VENCIDO -> actual == EstadoCompromiso.EN_REVISION;
            default -> false;
        };
        if (!permitido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede pasar de " + actual + " a " + nuevo);
        }

        boolean atrasado = vencimientoService.estaAtrasado(compromiso, LocalDate.now());
        if (nuevo == EstadoCompromiso.VENCIDO || (nuevo == EstadoCompromiso.PENDIENTE && atrasado)) {
            vencimientoService.marcarVencido(compromiso);
        } else {
            compromiso.setEstado(nuevo);
        }
        return CompromisoResponse.desde(compromisoRepository.save(compromiso));
    }

    public CompromisoResponse anular(String folio, AnularRequest datos) {
        Compromiso compromiso = buscar(folio);

        if (datos == null || datos.motivo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el motivo de la anulación");
        }
        if (datos.motivo() == MotivoAnulacion.OTRO && vacio(datos.comentario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si el motivo es Otro, escribe un comentario");
        }
        if (vacio(datos.ejecutivoCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el ejecutivo que anula");
        }
        if (!compromiso.getEmpresaCodigo().equals(datos.empresaCodigo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo la empresa dueña del compromiso puede anularlo");
        }
        EstadoCompromiso estado = compromiso.getEstado();
        if (estado == EstadoCompromiso.PAGADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un compromiso pagado nunca se anula");
        }
        if (estado == EstadoCompromiso.ANULADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El compromiso ya está anulado");
        }

        compromiso.setEstado(EstadoCompromiso.ANULADO);
        compromiso.setMotivoAnulacion(datos.motivo());
        compromiso.setComentarioAnulacion(vacio(datos.comentario()) ? null : datos.comentario().trim());
        compromiso.setAnuladoPor(datos.ejecutivoCodigo().trim());
        compromiso.setFechaAnulacion(LocalDateTime.now().format(FORMATO_FECHA_HORA));
        return CompromisoResponse.desde(compromisoRepository.save(compromiso));
    }

    @Transactional
    public PagoResponse pagar(String folio, PagoRequest datos) {
        vencimientoService.actualizarVencidos();
        Compromiso compromiso = buscar(folio);
        EstadoCompromiso estado = compromiso.getEstado();
        if (estado != EstadoCompromiso.PENDIENTE && estado != EstadoCompromiso.VENCIDO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este compromiso no se puede pagar");
        }

        CanalPago canal = datos != null && datos.canal() != null ? datos.canal() : CanalPago.APP;
        String medio = datos != null && !vacio(datos.medioPago()) ? datos.medioPago().trim() : "Pago simulado";

        Comprobante comprobante = Comprobante.builder()
                .numero(siguienteNumeroComprobante())
                .compromisoFolio(compromiso.getFolio())
                .rutUsuario(compromiso.getRutUsuario())
                .rutClave(compromiso.getRutClave())
                .empresaCodigo(compromiso.getEmpresaCodigo())
                .fechaHora(LocalDateTime.now().format(FORMATO_FECHA_HORA))
                .montoPagado(compromiso.getMonto() + compromiso.getRecargoAplicado())
                .canal(canal)
                .aTiempo(!vencimientoService.estaAtrasado(compromiso, LocalDate.now()))
                .medioPago(medio)
                .build();

        compromiso.setEstado(EstadoCompromiso.PAGADO);
        compromiso.setRecargoAplicado(0);
        compromisoRepository.save(compromiso);
        Comprobante guardado = comprobanteRepository.save(comprobante);

        Compromiso siguiente = generarSiguienteCuota(compromiso);
        return new PagoResponse(
                ComprobanteResponse.desde(guardado),
                siguiente == null ? null : CompromisoResponse.desde(siguiente)
        );
    }

    private Compromiso generarSiguienteCuota(Compromiso pagada) {
        int numeroSiguiente = pagada.getCuotaActual() + 1;
        if (numeroSiguiente > pagada.getTotalCuotas()) {
            return null;
        }
        return compromisoRepository.findByCodigoCreditoAndCuotaActual(pagada.getCodigoCredito(), numeroSiguiente)
                .orElseGet(() -> {
                    LocalDate vencimiento = LocalDate.parse(pagada.getFechaVencimiento(), FORMATO_FECHA).plusMonths(1);
                    Compromiso siguiente = Compromiso.builder()
                            .folio(siguienteFolio())
                            .codigoCredito(pagada.getCodigoCredito())
                            .rutUsuario(pagada.getRutUsuario())
                            .rutClave(pagada.getRutClave())
                            .nombreUsuario(pagada.getNombreUsuario())
                            .empresaCodigo(pagada.getEmpresaCodigo())
                            .concepto(pagada.getConcepto())
                            .cuotaActual(numeroSiguiente)
                            .totalCuotas(pagada.getTotalCuotas())
                            .monto(pagada.getMonto())
                            .fechaVencimiento(vencimiento.format(FORMATO_FECHA))
                            .estado(EstadoCompromiso.PENDIENTE)
                            .recargoAplicado(0)
                            .registradoPor(pagada.getRegistradoPor())
                            .build();
                    if (vencimientoService.estaAtrasado(siguiente, LocalDate.now())) {
                        vencimientoService.marcarVencido(siguiente);
                    }
                    return compromisoRepository.save(siguiente);
                });
    }

    public List<ComprobanteResponse> listarComprobantes(String rut) {
        return comprobanteRepository.findByRutClaveOrderByNumeroDesc(Rut.clave(rut)).stream()
                .map(ComprobanteResponse::desde)
                .toList();
    }

    private Compromiso buscar(String folio) {
        return compromisoRepository.findByFolio(folio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compromiso no encontrado"));
    }

    private List<CompromisoResponse> ordenar(List<Compromiso> compromisos) {
        return compromisos.stream()
                .sorted(Comparator.comparing((Compromiso c) -> LocalDate.parse(c.getFechaVencimiento(), FORMATO_FECHA)))
                .map(CompromisoResponse::desde)
                .toList();
    }

    private String siguienteFolio() {
        int mayor = compromisoRepository.findAll().stream()
                .mapToInt(c -> numeroFinal(c.getFolio()))
                .max()
                .orElse(100);
        return "CP-" + LocalDate.now().getYear() + "-" + String.format("%04d", mayor + 1);
    }

    private String siguienteCodigoCredito() {
        int mayor = compromisoRepository.findAll().stream()
                .mapToInt(c -> numeroFinal(c.getCodigoCredito()))
                .max()
                .orElse(0);
        return "CR-" + LocalDate.now().getYear() + "-" + String.format("%04d", mayor + 1);
    }

    private String siguienteNumeroComprobante() {
        int mayor = comprobanteRepository.findAll().stream()
                .mapToInt(c -> numeroFinal(c.getNumero()))
                .max()
                .orElse(0);
        return "CPR-" + String.format("%06d", mayor + 1);
    }

    private int numeroFinal(String codigo) {
        try {
            return Integer.parseInt(codigo.substring(codigo.lastIndexOf('-') + 1));
        } catch (RuntimeException e) {
            return 0;
        }
    }

    private void validarFecha(String fecha) {
        try {
            LocalDate.parse(fecha == null ? "" : fecha.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de vencimiento debe tener el formato dd-MM-aaaa");
        }
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
