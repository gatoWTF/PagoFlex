package com.example.pagoflex.model

enum class EstadoCompromiso(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    VENCIDO("Vencido"),
    PAGADO("Pagado"),
    EN_REVISION("En revisión"),
    ANULADO("Anulado");

    val prioridadLista: Int
        get() = when (this) {
            PENDIENTE, VENCIDO -> 0
            EN_REVISION -> 1
            PAGADO -> 2
            ANULADO -> 3
        }
}
