package com.example.pagoflex.ui.navigation

import com.example.pagoflex.model.RolUsuario

sealed class Rutas(val ruta: String) {

    object Login : Rutas("login")
    object Registro : Rutas("registro")

    object InicioUsuario : Rutas("usuario/inicio")
    object Perfil : Rutas("usuario/perfil")
    object Historial : Rutas("usuario/historial")
    object Configuracion : Rutas("usuario/configuracion")
    object DetalleCompromiso : Rutas("usuario/detalle/{folio}") {
        const val ARG_FOLIO = "folio"

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

    object HomeAgente : Rutas("agente/inicio")

    object HomeEjecutivo : Rutas("ejecutivo/inicio")
    object RegistrarCompromiso : Rutas("ejecutivo/registrar")

    companion object {

        fun homeDeRol(rol: RolUsuario): Rutas = when (rol) {
            RolUsuario.USUARIO_FINAL -> InicioUsuario
            RolUsuario.AGENTE -> HomeAgente
            RolUsuario.EJECUTIVO -> HomeEjecutivo
        }
    }
}
