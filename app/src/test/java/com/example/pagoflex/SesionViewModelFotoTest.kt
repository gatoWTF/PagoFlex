package com.example.pagoflex

import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.model.Usuario
import com.example.pagoflex.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SesionViewModelFotoTest {

    private val cuentaDePrueba = Usuario(
        id = 7L,
        nombre = "Ana Perez",
        rut = "15.834.207-3",
        correo = "ana@correo.cl",
        rol = RolUsuario.USUARIO_FINAL
    )

    @Test
    fun fotoPerfil_arranca_null() {
        val sesion = SesionViewModel()
        assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarSesion_dejaLaFotoEnNull() {
        val sesion = SesionViewModel()
        sesion.iniciarSesion(cuentaDePrueba)
        assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarSesion_guardaLosDatosDeLaCuenta() {
        val sesion = SesionViewModel()
        sesion.iniciarSesion(cuentaDePrueba)
        assertEquals(7L, sesion.idUsuario)
        assertEquals("Ana Perez", sesion.nombre)
        assertEquals(RolUsuario.USUARIO_FINAL, sesion.rol)
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
