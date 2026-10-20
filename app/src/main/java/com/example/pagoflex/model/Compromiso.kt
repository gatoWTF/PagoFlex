package com.example.pagoflex.model

data class Compromiso(
    val folio: String,
    val empresa: String,
    val rubro: String,
    val concepto: String,
    val cuota: String?,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso,
    val recargo: Int = 0
) {

    val totalAPagar: Int get() = monto + recargo

    val estaPorPagar: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO

    val sePuedePagar: Boolean get() = estaPorPagar
}
