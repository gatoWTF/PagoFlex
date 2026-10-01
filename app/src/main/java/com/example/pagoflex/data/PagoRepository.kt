package com.example.pagoflex.data

import com.example.pagoflex.model.CategoriaPago
import com.example.pagoflex.model.EstadoPago
import com.example.pagoflex.model.Pago

class PagoRepository {
    private val pagos = mutableListOf(
        Pago(1, "Cuenta de luz", "Enel", CategoriaPago.LUZ, 25990, "05/10/2026", EstadoPago.PENDIENTE),
        Pago(2, "Cuenta de agua", "Aguas Andinas", CategoriaPago.AGUA, 14990, "08/10/2026", EstadoPago.PENDIENTE),
        Pago(3, "Internet hogar", "Movistar", CategoriaPago.INTERNET, 26990, "12/10/2026", EstadoPago.PENDIENTE),
        Pago(4, "Luz septiembre", "Enel", CategoriaPago.LUZ, 24500, "04/09/2026", EstadoPago.PAGADO),
        Pago(5, "Agua septiembre", "Aguas Andinas", CategoriaPago.AGUA, 13200, "07/09/2026", EstadoPago.PAGADO),
        Pago(6, "Gas agosto", "Metrogas", CategoriaPago.GAS, 18000, "20/08/2026", EstadoPago.VENCIDO)
    )

    // toList() entrega una copia, así la pantalla detecta que la lista cambió
    fun obtenerPagos(): List<Pago> = pagos.toList()

    fun obtenerPorId(id: Int): Pago? = pagos.find { it.id == id }

    fun marcarComoPagado(id: Int) {
        val indice = pagos.indexOfFirst { it.id == id }
        if (indice != -1) {
            // copy() crea un Pago igual pero con el estado cambiado
            pagos[indice] = pagos[indice].copy(estado = EstadoPago.PAGADO)
        }
    }
}
