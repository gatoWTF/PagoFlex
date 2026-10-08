package com.example.pagoflex

import com.example.pagoflex.utils.Validaciones
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Pruebas de la validacion de RUT por modulo 11 (RN-18).
class ValidacionesTest {

    @Test
    fun rutConFormato_valido() {
        assertTrue(Validaciones.rutValido("15.834.207-3"))
        assertTrue(Validaciones.rutValido("76.845.213-K"))
    }

    @Test
    fun rutSinFormato_valido() {
        assertTrue(Validaciones.rutValido("158342073"))
    }

    @Test
    fun rutConDigitoIncorrecto_invalido() {
        assertFalse(Validaciones.rutValido("15.834.207-4"))
    }

    @Test
    fun rutVacioOMuyCorto_invalido() {
        assertFalse(Validaciones.rutValido(""))
        assertFalse(Validaciones.rutValido("1"))
    }
}
