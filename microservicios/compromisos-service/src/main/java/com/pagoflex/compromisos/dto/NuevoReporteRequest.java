package com.pagoflex.compromisos.dto;

public record NuevoReporteRequest(
        String compromisoFolio,
        String rutUsuario,
        String motivo,
        String comentario
) {
}
