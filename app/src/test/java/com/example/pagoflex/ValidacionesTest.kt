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

    // Pruebas del monto en pesos (RN-17).
    @Test
    fun montoEnteroPositivo_valido() {
        assertTrue(Validaciones.montoValido("15000"))
        assertTrue(Validaciones.montoValido("  86400 "))
    }

    @Test
    fun montoCeroNegativoONoNumerico_invalido() {
        assertFalse(Validaciones.montoValido("0"))
        assertFalse(Validaciones.montoValido("-100"))
        assertFalse(Validaciones.montoValido("1.500"))
        assertFalse(Validaciones.montoValido("abc"))
        assertFalse(Validaciones.montoValido(""))
    }

    // Pruebas de la fecha DD-MM-AAAA (RN-17).
    @Test
    fun fechaConFormatoYValoresValidos_valida() {
        assertTrue(Validaciones.fechaValida("25-10-2026"))
        assertTrue(Validaciones.fechaValida("29-02-2024")) // anio bisiesto
    }

    @Test
    fun fechaConFormatoOValoresInvalidos_invalida() {
        assertFalse(Validaciones.fechaValida("2026-10-25")) // orden incorrecto
        assertFalse(Validaciones.fechaValida("32-01-2026")) // dia fuera de rango
        assertFalse(Validaciones.fechaValida("10-13-2026")) // mes fuera de rango
        assertFalse(Validaciones.fechaValida("29-02-2025")) // no bisiesto
        assertFalse(Validaciones.fechaValida("5-6-2026"))   // sin dos digitos
    }
}
