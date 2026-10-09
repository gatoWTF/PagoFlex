package com.example.pagoflex.ui.navigation

import com.example.pagoflex.model.RolUsuario

// Destinos de navegacion de la app. Cada rol tiene su propio home (RNF-13, RN-22):
// cada usuario ve solo las pantallas que le corresponden.
sealed class Rutas(val ruta: String) {
    // Sesion simulada: se elige el rol al entrar (RF-01, FA-01).
    object SelectorRol : Rutas("selector")

    // Usuario final (R-01)
    object InicioUsuario : Rutas("usuario/inicio")
    object DetalleCompromiso : Rutas("usuario/detalle/{folio}") {
        const val ARG_FOLIO = "folio"
        // crear("CP-2026-0101") -> "usuario/detalle/CP-2026-0101"
        fun crear(folio: String) = "usuario/detalle/$folio"
    }

    // Roles internos (acotados): se completan en los proximos avances.
    object HomeAgente : Rutas("agente/inicio")       // R-02: gestiona reportes
    object HomeEjecutivo : Rutas("ejecutivo/inicio") // R-03: compromisos y cobranza

    companion object {
        // A que home entra cada rol tras elegirlo en el selector.
        fun homeDeRol(rol: RolUsuario): Rutas = when (rol) {
            RolUsuario.USUARIO_FINAL -> InicioUsuario
            RolUsuario.AGENTE -> HomeAgente
            RolUsuario.EJECUTIVO -> HomeEjecutivo
        }
    }
}
