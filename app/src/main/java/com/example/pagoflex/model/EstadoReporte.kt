package com.example.pagoflex.model

// Estados de un reporte de problema (RN-09, RN-23): Abierto -> En gestion -> Resuelto.
enum class EstadoReporte(val etiqueta: String) {
    ABIERTO("Abierto"),
    EN_GESTION("En gestión"),
    RESUELTO("Resuelto")
}
