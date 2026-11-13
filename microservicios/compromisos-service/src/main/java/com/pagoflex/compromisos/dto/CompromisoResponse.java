package com.pagoflex.compromisos.dto;

import com.pagoflex.compromisos.model.Compromiso;
import com.pagoflex.compromisos.model.EstadoCompromiso;

public record CompromisoResponse(
        String folio,
        String rutUsuario,
        String nombreUsuario,
        String empresaCodigo,
        String concepto,
        String numeroCuota,
        Integer monto,
        String fechaVencimiento,
        EstadoCompromiso estado,
        Integer recargoAplicado,
        String registradoPor
) {
    public static CompromisoResponse desde(Compromiso c) {
        return new CompromisoResponse(
                c.getFolio(), c.getRutUsuario(), c.getNombreUsuario(), c.getEmpresaCodigo(),
                c.getConcepto(), c.getNumeroCuota(), c.getMonto(), c.getFechaVencimiento(),
                c.getEstado(), c.getRecargoAplicado(), c.getRegistradoPor()
        );
    }
}
