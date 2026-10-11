package com.example.pagoflex.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.pagoflex.model.ModoTema

// Preferencias del usuario (apariencia y avisos). Se guardan en SharedPreferences,
// asi persisten al cerrar y abrir la app.
class ConfiguracionViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs =
        app.getSharedPreferences("pagoflex_prefs", Context.MODE_PRIVATE)

    var modoTema by mutableStateOf(leerModoTema())
        private set
    var avisosActivos by mutableStateOf(prefs.getBoolean(CLAVE_AVISOS, true))
        private set
    var diasAviso by mutableIntStateOf(prefs.getInt(CLAVE_DIAS, 3))
        private set

    fun cambiarModoTema(modo: ModoTema) {
        modoTema = modo
        prefs.edit().putString(CLAVE_TEMA, modo.name).apply()
    }

    fun cambiarAvisos(activo: Boolean) {
        avisosActivos = activo
        prefs.edit().putBoolean(CLAVE_AVISOS, activo).apply()
    }

    fun cambiarDiasAviso(dias: Int) {
        diasAviso = dias
        prefs.edit().putInt(CLAVE_DIAS, dias).apply()
    }

    private fun leerModoTema(): ModoTema {
        val guardado = prefs.getString(CLAVE_TEMA, ModoTema.SISTEMA.name)
        return runCatching { ModoTema.valueOf(guardado!!) }.getOrDefault(ModoTema.SISTEMA)
    }

    companion object {
        private const val CLAVE_TEMA = "modo_tema"
        private const val CLAVE_AVISOS = "avisos_activos"
        private const val CLAVE_DIAS = "dias_aviso"
    }
}
