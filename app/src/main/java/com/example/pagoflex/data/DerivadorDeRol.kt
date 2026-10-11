package com.example.pagoflex.data

import com.example.pagoflex.model.RolUsuario

/**
 * Deduce el rol de una persona a partir del dominio de su correo, en vez de
 * dejar que cada quien elija su rol (asi nadie se pone un rol que no le toca).
 * Esta regla calza con los datos del Anexo 9:
 *  - @pagoflex.ejemplo            -> agente PagoFlex (personal interno)
 *  - dominio de una empresa cliente -> ejecutivo de esa empresa
 *  - cualquier otro correo (gmail, hotmail, duoc, correo.ejemplo...) -> usuario final
 *
 * El usuario final es el rol por defecto: quien usa un correo personal cualquiera
 * entra solo a revisar sus propios gastos. El rol de ejecutivo queda reservado a
 * los dominios de las empresas registradas.
 *
 * Es una funcion pura (no toca la base ni la red), por eso se puede probar sola.
 */
object DerivadorDeRol {

    const val DOMINIO_AGENTE = "pagoflex.ejemplo"

    // Dominios de las empresas cliente (Anexo 9). Solo estos dan rol de ejecutivo.
    val DOMINIOS_EMPRESA = setOf(
        "altosmaipo.ejemplo",
        "bosquenativo.ejemplo",
        "creditoandino.ejemplo",
        "horizontesur.ejemplo",
        "manosabiertas.ejemplo",
        "loshalcones.ejemplo",
        "itcordillera.ejemplo",
        "plazaoriente.ejemplo"
    )

    fun rolPorCorreo(correo: String): RolUsuario {
        val dominio = correo.trim().lowercase().substringAfter('@', "")
        return when {
            dominio == DOMINIO_AGENTE -> RolUsuario.AGENTE
            dominio in DOMINIOS_EMPRESA -> RolUsuario.EJECUTIVO
            else -> RolUsuario.USUARIO_FINAL
        }
    }
}
