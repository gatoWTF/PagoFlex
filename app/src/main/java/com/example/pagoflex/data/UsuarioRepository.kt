package com.example.pagoflex.data

import com.example.pagoflex.model.Usuario

class UsuarioRepository {
    private val usuarioPrueba = Usuario(nombre = "Usuario", correo = "usuario@correo.cl")
    private val contrasenaPrueba = "123456"

    // Devuelve el usuario si los datos coinciden, o null si no
    fun login(correo: String, contrasena: String): Usuario? =
        if (correo == usuarioPrueba.correo && contrasena == contrasenaPrueba) usuarioPrueba else null
}
