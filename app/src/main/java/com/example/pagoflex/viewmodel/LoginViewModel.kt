package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.UsuarioRepository
import com.example.pagoflex.model.Usuario
import com.example.pagoflex.utils.Validaciones

class LoginViewModel(
    private val repo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var contrasenaVisible by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun cambiarCorreo(nuevo: String) {
        correo = nuevo
        mensajeError = null
    }

    fun cambiarContrasena(nueva: String) {
        contrasena = nueva
        mensajeError = null
    }

    fun alternarContrasenaVisible() {
        contrasenaVisible = !contrasenaVisible
    }

    // Devuelve true si el login fue correcto (para saber si navegar a Inicio)
    fun iniciarSesion(): Boolean {
        val correoLimpio = correo.trim()   // el teclado a veces deja un espacio al final
        if (!Validaciones.correoValido(correoLimpio)) {
            mensajeError = "Correo no válido"
            return false
        }
        if (!Validaciones.contrasenaValida(contrasena)) {
            mensajeError = "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres"
            return false
        }
        usuario = repo.login(correoLimpio, contrasena)
        if (usuario == null) {
            mensajeError = "Correo o contraseña incorrectos"
        }
        return usuario != null
    }

    fun cerrarSesion() {
        usuario = null
        correo = ""
        contrasena = ""
        contrasenaVisible = false
        mensajeError = null
    }
}
