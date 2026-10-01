package com.example.pagoflex.model

data class Pago(
    val id: Int,
    val descripcion: String,        // "Cuenta de luz"
    val empresa: String,            // "Enel"
    val categoria: CategoriaPago,   // decide qué ícono mostrar
    val monto: Int,                 // en pesos, sin decimales
    val fechaVencimiento: String,   // "05/10/2026"
    val estado: EstadoPago
)