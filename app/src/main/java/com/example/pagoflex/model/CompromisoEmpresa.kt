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
    val estado: EstadoCompromiso,
    val recargo: Int = 0,
    val motivoAnulacion: String? = null
) {

    val cuota: String get() = "$cuotaActual de $totalCuotas"

    val sePuedeAnular: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE ||
            estado == EstadoCompromiso.VENCIDO ||
            estado == EstadoCompromiso.EN_REVISION
}
