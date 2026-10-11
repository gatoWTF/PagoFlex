package com.example.pagoflex.model

// Preferencia de apariencia elegida por la persona en Configuracion.
// SISTEMA sigue el modo del telefono; CLARO y OSCURO lo fuerzan.
enum class ModoTema(val etiqueta: String) {
    CLARO("Claro"),
    OSCURO("Oscuro"),
    SISTEMA("Según el sistema")
}
