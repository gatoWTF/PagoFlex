package com.example.pagoflex.viewmodel

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse


class EditarPerfilViewModelTest {

    private lateinit var viewModel: EditarPerfilViewModel


    @Before
    fun preparar(){
        viewModel = EditarPerfilViewModel()
    }

    @Test
    fun validar_conNombreConNumeros_mostrarError(){
        viewModel.cambiarNombre("benjamin 123")
        viewModel.cambiarCorreo("benjamin@correo.cl")

        val resultado = viewModel.validar()

        assertFalse(resultado)
        assertEquals("El nombre no puede llevar números", viewModel.errorNombre)
    }

    @Test
    fun validar_conCorreoInvalido_mostrarError(){
        viewModel.cambiarNombre("Benjamin Soto")
        viewModel.cambiarCorreo("benjamin")

        val resultado = viewModel.validar()

        assertFalse(resultado)
        assertEquals("Correo no válido", viewModel.errorCorreo)
    }
}