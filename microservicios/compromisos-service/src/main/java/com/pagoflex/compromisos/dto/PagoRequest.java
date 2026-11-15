package com.pagoflex.compromisos.dto;

import com.pagoflex.compromisos.model.CanalPago;

public record PagoRequest(CanalPago canal, String medioPago) {
}
