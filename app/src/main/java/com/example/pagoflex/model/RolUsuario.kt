package com.example.pagoflex.model

// Los tres roles de la app, con sesion simulada (RF-01, Anexo 2).
// El nombre de la constante va sin tildes; la etiqueta es el texto visible al usuario.
enum class RolUsuario(val etiqueta: String) {
    USUARIO_FINAL("Usuario final"),
    AGENTE("Agente PagoFlex"),
    EJECUTIVO("Ejecutivo de empresa")
}
