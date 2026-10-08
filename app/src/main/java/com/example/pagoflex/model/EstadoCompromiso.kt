package com.example.pagoflex.model

// Estados de un compromiso de pago (RN-01).
// Un compromiso Pagado o Anulado no cambia mas de estado.
enum class EstadoCompromiso(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    VENCIDO("Vencido"),
    PAGADO("Pagado"),
    EN_REVISION("En revisión"),
    ANULADO("Anulado")
}
