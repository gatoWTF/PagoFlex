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

class LoginViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

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
    var errorGeneral by mutableStateOf<String?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    fun cambiarCorreo(nuevo: String) { correo = nuevo; errorCorreo = null; errorGeneral = null }
    fun cambiarContrasena(nueva: String) { contrasena = nueva; errorContrasena = null; errorGeneral = null }
    fun alternarContrasenaVisible() { contrasenaVisible = !contrasenaVisible }

    fun validar(): Boolean {
        errorCorreo = if (Validaciones.correoValido(correo.trim())) null else "Correo no válido"
        errorContrasena = if (Validaciones.contrasenaValida(contrasena)) null
        else "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres"
        return errorCorreo == null && errorContrasena == null
    }

    fun iniciarSesion() {
        if (cargando || !validar()) return
        cargando = true
        errorGeneral = null
        viewModelScope.launch {
            try {
                usuario = repositorio.iniciarSesion(correo.trim(), contrasena)
            } catch (e: Exception) {
                errorGeneral = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun consumirUsuario() { usuario = null }
}
