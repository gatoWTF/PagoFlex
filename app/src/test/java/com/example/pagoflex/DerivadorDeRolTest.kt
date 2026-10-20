package com.example.pagoflex

import com.example.pagoflex.data.DerivadorDeRol
import com.example.pagoflex.model.RolUsuario
import org.junit.Assert.assertEquals
import org.junit.Test

class DerivadorDeRolTest {

    @Test
    fun correoDeClienteEsUsuarioFinal() {
        assertEquals(RolUsuario.USUARIO_FINAL, DerivadorDeRol.rolPorCorreo("crojas@correo.ejemplo"))
    }

    @Test
    fun correoDePagoFlexEsAgente() {
        assertEquals(RolUsuario.AGENTE, DerivadorDeRol.rolPorCorreo("alagos@pagoflex.ejemplo"))
    }

    @Test
    fun correoDeEmpresaEsEjecutivo() {
        assertEquals(RolUsuario.EJECUTIVO, DerivadorDeRol.rolPorCorreo("chenriquez@creditoandino.ejemplo"))
    }

    @Test
    fun noDistingueMayusculasNiEspacios() {
        assertEquals(RolUsuario.AGENTE, DerivadorDeRol.rolPorCorreo("  ALAGOS@PagoFlex.Ejemplo  "))
    }

    @Test
    fun correoSinArrobaCaeEnUsuarioFinal() {
        assertEquals(RolUsuario.USUARIO_FINAL, DerivadorDeRol.rolPorCorreo("textoSinArroba"))
    }

    @Test
    fun correoDeGmailEsUsuarioFinal() {
        assertEquals(RolUsuario.USUARIO_FINAL, DerivadorDeRol.rolPorCorreo("juan.perez@gmail.com"))
    }

    @Test
    fun correoDeHotmailEsUsuarioFinal() {
        assertEquals(RolUsuario.USUARIO_FINAL, DerivadorDeRol.rolPorCorreo("ana@hotmail.com"))
    }

    @Test
    fun correoDuocEsUsuarioFinal() {
        assertEquals(RolUsuario.USUARIO_FINAL, DerivadorDeRol.rolPorCorreo("f.armazan@duocuc.cl"))
    }
}
