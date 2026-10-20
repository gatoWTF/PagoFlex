package com.example.pagoflex

import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

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
        sesion.guardarContacto()
        assertEquals("Datos de contacto guardados", sesion.mensaje)
        sesion.consumirMensaje()
        assertNull(sesion.mensaje)
    }
}
