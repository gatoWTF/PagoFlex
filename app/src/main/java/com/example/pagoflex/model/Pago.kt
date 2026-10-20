package com.example.pagoflex.model

data class Pago(
    val id: Int,
    val descripcion: String,
    val empresa: String,
    val categoria: CategoriaPago,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoPago
)
