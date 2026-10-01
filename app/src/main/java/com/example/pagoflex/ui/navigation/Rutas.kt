package com.example.pagoflex.ui.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Inicio : Rutas("inicio")
    object Historial : Rutas("historial")
    object Configuracion : Rutas("configuracion")
    object DetallePago : Rutas("detalle/{pagoId}") {
        // crear(1) -> "detalle/1", así Detalle sabe qué pago mostrar
        fun crear(pagoId: Int) = "detalle/$pagoId"
    }
}
