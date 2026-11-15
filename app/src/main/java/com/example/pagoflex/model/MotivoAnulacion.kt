package com.example.pagoflex.model

enum class MotivoAnulacion(val etiqueta: String) {
    REPACTACION("Repactación"),
    ERROR_DE_REGISTRO("Error de registro"),
    PAGADO_DIRECTAMENTE("Pagado directamente a la empresa"),
    OTRO("Otro")
}
