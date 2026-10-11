package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.local.DatosSemilla
import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.utils.Validaciones

/**
 * Portador de la sesion simulada (RF-01). Guarda los datos de la persona que
 * inicio sesion o se registro para mostrarlos en el Perfil. Vive con alcance de
 * Activity, por eso cualquier pantalla puede leer el usuario actual.
 *
 * Identidad (nombre, RUT, correo, rol): solo lectura.
 * Contacto (correo secundario y telefono): editable por la persona, con validacion.
 */
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

    var errorCorreoSecundario by mutableStateOf<String?>(null)
        private set
    var errorTelefono by mutableStateOf<String?>(null)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    // Alta desde el registro: tenemos todos los datos que escribio la persona.
    fun iniciarDesdeRegistro(nombre: String, rut: String, correo: String, rol: RolUsuario) {
        this.nombre = nombre.trim()
        this.rut = rut.trim()
        this.correo = correo.trim()
        this.rol = rol
        this.telefono = ""
        this.correoSecundario = ""
        limpiarErrores()
    }

    // Alta desde el login: solo tenemos correo y rol. Si el correo calza con un
    // usuario de ejemplo (Anexo 9), completamos nombre, RUT y telefono.
    fun iniciarDesdeLogin(correo: String, rol: RolUsuario) {
        val limpio = correo.trim()
        val semilla = DatosSemilla.usuariosFinales
            .firstOrNull { it.correo.equals(limpio, ignoreCase = true) }
        this.correo = limpio
        this.rol = rol
        this.nombre = semilla?.nombreCompleto.orEmpty()
        this.rut = semilla?.rut.orEmpty()
        this.telefono = semilla?.telefono.orEmpty()
        this.correoSecundario = ""
        limpiarErrores()
    }

    fun cambiarCorreoSecundario(nuevo: String) {
        correoSecundario = nuevo
        errorCorreoSecundario = null
    }

    fun cambiarTelefono(nuevo: String) {
        telefono = nuevo
        errorTelefono = null
    }

    // Guarda el contacto si es valido. El correo secundario y el telefono son
    // opcionales: vacios se aceptan; si traen algo, deben tener formato valido.
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
