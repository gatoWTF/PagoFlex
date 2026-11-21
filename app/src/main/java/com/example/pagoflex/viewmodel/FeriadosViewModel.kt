package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.FeriadoRepository
import com.example.pagoflex.model.Feriado
import kotlinx.coroutines.launch

class FeriadosViewModel(
    private val repositorio: FeriadoRepository = FeriadoRepository()
) : ViewModel() {

    var feriados by mutableStateOf<List<Feriado>>(emptyList())
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private val aniosCargados = mutableSetOf<Int>()

    fun cargar(anio: Int) {
        if (!aniosCargados.add(anio)) return
        viewModelScope.launch {
            try {
                feriados = feriados + repositorio.feriadosDelAnio(anio)
                error = null
            } catch (e: Exception) {
                aniosCargados.remove(anio)
                error = e.message
            }
        }
    }
}
