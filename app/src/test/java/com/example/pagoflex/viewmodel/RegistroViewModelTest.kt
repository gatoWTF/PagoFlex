package com.example.pagoflex.viewmodel

import com.example.pagoflex.viewmodel.RegistroViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegistroViewModelTest {

    private lateinit var viewModel: RegistroViewModel

    @Before
    fun preparar() {
        viewModel = RegistroViewModel()
    }

    @Test
    fun validar_conFormularioVacio_devuelveFalseYPideLosDatos() {
        val resultado = viewModel.validar()

        assertFalse(resultado)
        assertEquals("Ingresa tu nombre", viewModel.errorNombre)
        assertEquals("Ingresa una contraseña", viewModel.errorContrasena)
    }

    @Test
    fun validar_conNombreConNumeros_muestraError() {
        viewModel.cambiarNombre("Camila 2")

        viewModel.validar()

        assertEquals("El nombre no puede llevar números", viewModel.errorNombre)
    }

    @Test
    fun validar_conRutConDigitoVerificadorMalo_muestraError() {
        viewModel.cambiarRut("15.834.207-0")

        viewModel.validar()

        assertEquals("RUT no válido", viewModel.errorRut)
    }

    @Test
    fun cambiarContrasena_sinMayuscula_muestraError() {
        viewModel.cambiarContrasena("clave123")

        assertEquals("Debe tener al menos una mayúscula", viewModel.errorContrasena)
    }

    @Test
    fun validar_conDatosCorrectos_devuelveTrueSinErrores() {
        viewModel.cambiarNombre("Camila Rojas")
        viewModel.cambiarRut("15.834.207-3")
        viewModel.cambiarCorreo("camila@correo.cl")
        viewModel.cambiarContrasena("Clave123")
        viewModel.cambiarConfirmar("Clave123")

        val resultado = viewModel.validar()

        assertTrue(resultado)
        assertNull(viewModel.errorNombre)
        assertNull(viewModel.errorRut)
        assertNull(viewModel.errorCorreo)
        assertNull(viewModel.errorContrasena)
        assertNull(viewModel.errorConfirmar)
    }
}
