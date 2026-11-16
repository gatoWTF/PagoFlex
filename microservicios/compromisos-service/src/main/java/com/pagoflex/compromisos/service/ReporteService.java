package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.CambiarEstadoRequest;
import com.pagoflex.compromisos.dto.GestionReporteRequest;
import com.pagoflex.compromisos.dto.NuevoReporteRequest;
import com.pagoflex.compromisos.dto.ReporteResponse;
import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.Empresa;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.model.EstadoReporte;
import com.pagoflex.compromisos.model.Reporte;
import com.pagoflex.compromisos.model.Rut;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EmpresaRepository;
import com.pagoflex.compromisos.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String RESPUESTA_POR_DEFECTO = "Gestionado por soporte.";

    private final ReporteRepository reporteRepository;
    private final CompromisoRepository compromisoRepository;
    private final EmpresaRepository empresaRepository;
    private final CompromisoService compromisoService;

    public List<ReporteResponse> listar(String rut) {
        List<Reporte> reportes = vacio(rut)
                ? reporteRepository.findAll()
                : reporteRepository.findByRutClave(Rut.clave(rut));
        return reportes.stream()
                .sorted(Comparator.comparing(Reporte::getEstado)
                        .thenComparing(Reporte::getNumero, Comparator.reverseOrder()))
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional
    public ReporteResponse crear(NuevoReporteRequest datos) {
        if (datos == null || vacio(datos.compromisoFolio()) || vacio(datos.rutUsuario()) || vacio(datos.motivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Folio, RUT y motivo son obligatorios");
        }
        Compromiso compromiso = compromisoRepository.findByFolio(datos.compromisoFolio().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compromiso no encontrado"));
        if (!compromiso.getRutClave().equals(Rut.clave(datos.rutUsuario()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el titular del compromiso puede reportarlo");
        }
        EstadoCompromiso estado = compromiso.getEstado();
        if (estado != EstadoCompromiso.PENDIENTE && estado != EstadoCompromiso.VENCIDO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se pueden reportar compromisos pendientes o vencidos");
        }

        compromisoService.cambiarEstado(compromiso.getFolio(), new CambiarEstadoRequest(EstadoCompromiso.EN_REVISION));

        Reporte nuevo = Reporte.builder()
                .numero(siguienteNumero())
                .compromisoFolio(compromiso.getFolio())
                .rutUsuario(compromiso.getRutUsuario())
                .rutClave(compromiso.getRutClave())
                .motivo(datos.motivo().trim())
                .comentario(datos.comentario() == null ? "" : datos.comentario().trim())
                .fecha(LocalDate.now().format(FORMATO_FECHA))
                .estado(EstadoReporte.ABIERTO)
                .build();
        return aRespuesta(reporteRepository.save(nuevo));
    }

    public ReporteResponse tomar(String numero, GestionReporteRequest datos) {
        String agente = agenteObligatorio(datos);
        Reporte reporte = buscar(numero);
        if (reporte.getEstado() != EstadoReporte.ABIERTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este reporte ya fue tomado por " + reporte.getAgenteResponsable());
        }
        reporte.setEstado(EstadoReporte.EN_GESTION);
        reporte.setAgenteResponsable(agente);
        return aRespuesta(reporteRepository.save(reporte));
    }

    @Transactional
    public ReporteResponse resolver(String numero, GestionReporteRequest datos) {
        String agente = agenteObligatorio(datos);
        Reporte reporte = buscar(numero);
        if (reporte.getEstado() == EstadoReporte.RESUELTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El reporte ya está resuelto");
        }
        if (reporte.getEstado() == EstadoReporte.ABIERTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Toma el reporte antes de resolverlo");
        }
        if (!agente.equals(reporte.getAgenteResponsable())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este reporte lo gestiona " + reporte.getAgenteResponsable());
        }

        reporte.setEstado(EstadoReporte.RESUELTO);
        reporte.setRespuesta(vacio(datos.respuesta()) ? RESPUESTA_POR_DEFECTO : datos.respuesta().trim());
        reporteRepository.save(reporte);

        compromisoRepository.findByFolio(reporte.getCompromisoFolio())
                .filter(c -> c.getEstado() == EstadoCompromiso.EN_REVISION)
                .ifPresent(c -> compromisoService.cambiarEstado(c.getFolio(), new CambiarEstadoRequest(EstadoCompromiso.PENDIENTE)));
        return aRespuesta(reporte);
    }

    private Reporte buscar(String numero) {
        return reporteRepository.findByNumero(numero)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
    }

    private String agenteObligatorio(GestionReporteRequest datos) {
        if (datos == null || vacio(datos.agenteCodigo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica el agente que gestiona el reporte");
        }
        return datos.agenteCodigo().trim();
    }

    private ReporteResponse aRespuesta(Reporte reporte) {
        Compromiso compromiso = compromisoRepository.findByFolio(reporte.getCompromisoFolio()).orElse(null);
        String empresaCodigo = compromiso == null ? null : compromiso.getEmpresaCodigo();
        String empresaNombre = empresaCodigo == null ? null
                : empresaRepository.findById(empresaCodigo).map(Empresa::getNombre).orElse(empresaCodigo);
        return new ReporteResponse(
                reporte.getNumero(),
                reporte.getCompromisoFolio(),
                reporte.getRutUsuario(),
                compromiso == null ? null : compromiso.getNombreUsuario(),
                empresaCodigo,
                empresaNombre,
                compromiso == null ? null : compromiso.getConcepto(),
                compromiso == null ? null : compromiso.getCuotaActual(),
                compromiso == null ? null : compromiso.getTotalCuotas(),
                compromiso == null ? null : compromiso.getMonto() + compromiso.getRecargoAplicado(),
                compromiso == null ? null : compromiso.getEstado(),
                reporte.getMotivo(),
                reporte.getComentario(),
                reporte.getFecha(),
                reporte.getEstado(),
                reporte.getAgenteResponsable(),
                reporte.getRespuesta()
        );
    }

    private String siguienteNumero() {
        int mayor = reporteRepository.findAll().stream()
                .mapToInt(r -> numeroFinal(r.getNumero()))
                .max()
                .orElse(0);
        return "REP-" + String.format("%04d", mayor + 1);
    }

    private int numeroFinal(String codigo) {
        try {
            return Integer.parseInt(codigo.substring(codigo.lastIndexOf('-') + 1));
        } catch (RuntimeException e) {
            return 0;
        }
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
