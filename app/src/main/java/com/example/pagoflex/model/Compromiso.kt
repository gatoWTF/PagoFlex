package com.example.pagoflex.model

// Modelo de un compromiso para la interfaz (datos en memoria por ahora).
// Mas adelante se reemplaza por la lectura desde Room.
data class Compromiso(
    val folio: String,
    val empresa: String,            // nombre de fantasia, como lo reconoce la persona
    val rubro: String,
    val concepto: String,
    val cuota: String?,             // "10/12" o null si no aplica
    val monto: Int,
    val fechaVencimiento: String,   // DD-MM-AAAA
    val estado: EstadoCompromiso,
    val recargo: Int = 0            // recargo por atraso si esta vencido (RN-03)
) {
    // Total a pagar: monto mas recargo si corresponde (RN-03)
    val totalAPagar: Int get() = monto + recargo

    // Lo que falta pagar: pendientes y vencidos (RF-02)
    val estaPorPagar: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO

    // Se puede pagar solo si esta pendiente o vencido (RN-08, RN-09)
    val sePuedePagar: Boolean get() = estaPorPagar
}
