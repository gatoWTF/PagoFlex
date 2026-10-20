package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.PagoRepository
import com.example.pagoflex.model.EstadoPago
import com.example.pagoflex.model.Pago

class PagosViewModel(
    private val repo: PagoRepository = PagoRepository()
) : ViewModel() {

    var pagos by mutableStateOf(repo.obtenerPagos())
        private set

    val pendientes: List<Pago>
        get() = pagos.filter { it.estado != EstadoPago.PAGADO }

    val totalPendiente: Int
        get() = pendientes.sumOf { it.monto }

    fun buscarPorId(id: Int): Pago? = pagos.find { it.id == id }

    fun pagar(id: Int) {
        repo.marcarComoPagado(id)
        pagos = repo.obtenerPagos()
    }
}
