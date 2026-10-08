package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.MemoriaDatos
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso

// Estado y logica de los compromisos del usuario final.
// Por ahora usa datos en memoria; la lectura desde Room se integra al final.
class CompromisosViewModel : ViewModel() {

    var compromisos by mutableStateOf(MemoriaDatos.compromisosDeEjemplo)
        private set

    // Lo que falta pagar: pendientes y vencidos (RF-02)
    val porPagar: List<Compromiso>
        get() = compromisos.filter { it.estaPorPagar }

    val totalPorPagar: Int
        get() = porPagar.sumOf { it.totalAPagar }

    val cantidadVencidos: Int
        get() = compromisos.count { it.estado == EstadoCompromiso.VENCIDO }

    // Fecha del compromiso por pagar que vence primero (RF-02)
    val proximoVencimiento: String?
        get() = porPagar.minByOrNull { aOrden(it.fechaVencimiento) }?.fechaVencimiento

    fun buscarPorFolio(folio: String): Compromiso? = compromisos.find { it.folio == folio }

    // Pago simulado: el compromiso pasa a Pagado (RF-05, RN-05)
    fun pagar(folio: String) {
        compromisos = compromisos.map { compromiso ->
            if (compromiso.folio == folio) {
                compromiso.copy(estado = EstadoCompromiso.PAGADO, recargo = 0)
            } else {
                compromiso
            }
        }
    }

    // Convierte DD-MM-AAAA en un numero AAAAMMDD para poder ordenar por fecha.
    private fun aOrden(fecha: String): Int {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0
        return partes[2].toInt() * 10000 + partes[1].toInt() * 100 + partes[0].toInt()
    }
}
