package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.MemoriaDatos
import com.example.pagoflex.model.CompromisoEmpresa
import com.example.pagoflex.model.EstadoCompromiso
import java.util.Locale

// Estado y logica del ejecutivo de empresa cliente (R-03): registra y anula
// compromisos de su empresa y ve su cobranza del mes (RF-17 a RF-20).
// Por ahora usa datos en memoria; la lectura desde Room se integra al final.
class EjecutivoViewModel : ViewModel() {

    val empresa = "Crédito Andino"

    var compromisos by mutableStateOf(MemoriaDatos.compromisosEmpresaDeEjemplo)
        private set

    // Mensaje puntual para mostrar en una Snackbar; la pantalla lo consume y lo limpia.
    var mensaje by mutableStateOf<String?>(null)
        private set

    fun consumirMensaje() { mensaje = null }

    // Numero correlativo para el folio del proximo compromiso registrado.
    private var contadorFolio = 130

    // Compromisos que siguen por cobrar (no cuentan los anulados).
    private val porCobrar: List<CompromisoEmpresa>
        get() = compromisos.filter {
            it.estado == EstadoCompromiso.PENDIENTE || it.estado == EstadoCompromiso.VENCIDO
        }

    private val pagados: List<CompromisoEmpresa>
        get() = compromisos.filter { it.estado == EstadoCompromiso.PAGADO }

    val totalRecaudado: Int get() = pagados.sumOf { it.monto }
    val totalPorCobrar: Int get() = porCobrar.sumOf { it.monto }

    // Tasa de cobranza del mes: % pagado sobre lo que debia cobrarse (RF-20, RN-24).
    val tasaCobranza: Int
        get() {
            val base = pagados.size + porCobrar.size
            return if (base == 0) 0 else pagados.size * 100 / base
        }

    // Registrar un nuevo compromiso (RF-17). Queda Pendiente.
    fun registrarCompromiso(
        deudor: String,
        rutDeudor: String,
        concepto: String,
        monto: Int,
        fechaVencimiento: String
    ) {
        val nuevo = CompromisoEmpresa(
            folio = "CP-2026-" + String.format(Locale.US, "%04d", contadorFolio),
            deudor = deudor.trim(),
            rutDeudor = rutDeudor.trim(),
            concepto = concepto.trim(),
            monto = monto,
            fechaVencimiento = fechaVencimiento.trim(),
            estado = EstadoCompromiso.PENDIENTE
        )
        contadorFolio++
        compromisos = listOf(nuevo) + compromisos
        mensaje = "Compromiso registrado"
    }

    // Anular un compromiso (RF-18): solo si sigue por cobrar (RN-01).
    fun anular(folio: String) {
        compromisos = compromisos.map {
            if (it.folio == folio && it.sePuedeAnular) {
                it.copy(estado = EstadoCompromiso.ANULADO)
            } else {
                it
            }
        }
        mensaje = "Compromiso anulado"
    }
}
