package com.example.pagoflex.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.model.Usuario
import com.example.pagoflex.utils.Validaciones

class SesionViewModel : ViewModel() {

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

    var bienvenida by mutableStateOf<String?>(null)
        private set

    fun iniciarSesion(usuario: Usuario) {
        this.idUsuario = usuario.id
        this.nombre = usuario.nombre
        this.rut = usuario.rut
        this.correo = usuario.correo
        this.rol = usuario.rol
        this.telefono = ""
        this.correoSecundario = ""
        this.fotoPerfil = null
        this.bienvenida = "Ingresaste como ${usuario.rol.etiqueta}"
        limpiarErrores()
    }

    fun cambiarFoto(bitmap: Bitmap) {
        fotoPerfil = bitmap
        mensaje = "Foto de perfil actualizada"
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

    fun guardarContacto() {
        val correo2 = correoSecundario.trim()
        errorCorreoSecundario =
            if (correo2.isEmpty() || Validaciones.correoValido(correo2)) null
            else "Correo no válido"
        errorTelefono =
            if (telefono.isBlank() || Validaciones.telefonoValido(telefono)) null
            else "Teléfono no válido"

        if (errorCorreoSecundario != null || errorTelefono != null) return
        mensaje = "Datos de contacto guardados"
    }

    fun consumirMensaje() { mensaje = null }

    fun consumirBienvenida() { bienvenida = null }
    private fun limpiarErrores() {
        errorCorreoSecundario = null
        errorTelefono = null
        mensaje = null
    }
}
