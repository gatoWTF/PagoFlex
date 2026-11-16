package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.CompromisoRepository
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.remote.ReporteResponse
import com.example.pagoflex.model.EstadoReporte
import kotlinx.coroutines.launch

data class ReporteGestion(
    val numero: String,
    val usuario: String,
    val folio: String,
    val empresa: String,
    val detalleCompromiso: String,
    val montoCuota: Int?,
    val motivo: String,
    val comentario: String,
    val fecha: String,
    val estado: EstadoReporte
)

class AgenteViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)
    private val repositorio = CompromisoRepository()

    private var agenteCodigo: String? = null
    private var rutAgente: String = ""

    var reportes by mutableStateOf<List<ReporteGestion>>(emptyList())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    val cantidadAbiertos: Int get() = reportes.count { it.estado == EstadoReporte.ABIERTO }

    fun cargarAgente(rut: String) {
        rutAgente = rut
        agenteCodigo = null
        reportes = emptyList()
        viewModelScope.launch {
            codigoDelAgente()
            recargar()
        }
    }

    private suspend fun codigoDelAgente(): String? {
        if (agenteCodigo == null) {
            val rutSinFormato = rutAgente.trim().uppercase().replace(".", "").replace("-", "")
            agenteCodigo = db.agenteDao().buscarPorRut(rutSinFormato)?.codigo
        }
        return agenteCodigo
    }

    fun actualizar() {
        viewModelScope.launch { recargar() }
    }

    fun tomar(numero: String) {
        viewModelScope.launch {
            val codigo = codigoDelAgente() ?: run {
                mensaje = "No se encontró tu ficha de agente"
                return@launch
            }
            try {
                repositorio.tomarReporte(numero, codigo)
                mensaje = "Reporte tomado"
            } catch (e: Exception) {
                mensaje = e.message
            }
            recargar()
        }
    }

    fun resolver(numero: String) {
        viewModelScope.launch {
            val codigo = codigoDelAgente() ?: run {
                mensaje = "No se encontró tu ficha de agente"
                return@launch
            }
            try {
                repositorio.resolverReporte(numero, codigo)
                mensaje = "Reporte resuelto"
            } catch (e: Exception) {
                mensaje = e.message
            }
            recargar()
        }
    }

    fun consumirMensaje() { mensaje = null }

    private suspend fun recargar() {
        try {
            reportes = repositorio.listarReportes().map { it.aGestion() }
        } catch (e: Exception) {
            mensaje = e.message
        }
    }

    private fun ReporteResponse.aGestion(): ReporteGestion {
        val cuota = if (cuotaActual != null && totalCuotas != null) "cuota $cuotaActual de $totalCuotas" else null
        return ReporteGestion(
            numero = numero,
            usuario = nombreUsuario ?: rutUsuario,
            folio = compromisoFolio,
            empresa = empresaNombre ?: "Compromiso no encontrado",
            detalleCompromiso = listOfNotNull(concepto, cuota).joinToString(" · "),
            montoCuota = montoCuota,
            motivo = motivo,
            comentario = comentario,
            fecha = fecha,
            estado = estado
        )
    }
}
