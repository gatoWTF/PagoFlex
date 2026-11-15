package com.example.pagoflex.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Before

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel


    @Before
    fun preparar(){
        viewModel = LoginViewModel()
    }

    @Test
    fun validar_conCamposVacios_mostrarErrores() {
        viewModel.cambiarCorreo("")
        viewModel.cambiarContrasena("")

        val resultado = viewModel.validar()

        assertFalse(resultado)
        assertEquals("Ingresa tu correo", viewModel.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.errorContrasena)
    }

    @Test
    fun validar_conCorreoSinFormato_noMostrarError() {
        viewModel.cambiarCorreo("benjamin")
        viewModel.cambiarContrasena("2026Pago")

        val resultado = viewModel.validar()

        assertTrue(resultado)
        assertNull(viewModel.errorCorreo)
    }

    @Test
    fun validar_sinContrasena_soloErrorDeContrasena() {
        viewModel.cambiarCorreo("benjamin")
        viewModel.cambiarContrasena("")

        val resultado = viewModel.validar()

        assertFalse(resultado)
        assertNull(viewModel.errorCorreo)
        assertEquals("Ingresa tu contraseña", viewModel.errorContrasena)
    }
}