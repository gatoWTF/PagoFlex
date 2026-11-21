package com.example.pagoflex.viewmodel

import com.example.pagoflex.model.RolUsuario
import com.example.pagoflex.model.Usuario
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SesionViewModelTest {

    private val cuentaDePrueba = Usuario(
        id = 7L,
        nombre = "Ana Perez",
        rut = "15.834.207-3",
        correo = "ana@correo.cl",
        rol = RolUsuario.USUARIO_FINAL
    )

    private lateinit var editarPerfil: EditarPerfilViewModel

    @Before
    fun preparar() {
        editarPerfil = EditarPerfilViewModel()
    }

    @Test
    fun fotoPerfil_arranca_null() {
        val sesion = SesionViewModel()
        Assert.assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarSesion_dejaLaFotoEnNull() {
        val sesion = SesionViewModel()
        sesion.iniciarSesion(cuentaDePrueba)
        Assert.assertNull(sesion.fotoPerfil)
    }

    @Test
    fun iniciarSesion_guardaLosDatosDeLaCuenta() {
        val sesion = SesionViewModel()
        sesion.iniciarSesion(cuentaDePrueba)
        Assert.assertEquals(7L, sesion.idUsuario)
        Assert.assertEquals("Ana Perez", sesion.nombre)
        Assert.assertEquals(RolUsuario.USUARIO_FINAL, sesion.rol)
    }

    @Test
    fun consumirMensaje_limpiaElAviso() {
        val sesion = SesionViewModel()
        sesion.actualizarDatos(cuentaDePrueba)
        Assert.assertEquals("Datos actualizados", sesion.mensaje)
        sesion.consumirMensaje()
        Assert.assertNull(sesion.mensaje)
    }

    @Test
    fun validarContacto_conTelefonoInvalido_mostrarError() {
        val sesion = SesionViewModel()
        sesion.cambiarTelefono("12ab")
        val valido = sesion.validarContacto()
        Assert.assertFalse(valido)
        Assert.assertEquals("Teléfono no válido", sesion.errorTelefono)
    }

    @Test
    fun eliminarCuenta_sinSesion_noHaceNada() {
        val sesion = SesionViewModel()
        sesion.eliminarCuenta()
        Assert.assertFalse(sesion.eliminandoCuenta)
        Assert.assertFalse(sesion.cuentaEliminada)
    }

    @Test
    fun editarPerfil_conNombreConNumeros_mostrarError() {
        editarPerfil.cambiarNombre("benjamin 123")
        editarPerfil.cambiarCorreo("benjamin@correo.cl")

        val resultado = editarPerfil.validar()

        Assert.assertFalse(resultado)
        Assert.assertEquals("El nombre no puede llevar números", editarPerfil.errorNombre)
    }

    @Test
    fun editarPerfil_conCorreoInvalido_mostrarError() {
        editarPerfil.cambiarNombre("Benjamin Soto")
        editarPerfil.cambiarCorreo("benjamin")

        val resultado = editarPerfil.validar()

        Assert.assertFalse(resultado)
        Assert.assertEquals("Correo no válido", editarPerfil.errorCorreo)
    }
}