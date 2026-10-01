package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.PagoRepository
import com.example.pagoflex.model.Pago

class PagosViewModel(
    private val repo: PagoRepository = PagoRepository()
) : ViewModel() {

    // Empieza vacía. Cuando agreguemos las funciones del repositorio, se llena con sus pagos.
    var pagos by mutableStateOf(emptyList<Pago>())
        private set
}