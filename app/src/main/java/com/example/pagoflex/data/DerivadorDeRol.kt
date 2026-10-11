package com.example.pagoflex.data

import com.example.pagoflex.model.RolUsuario

/**
 * Deduce el rol de una persona a partir del dominio de su correo, en vez de
 * dejar que cada quien elija su rol (asi nadie se pone un rol que no le toca).
 * Esta regla calza con los datos del Anexo 9:
 *  - @pagoflex.ejemplo  -> agente PagoFlex (personal interno)
 *  - @correo.ejemplo    -> usuario final (cliente)
 *  - cualquier otro dominio (empresa) -> ejecutivo de esa empresa
 *
 * Es una funcion pura (no toca la base ni la red), por eso se puede probar sola.
 */
object DerivadorDeRol {

    const val DOMINIO_AGENTE = "pagoflex.ejemplo"
    const val DOMINIO_USUARIO = "correo.ejemplo"

    fun rolPorCorreo(correo: String): RolUsuario {
        val dominio = correo.trim().lowercase().substringAfter('@', "")
        return when {
            dominio == DOMINIO_AGENTE -> RolUsuario.AGENTE
            dominio == DOMINIO_USUARIO -> RolUsuario.USUARIO_FINAL
            dominio.isEmpty() -> RolUsuario.USUARIO_FINAL
            else -> RolUsuario.EJECUTIVO
        }
    }
}
