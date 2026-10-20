package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.DerivadorDeRol
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.utils.Validaciones

class RegistroViewModel : ViewModel() {

    var nombre by mutableStateOf("")
        private set
    var rut by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmar by mutableStateOf("")
        private set
    var contrasenaVisible by mutableStateOf(false)
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorRut by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmar by mutableStateOf<String?>(null)
        private set

    var rol by mutableStateOf<RolUsuario?>(null)
        private set

    fun cambiarNombre(nuevo: String) { nombre = nuevo; errorNombre = null }

    fun cambiarRut(nuevo: String) { rut = nuevo; errorRut = null }
    fun cambiarCorreo(nuevo: String) { correo = nuevo; errorCorreo = null }

    fun cambiarContrasena(nueva: String) {
        contrasena = nueva
        errorContrasena = evaluarContrasena()
        errorConfirmar = evaluarConfirmar()
    }

    fun cambiarConfirmar(nueva: String) {
        confirmar = nueva
        errorConfirmar = evaluarConfirmar()
    }

    fun alternarContrasenaVisible() { contrasenaVisible = !contrasenaVisible }

    fun validarRut() {
        errorRut = if (rut.isBlank() || Validaciones.rutValido(rut)) null else "RUT no válido"
    }

    fun validarCorreo() {
        errorCorreo = if (correo.isBlank() || Validaciones.correoValido(correo.trim())) null
        else "Correo no válido"
    }

    fun crearCuenta() {
        val correoLimpio = correo.trim()
        errorNombre = if (nombre.isBlank()) "Ingresa tu nombre" else null
        errorRut = if (Validaciones.rutValido(rut)) null else "RUT no válido"
        errorCorreo = if (Validaciones.correoValido(correoLimpio)) null else "Correo no válido"
        errorContrasena = evaluarContrasena() ?: contrasenaObligatoria()
        errorConfirmar = evaluarConfirmar() ?: confirmacionObligatoria()

        if (errorNombre != null || errorRut != null || errorCorreo != null ||
            errorContrasena != null || errorConfirmar != null
        ) return

        rol = DerivadorDeRol.rolPorCorreo(correoLimpio)
    }

    fun consumirRol() { rol = null }

    private fun evaluarContrasena(): String? = when {
        contrasena.isEmpty() -> null
        !Validaciones.contrasenaValida(contrasena) ->
            "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres"
        else -> null
    }

    private fun evaluarConfirmar(): String? = when {
        confirmar.isEmpty() -> null
        confirmar != contrasena -> "Las contraseñas no coinciden"
        else -> null
    }

    private fun contrasenaObligatoria(): String? =
        if (contrasena.isEmpty()) "Ingresa una contraseña" else null

    private fun confirmacionObligatoria(): String? =
        if (confirmar.isEmpty()) "Repite la contraseña" else null
}
