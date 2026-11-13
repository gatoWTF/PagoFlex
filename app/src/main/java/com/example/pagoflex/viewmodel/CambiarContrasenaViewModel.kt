package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.UsuarioRepository
import com.example.pagoflex.utils.Validaciones
import kotlinx.coroutines.launch

class CambiarContrasenaViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var actual by mutableStateOf("")
        private set
    var nueva by mutableStateOf("")
        private set
    var confirmar by mutableStateOf("")
        private set
    var visible by mutableStateOf(false)
        private set

    var errorActual by mutableStateOf<String?>(null)
        private set
    var errorNueva by mutableStateOf<String?>(null)
        private set
    var errorConfirmar by mutableStateOf<String?>(null)
        private set
    var errorGeneral by mutableStateOf<String?>(null)
        private set
    var cargando by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    fun cambiarActual(valor: String) { actual = valor; errorActual = null; errorGeneral = null }
    fun cambiarNueva(valor: String) { nueva = valor; errorNueva = null }
    fun cambiarConfirmar(valor: String) { confirmar = valor; errorConfirmar = null }
    fun alternarVisible() { visible = !visible }

    fun validar(): Boolean {
        errorActual = if (actual.isBlank()) "Ingresa tu contraseña actual" else null
        errorNueva = when {
            !Validaciones.contrasenaValida(nueva) || !Validaciones.tieneMayuscula(nueva) ||
                    !Validaciones.tieneMinuscula(nueva) || !Validaciones.tieneNumero(nueva) ->
                "La contraseña debe tener al menos ${Validaciones.LARGO_MINIMO_CONTRASENA} caracteres, una mayúscula, una minúscula y un número"
            nueva == actual -> "Debe ser distinta a la actual"
            else -> null
        }
        errorConfirmar = if (confirmar != nueva) "Las contraseñas no coinciden" else null
        return errorActual == null && errorNueva == null && errorConfirmar == null
    }

    fun guardar(idUsuario: Long?) {
        if (idUsuario == null || cargando || !validar()) return
        cargando = true
        errorGeneral = null
        viewModelScope.launch {
            try {
                repositorio.cambiarContrasena(idUsuario, actual, nueva)
                actual = ""
                nueva = ""
                confirmar = ""
                mensaje = "Contraseña actualizada"
            } catch (e: Exception) {
                errorGeneral = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun consumirMensaje() { mensaje = null }
}