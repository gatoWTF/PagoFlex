package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.MemoriaDatos
import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estado y logica de los compromisos del usuario final.
// Por ahora usa datos en memoria; la lectura desde Room se integra al final.
class CompromisosViewModel : ViewModel() {

    var compromisos by mutableStateOf(MemoriaDatos.compromisosDeEjemplo)
        private set

    // Historial de pagos (RF-07): parte con los pagos anteriores y crece con cada pago nuevo.
    var comprobantes by mutableStateOf(MemoriaDatos.historialDeEjemplo)
        private set

    // Mensaje puntual para mostrar en una Snackbar; la pantalla lo consume y lo limpia.
    var mensaje by mutableStateOf<String?>(null)
        private set

    fun consumirMensaje() { mensaje = null }

    // Numero correlativo para el folio del proximo comprobante.
    private var contadorComprobante = 460

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

    // Indicador de cumplimiento: porcentaje de pagos hechos a tiempo (RF-08, RN-10).
    val porcentajeCumplimiento: Int
        get() = if (comprobantes.isEmpty()) 100
        else comprobantes.count { it.aTiempo } * 100 / comprobantes.size

    fun buscarPorFolio(folio: String): Compromiso? = compromisos.find { it.folio == folio }

    fun buscarComprobante(folio: String): Comprobante? = comprobantes.find { it.folio == folio }

    // Pago simulado (RF-05): el compromiso pasa a Pagado y se emite un comprobante (RF-06, RN-05).
    // Devuelve el comprobante para poder mostrarlo.
    fun pagar(folio: String): Comprobante {
        val compromiso = compromisos.first { it.folio == folio }
        // A tiempo si no estaba vencido al momento de pagar.
        val aTiempo = compromiso.estado != EstadoCompromiso.VENCIDO

        val comprobante = Comprobante(
            folio = "CPR-" + String.format(Locale.US, "%06d", contadorComprobante),
            folioCompromiso = compromiso.folio,
            concepto = compromiso.concepto,
            empresa = compromiso.empresa,
            monto = compromiso.totalAPagar,
            fechaHora = ahoraFormateado(),
            aTiempo = aTiempo
        )
        contadorComprobante++

        compromisos = compromisos.map {
            if (it.folio == folio) it.copy(estado = EstadoCompromiso.PAGADO, recargo = 0) else it
        }
        comprobantes = listOf(comprobante) + comprobantes
        return comprobante
    }

    // Reportar un problema (RF-09): el compromiso queda En revision y no se puede pagar (RN-08).
    fun reportarProblema(folio: String) {
        compromisos = compromisos.map {
            if (it.folio == folio) it.copy(estado = EstadoCompromiso.EN_REVISION) else it
        }
        mensaje = "Reporte enviado"
    }

    // Fecha y hora actual como "dd-MM-aaaa HH:mm" (SimpleDateFormat sirve desde minSdk 24).
    private fun ahoraFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Date())

    // Convierte DD-MM-AAAA en un numero AAAAMMDD para poder ordenar por fecha.
    private fun aOrden(fecha: String): Int {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0
        return partes[2].toInt() * 10000 + partes[1].toInt() * 100 + partes[0].toInt()
    }
}
