package com.example.pagoflex

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pagoflex.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SesionFotoInstrumentedTest {

    @Test
    fun cambiarFoto_guardaElBitmapYAvisa() {
        val sesion = SesionViewModel()
        val imagen = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)

        sesion.cambiarFoto(imagen)

        assertNotNull(sesion.fotoPerfil)
        assertSame(imagen, sesion.fotoPerfil)
        assertEquals("Foto de perfil actualizada", sesion.mensaje)
    }
}
