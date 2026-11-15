package com.pagoflex.compromisos.dto;

public record NuevoCompromisoRequest(
        String rutUsuario,
        String nombreUsuario,
        String empresaCodigo,
        String concepto,
        Integer totalCuotas,
        Integer cuotaInicial,
        Integer monto,
        String fechaVencimiento,
        String registradoPor
) {
}
