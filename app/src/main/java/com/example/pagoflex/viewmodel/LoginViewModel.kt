package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.DerivadorDeRol
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.utils.Validaciones

class LoginViewModel : ViewModel() {

    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var contrasenaVisible by mutableStateOf(false)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set

    var rol by mutableStateOf<RolUsuario?>(null)
        private set

    fun cambiarCorreo(nuevo: String) { correo = nuevo; errorCorreo = null }
    fun cambiarContrasena(nueva: String) { contrasena = nueva; errorContrasena = null }
    fun alternarContrasenaVisible() { contrasenaVisible = !contrasenaVisible }

    fun iniciarSesion() {
        val correoLimpio = correo.trim()
        errorCorreo = if (Validaciones.correoValido(correoLimpio)) null else "Correo no válido"
        errorContrasena = if (Validaciones.contrasenaValida(contrasena)) null
        else "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres"
        if (errorCorreo != null || errorContrasena != null) return

        rol = DerivadorDeRol.rolPorCorreo(correoLimpio)
    }

    fun consumirRol() { rol = null }
}
