package com.example.pagoflex.model

data class Compromiso(
    val folio: String,
    val empresa: String,
    val rubro: String,
    val concepto: String,
    val cuotaActual: Int,
    val totalCuotas: Int,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso,
    val recargo: Int = 0,
    val codigoCredito: String = "",
    val motivoAnulacion: String? = null
) {

    val cuota: String get() = "$cuotaActual de $totalCuotas"

    val cuotasPorPagar: Int get() = totalCuotas - cuotaActual + 1

    val saldoCredito: Int get() = cuotasPorPagar * monto + recargo

    val esUltimaCuota: Boolean get() = cuotaActual == totalCuotas

    val totalAPagar: Int get() = monto + recargo

    val estaPorPagar: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO

    val sePuedePagar: Boolean get() = estaPorPagar
}
