package com.pagoflex.compromisos.dto;

import com.pagoflex.compromisos.model.CanalPago;
import com.pagoflex.compromisos.model.Comprobante;

public record ComprobanteResponse(
        String numero,
        String compromisoFolio,
        String rutUsuario,
        String empresaCodigo,
        String fechaHora,
        Integer montoPagado,
        CanalPago canal,
        Boolean aTiempo,
        String medioPago
) {
    public static ComprobanteResponse desde(Comprobante c) {
        return new ComprobanteResponse(
                c.getNumero(), c.getCompromisoFolio(), c.getRutUsuario(), c.getEmpresaCodigo(),
                c.getFechaHora(), c.getMontoPagado(), c.getCanal(), c.getATiempo(), c.getMedioPago()
        );
    }
}
