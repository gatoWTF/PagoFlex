package com.example.pagoflex.model

data class CompromisoEmpresa(
    val folio: String,
    val deudor: String,
    val rutDeudor: String,
    val concepto: String,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso
) {

    val sePuedeAnular: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO
}
