package com.example.pagoflex.model

// Comprobante de un pago (RF-05, RF-06). Es inmutable: representa un pago ya hecho (RN-05).
// El campo aTiempo alimenta el indicador de cumplimiento del historial (RF-08, RN-10).
data class Comprobante(
    val folio: String,            // folio del comprobante, ej "CPR-000460"
    val folioCompromiso: String,  // compromiso que se pago
    val concepto: String,
    val empresa: String,
    val monto: Int,
    val fechaHora: String,        // "09-10-2026 14:32"
    val aTiempo: Boolean,         // se pago en fecha (true) o atrasado (false)
    val canal: String = "App"
)
