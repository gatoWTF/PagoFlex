package com.example.pagoflex.model

// Canal por el que se realizo un pago (RN-07). Todos los canales cuentan en el historial.
enum class CanalPago(val etiqueta: String) {
    APP("App"),
    PORTAL("Portal de pagos"),
    AGENTE_CONVERSACIONAL("Agente conversacional")
}
