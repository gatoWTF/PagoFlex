package com.example.pagoflex.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.UsuarioRepository
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.model.Usuario
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var idUsuario by mutableStateOf<Long?>(null)
        private set
    var nombre by mutableStateOf("")
        private set
    var rut by mutableStateOf("")
        private set
    var correo by mutableStateOf("")
        private set
    var rol by mutableStateOf<RolUsuario?>(null)
        private set

    var correoSecundario by mutableStateOf("")
        private set
    var telefono by mutableStateOf("")
        private set

    var fotoPerfil by mutableStateOf<Bitmap?>(null)
        private set

    var errorCorreoSecundario by mutableStateOf<String?>(null)
        private set
    var errorTelefono by mutableStateOf<String?>(null)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set
    var mensajeEsError by mutableStateOf(false)
        private set
    var guardandoContacto by mutableStateOf(false)
        private set

    var bienvenida by mutableStateOf<String?>(null)
        private set

    var eliminandoCuenta by mutableStateOf(false)
        private set
    var cuentaEliminada by mutableStateOf(false)
        private set

    fun iniciarSesion(usuario: Usuario) {
        this.idUsuario = usuario.id
        this.nombre = usuario.nombre
        this.rut = usuario.rut
        this.correo = usuario.correo
        this.rol = usuario.rol
        this.telefono = usuario.telefono
        this.correoSecundario = usuario.correoSecundario
        this.fotoPerfil = null
        this.bienvenida = "Ingresaste como ${usuario.rol.etiqueta}"
        limpiarErrores()
    }

    fun cambiarFoto(bitmap: Bitmap) {
        fotoPerfil = bitmap
        mensaje = "Foto de perfil actualizada"
    }

    fun mostrarFotoGuardada(bitmap: Bitmap) {
        fotoPerfil = bitmap
    }

    fun cambiarCorreoSecundario(nuevo: String) {
        correoSecundario = nuevo
        errorCorreoSecundario = null
    }

    fun cambiarTelefono(nuevo: String) {
        telefono = nuevo
        errorTelefono = null
    }

    fun actualizarDatos(usuario: Usuario) {
        nombre = usuario.nombre
        rut = usuario.rut
        correo = usuario.correo
        mensaje = "Datos actualizados"
    }

    fun validarContacto(): Boolean {
        val correo2 = correoSecundario.trim()
        errorCorreoSecundario =
            if (correo2.isEmpty() || Validaciones.correoValido(correo2)) null
            else "Correo no válido"
        errorTelefono =
            if (telefono.isBlank() || Validaciones.telefonoValido(telefono)) null
            else "Teléfono no válido"
        return errorCorreoSecundario == null && errorTelefono == null
    }

    fun guardarContacto() {
        if (!validarContacto()) return
        val id = idUsuario ?: return
        guardandoContacto = true
        viewModelScope.launch {
            try {
                val usuario = repositorio.guardarContacto(id, correoSecundario.trim(), telefono.trim())
                correoSecundario = usuario.correoSecundario
                telefono = usuario.telefono
                mensaje = "Datos de contacto guardados"
            } catch (e: Exception) {
                mensajeEsError = true
                mensaje = e.message
            } finally {
                guardandoContacto = false
            }
        }
    }

    fun eliminarCuenta() {
        val id = idUsuario ?: return
        if (eliminandoCuenta) return
        eliminandoCuenta = true
        viewModelScope.launch {
            try {
                repositorio.eliminar(id)
                cuentaEliminada = true
            } catch (e: Exception) {
                mensajeEsError = true
                mensaje = e.message
            } finally {
                eliminandoCuenta = false
            }
        }
    }

    fun consumirCuentaEliminada() { cuentaEliminada = false }

    fun consumirMensaje() {
        mensaje = null
        mensajeEsError = false
    }

    fun consumirBienvenida() { bienvenida = null }
    private fun limpiarErrores() {
        errorCorreoSecundario = null
        errorTelefono = null
        mensaje = null
        mensajeEsError = false
    }
}
