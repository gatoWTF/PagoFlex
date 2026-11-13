package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.CambiarEstadoRequest;
import com.pagoflex.compromisos.dto.ComprobanteResponse;
import com.pagoflex.compromisos.dto.CompromisoResponse;
import com.pagoflex.compromisos.dto.NuevoCompromisoRequest;
import com.pagoflex.compromisos.dto.PagoRequest;
import com.pagoflex.compromisos.model.CanalPago;
import com.pagoflex.compromisos.model.Comprobante;
import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.model.Rut;
import com.pagoflex.compromisos.repository.ComprobanteRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    private final CompromisoRepository compromisoRepository;
    private final ComprobanteRepository comprobanteRepository;

    public List<CompromisoResponse> listarPorUsuario(String rut) {
        return ordenar(compromisoRepository.findByRutClave(Rut.clave(rut)));
    }

    public List<CompromisoResponse> listarPorEmpresa(String empresaCodigo) {
        return ordenar(compromisoRepository.findByEmpresaCodigo(empresaCodigo));
    }

    public CompromisoResponse obtener(String folio) {
        return CompromisoResponse.desde(buscar(folio));
    }

    public CompromisoResponse registrar(NuevoCompromisoRequest datos) {
        if (vacio(datos.rutUsuario()) || vacio(datos.nombreUsuario()) || vacio(datos.empresaCodigo())
                || vacio(datos.concepto()) || vacio(datos.registradoPor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUT, nombre, empresa, concepto y ejecutivo son obligatorios");
        }
        if (datos.monto() == null || datos.monto() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero");
        }
        validarFecha(datos.fechaVencimiento());

        Compromiso nuevo = Compromiso.builder()
                .folio(siguienteFolio())
                .rutUsuario(datos.rutUsuario().trim())
                .rutClave(Rut.clave(datos.rutUsuario()))
                .nombreUsuario(datos.nombreUsuario().trim())
                .empresaCodigo(datos.empresaCodigo().trim())
                .concepto(datos.concepto().trim())
                .numeroCuota(vacio(datos.numeroCuota()) ? null : datos.numeroCuota().trim())
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
        boolean vigente = actual == EstadoCompromiso.PENDIENTE || actual == EstadoCompromiso.VENCIDO;
        boolean permitido = switch (nuevo) {
            case ANULADO, EN_REVISION -> vigente;
            case PENDIENTE, VENCIDO -> actual == EstadoCompromiso.EN_REVISION;
            default -> false;
        };
        if (!permitido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede pasar de " + actual + " a " + nuevo);
        }

        compromiso.setEstado(nuevo);
        return CompromisoResponse.desde(compromisoRepository.save(compromiso));
    }

    public ComprobanteResponse pagar(String folio, PagoRequest datos) {
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
                .aTiempo(estado != EstadoCompromiso.VENCIDO)
                .medioPago(medio)
                .build();

        compromiso.setEstado(EstadoCompromiso.PAGADO);
        compromiso.setRecargoAplicado(0);
        compromisoRepository.save(compromiso);
        return ComprobanteResponse.desde(comprobanteRepository.save(comprobante));
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
