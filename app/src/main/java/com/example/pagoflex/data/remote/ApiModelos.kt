package com.example.pagoflex.data.remote

import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.model.Usuario

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class RegistroRequest(
    val nombre: String,
    val rut: String,
    val correo: String,
    val contrasena: String
)

data class ActualizarUsuarioRequest(
    val nombre: String,
    val rut: String,
    val correo: String
)

data class CambiarContrasenaRequest(
    val actual: String,
    val nueva: String
)

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val rut: String,
    val correo: String,
    val rolId: Long,
    val rolNombre: String
)

data class ErrorApi(
    val message: String?
)

fun UsuarioResponse.aUsuario(): Usuario {
    val rol = RolUsuario.desdeId(rolId)
        ?: throw IllegalStateException("La cuenta tiene un rol desconocido")
    return Usuario(id = id, nombre = nombre, rut = rut, correo = correo, rol = rol)
}
