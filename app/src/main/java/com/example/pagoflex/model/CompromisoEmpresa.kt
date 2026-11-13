package com.example.pagoflex.model

data class CompromisoEmpresa(
    val folio: String,
    val deudor: String,
    val rutDeudor: String,
    val concepto: String,
    val cuotaActual: Int,
    val totalCuotas: Int,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso
) {

    val cuota: String get() = "$cuotaActual de $totalCuotas"

    val sePuedeAnular: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO
}
