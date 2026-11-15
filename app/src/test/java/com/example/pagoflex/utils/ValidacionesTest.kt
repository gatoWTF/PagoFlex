package com.example.pagoflex.utils

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class ValidacionesTest {

    @Test
    fun rutConFormato_valido() {
        Assert.assertTrue(Validaciones.rutValido("15.834.207-3"))
        Assert.assertTrue(Validaciones.rutValido("76.845.213-K"))
    }

    @Test
    fun rutSinFormato_valido() {
        Assert.assertTrue(Validaciones.rutValido("158342073"))
    }

    @Test
    fun rutConDigitoIncorrecto_invalido() {
        Assert.assertFalse(Validaciones.rutValido("15.834.207-4"))
    }

    @Test
    fun rutVacioOMuyCorto_invalido() {
        Assert.assertFalse(Validaciones.rutValido(""))
        Assert.assertFalse(Validaciones.rutValido("1"))
    }

    @Test
    fun montoEnteroPositivo_valido() {
        Assert.assertTrue(Validaciones.montoValido("15000"))
        Assert.assertTrue(Validaciones.montoValido("  86400 "))
    }

    @Test
    fun montoCeroNegativoONoNumerico_invalido() {
        Assert.assertFalse(Validaciones.montoValido("0"))
        Assert.assertFalse(Validaciones.montoValido("-100"))
        Assert.assertFalse(Validaciones.montoValido("1.500"))
        Assert.assertFalse(Validaciones.montoValido("abc"))
        Assert.assertFalse(Validaciones.montoValido(""))
    }

    @Test
    fun fechaConFormatoYValoresValidos_valida() {
        Assert.assertTrue(Validaciones.fechaValida("25-10-2026"))
        Assert.assertTrue(Validaciones.fechaValida("29-02-2024"))
    }

    @Test
    fun fechaConFormatoOValoresInvalidos_invalida() {
        Assert.assertFalse(Validaciones.fechaValida("2026-10-25"))
        Assert.assertFalse(Validaciones.fechaValida("32-01-2026"))
        Assert.assertFalse(Validaciones.fechaValida("10-13-2026"))
        Assert.assertFalse(Validaciones.fechaValida("29-02-2025"))
        Assert.assertFalse(Validaciones.fechaValida("5-6-2026"))
    }

    private val hoy = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse("14-11-2026")!!

    @Test
    fun fechaDeHoyOFutura_noEsAnterior() {
        Assert.assertTrue(Validaciones.fechaNoAnteriorAHoy("14-11-2026", hoy))
        Assert.assertTrue(Validaciones.fechaNoAnteriorAHoy("01-01-2027", hoy))
    }

    @Test
    fun fechaPasada_esAnterior() {
        Assert.assertFalse(Validaciones.fechaNoAnteriorAHoy("13-11-2026", hoy))
        Assert.assertFalse(Validaciones.fechaNoAnteriorAHoy("25-12-2025", hoy))
    }
}