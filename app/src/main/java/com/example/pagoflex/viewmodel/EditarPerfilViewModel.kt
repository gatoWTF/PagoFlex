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



class EditarPerfilViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var nombre by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorGeneral by mutableStateOf<String?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set
    var usuarioActualizado by mutableStateOf<Usuario?>(null)
        private set

    fun cargar(nombreActual: String, correoActual: String) {
        nombre = nombreActual
        correo = correoActual
    }

    fun cambiarNombre(nuevo: String) { nombre = nuevo; errorNombre = null }

    fun cambiarCorreo(nuevo: String) { correo = nuevo; errorCorreo = null; errorGeneral = null }
    fun validar(): Boolean {
        errorNombre = when {
            nombre.isBlank() -> "Ingresa tu nombre"
            !Validaciones.nombreLargo(nombre) -> "El nombre es muy corto"
            !Validaciones.nombreSoloLetras(nombre) -> "El nombre no puede llevar números"
            else -> null
        }
        errorCorreo = if (Validaciones.correoValido(correo.trim())) null else "Correo no válido"
        return errorNombre == null && errorCorreo == null
    }

    fun guardar(idUsuario: Long?, rut: String) {
        if (idUsuario == null || cargando || !validar()) return
        cargando = true
        errorGeneral = null
        viewModelScope.launch {
            try {
                usuarioActualizado = repositorio.actualizar(idUsuario, nombre.trim(), rut, correo.trim())
            } catch (e: Exception) {
                errorGeneral = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun consumirUsuario() { usuarioActualizado = null }
}