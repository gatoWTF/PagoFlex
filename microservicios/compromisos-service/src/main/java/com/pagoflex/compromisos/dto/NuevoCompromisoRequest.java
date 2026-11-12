package com.pagoflex.compromisos.dto;

public record NuevoCompromisoRequest(
        String rutUsuario,
        String nombreUsuario,
        String empresaCodigo,
        String concepto,
        String numeroCuota,
        Integer monto,
        String fechaVencimiento,
        String registradoPor
) {
}
