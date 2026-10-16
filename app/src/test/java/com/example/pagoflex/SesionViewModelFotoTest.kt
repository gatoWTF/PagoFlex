package com.example.pagoflex

import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// Pruebas del estado de la foto de perfil en la sesion (recurso nativo: camara o
// galeria). No crean un Bitmap real (eso vive en el test instrumentado), sino que
// cubren el ciclo de vida del estado: arranca vacio y se limpia al iniciar sesion.
class SesionViewModelFotoTest {

    @Test
    fun fotoPerfil_arranca_null() {
        val sesion = SesionViewModel()
        assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarDesdeRegistro_dejaLaFotoEnNull() {
        val sesion = SesionViewModel()
        sesion.iniciarDesdeRegistro(
            nombre = "Ana Perez",
            rut = "15.834.207-3",
            correo = "ana@correo.cl",
            rol = RolUsuario.USUARIO_FINAL
        )
        assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarDesdeLogin_dejaLaFotoEnNull() {
        val sesion = SesionViewModel()
        sesion.iniciarDesdeLogin(correo = "alguien@correo.cl", rol = RolUsuario.USUARIO_FINAL)
        assertNull(sesion.fotoPerfil)
    }

    @Test
    fun consumirMensaje_limpiaElAviso() {
        val sesion = SesionViewModel()
        sesion.guardarContacto() // deja un mensaje de confirmacion
        assertEquals("Datos de contacto guardados", sesion.mensaje)
        sesion.consumirMensaje()
        assertNull(sesion.mensaje)
    }
}
