package com.pagoflex.compromisos.dto;

import com.pagoflex.compromisos.model.EstadoCompromiso;
import com.pagoflex.compromisos.model.EstadoReporte;

public record ReporteResponse(
        String numero,
        String compromisoFolio,
        String rutUsuario,
        String nombreUsuario,
        String empresaCodigo,
        String empresaNombre,
        String concepto,
        Integer cuotaActual,
        Integer totalCuotas,
        Integer montoCuota,
        EstadoCompromiso estadoCompromiso,
        String motivo,
        String comentario,
        String fecha,
        EstadoReporte estado,
        String agenteResponsable,
        String respuesta
) {
}
