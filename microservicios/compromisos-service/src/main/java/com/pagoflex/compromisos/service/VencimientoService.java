package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.Empresa;
import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VencimientoService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final CompromisoRepository compromisoRepository;
    private final EmpresaRepository empresaRepository;

    @Scheduled(cron = "0 1 0 * * *")
    public void actualizarVencidos() {
        LocalDate hoy = LocalDate.now();
        List<Compromiso> atrasados = compromisoRepository.findByEstado(EstadoCompromiso.PENDIENTE).stream()
                .filter(compromiso -> estaAtrasado(compromiso, hoy))
                .toList();
        atrasados.forEach(this::marcarVencido);
        compromisoRepository.saveAll(atrasados);
    }

    public boolean estaAtrasado(Compromiso compromiso, LocalDate fecha) {
        return LocalDate.parse(compromiso.getFechaVencimiento(), FORMATO_FECHA).isBefore(fecha);
    }

    public void marcarVencido(Compromiso compromiso) {
        compromiso.setEstado(EstadoCompromiso.VENCIDO);
        if (compromiso.getRecargoAplicado() == 0) {
            compromiso.setRecargoAplicado(recargoDe(compromiso.getEmpresaCodigo()));
        }
    }

    private int recargoDe(String empresaCodigo) {
        return empresaRepository.findById(empresaCodigo)
                .map(Empresa::getRecargoAtraso)
                .orElse(0);
    }
}
