package com.example.pagoflex.model

enum class RolUsuario(val id: Long, val etiqueta: String) {
    USUARIO_FINAL(1, "Usuario final"),
    AGENTE(3, "Agente PagoFlex"),
    EJECUTIVO(2, "Ejecutivo de empresa");

    companion object {
        fun desdeId(id: Long): RolUsuario? = entries.firstOrNull { it.id == id }
    }
}
