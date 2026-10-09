package com.example.pagoflex.model

// Estados de un compromiso de pago (RN-01).
// Un compromiso Pagado o Anulado no cambia mas de estado.
enum class EstadoCompromiso(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    VENCIDO("Vencido"),
    PAGADO("Pagado"),
    EN_REVISION("En revisión"),
    ANULADO("Anulado");

    // Orden en que se muestran en las listas: primero lo accionable, al final lo cerrado.
    // Al cambiar de estado (p. ej. anular) el item se reubica y se anima (animateItem).
    val prioridadLista: Int
        get() = when (this) {
            PENDIENTE, VENCIDO -> 0
            EN_REVISION -> 1
            PAGADO -> 2
            ANULADO -> 3
        }
}
