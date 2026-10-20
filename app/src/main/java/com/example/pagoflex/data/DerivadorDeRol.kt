package com.example.pagoflex.data

import com.example.pagoflex.model.RolUsuario

object DerivadorDeRol {

    const val DOMINIO_AGENTE = "pagoflex.ejemplo"

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
