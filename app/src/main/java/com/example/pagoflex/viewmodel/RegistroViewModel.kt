package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.UsuarioRepository
import com.example.pagoflex.model.Usuario
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

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
    var errorGeneral by mutableStateOf<String?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun cambiarNombre(nuevo: String) { nombre = nuevo; errorNombre = null }

    fun cambiarRut(nuevo: String) { rut = nuevo; errorRut = null }
    fun cambiarCorreo(nuevo: String) { correo = nuevo; errorCorreo = null; errorGeneral = null }

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

    fun validar(): Boolean {
        errorNombre = when {
            nombre.isBlank() -> "Ingresa tu nombre"
            !Validaciones.nombreLargo(nombre) -> "El nombre es muy corto"
            !Validaciones.nombreSoloLetras(nombre) -> "El nombre no puede llevar números"
            else -> null}
        errorRut = if (Validaciones.rutValido(rut)) null else "RUT no válido"
        errorCorreo = if (Validaciones.correoValido(correo.trim())) null else "Correo no válido"
        errorContrasena = evaluarContrasena() ?: contrasenaObligatoria()
        errorConfirmar = evaluarConfirmar() ?: confirmacionObligatoria()

        return errorNombre == null && errorRut == null && errorCorreo == null &&
            errorContrasena == null && errorConfirmar == null
    }

    fun crearCuenta() {
        if (cargando || !validar()) return
        cargando = true
        errorGeneral = null
        viewModelScope.launch {
            try {
                usuario = repositorio.registrar(nombre.trim(), rut.trim(), correo.trim(), contrasena)
            } catch (e: Exception) {
                errorGeneral = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun consumirUsuario() { usuario = null }

    private fun evaluarContrasena(): String? = when {
        contrasena.isEmpty() -> null
        !Validaciones.contrasenaValida(contrasena) ->
            "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres, una mayúscula, una minúscula y un número"
        !Validaciones.tieneMayuscula(contrasena) ->
            "Debe tener al menos una mayúscula"
        !Validaciones.tieneMinuscula(contrasena) ->
            "Debe tener al menos una minúscula"
        !Validaciones.tieneNumero(contrasena) ->
            "Debe tener al menos un número"
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
