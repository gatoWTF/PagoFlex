package com.example.pagoflex.model

data class Comprobante(
    val folio: String,
    val folioCompromiso: String,
    val concepto: String,
    val empresa: String,
    val monto: Int,
    val fechaHora: String,
    val aTiempo: Boolean,
    val canal: String = "App",
    val cuota: String? = null,
    val siguienteCuota: Compromiso? = null,
    val creditoTerminado: Boolean = false
)
