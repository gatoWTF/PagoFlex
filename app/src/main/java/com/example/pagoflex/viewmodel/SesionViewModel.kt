package com.example.pagoflex.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.local.DatosSemilla
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.utils.Validaciones

class SesionViewModel : ViewModel() {

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

    fun iniciarDesdeRegistro(nombre: String, rut: String, correo: String, rol: RolUsuario) {
        this.nombre = nombre.trim()
        this.rut = rut.trim()
        this.correo = correo.trim()
        this.rol = rol
        this.telefono = ""
        this.correoSecundario = ""
        this.fotoPerfil = null
        limpiarErrores()
    }

    fun iniciarDesdeLogin(correo: String, rol: RolUsuario) {
        val limpio = correo.trim()
        val uf = DatosSemilla.usuariosFinales.firstOrNull { it.correo.equals(limpio, ignoreCase = true) }
        val ej = DatosSemilla.ejecutivos.firstOrNull { it.correo.equals(limpio, ignoreCase = true) }
        val ag = DatosSemilla.agentes.firstOrNull { it.correo.equals(limpio, ignoreCase = true) }
        this.correo = limpio
        this.rol = rol
        this.nombre = uf?.nombreCompleto ?: ej?.nombre ?: ag?.nombre ?: ""
        this.rut = uf?.rut ?: ej?.rut ?: ag?.rut ?: ""
        this.telefono = uf?.telefono ?: ej?.telefono ?: ""
        this.correoSecundario = ""
        this.fotoPerfil = null
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

    private fun limpiarErrores() {
        errorCorreoSecundario = null
        errorTelefono = null
        mensaje = null
    }
}
