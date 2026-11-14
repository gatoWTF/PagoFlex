package com.pagoflex.compromisos.dto;

import com.pagoflex.compromisos.model.MotivoAnulacion;

public record AnularRequest(
        MotivoAnulacion motivo,
        String comentario,
        String ejecutivoCodigo,
        String empresaCodigo
) {
}
