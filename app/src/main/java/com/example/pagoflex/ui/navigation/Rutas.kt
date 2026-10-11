package com.example.pagoflex.ui.navigation

import com.example.pagoflex.model.RolUsuario

// Destinos de navegacion de la app. La entrada es login/registro (RF-01): el rol
// se deduce del correo y cada rol tiene su propio home (RNF-13, RN-22).
sealed class Rutas(val ruta: String) {
    // Entrada: sesion simulada con login y registro.
    object Login : Rutas("login")
    object Registro : Rutas("registro")

    // Usuario final (R-01)
    object InicioUsuario : Rutas("usuario/inicio")
    object Perfil : Rutas("usuario/perfil")
    object Historial : Rutas("usuario/historial")
    object Configuracion : Rutas("usuario/configuracion")
    object DetalleCompromiso : Rutas("usuario/detalle/{folio}") {
        const val ARG_FOLIO = "folio"
        // crear("CP-2026-0101") -> "usuario/detalle/CP-2026-0101"
        fun crear(folio: String) = "usuario/detalle/$folio"
    }
    object Comprobante : Rutas("usuario/comprobante/{folioComprobante}") {
        const val ARG_FOLIO = "folioComprobante"
        fun crear(folioComprobante: String) = "usuario/comprobante/$folioComprobante"
    }
    object ReportarProblema : Rutas("usuario/reportar/{folio}") {
        const val ARG_FOLIO = "folio"
        fun crear(folio: String) = "usuario/reportar/$folio"
    }

    // Agente (R-02): se completa en los proximos avances.
    object HomeAgente : Rutas("agente/inicio")       // gestiona reportes

    // Ejecutivo de empresa (R-03)
    object HomeEjecutivo : Rutas("ejecutivo/inicio") // compromisos y cobranza
    object RegistrarCompromiso : Rutas("ejecutivo/registrar")

    companion object {
        // A que home entra cada rol tras iniciar sesion o registrarse.
        fun homeDeRol(rol: RolUsuario): Rutas = when (rol) {
            RolUsuario.USUARIO_FINAL -> InicioUsuario
            RolUsuario.AGENTE -> HomeAgente
            RolUsuario.EJECUTIVO -> HomeEjecutivo
        }
    }
}
