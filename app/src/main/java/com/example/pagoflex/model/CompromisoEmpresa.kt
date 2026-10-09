package com.example.pagoflex.model

// Vista de un compromiso desde la empresa cliente (rol ejecutivo, R-03).
// A la empresa le interesa quien debe (el usuario final) y el estado de cobro.
data class CompromisoEmpresa(
    val folio: String,
    val deudor: String,            // nombre del usuario final que debe
    val rutDeudor: String,
    val concepto: String,
    val monto: Int,
    val fechaVencimiento: String,  // DD-MM-AAAA
    val estado: EstadoCompromiso
) {
    // Se puede anular solo si sigue por cobrar; un pagado o anulado no cambia (RN-01).
    val sePuedeAnular: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO
}
