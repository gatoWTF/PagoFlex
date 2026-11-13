package com.pagoflex.compromisos.dto;

public record PagoResponse(
        ComprobanteResponse comprobante,
        CompromisoResponse siguienteCuota
) {
}
