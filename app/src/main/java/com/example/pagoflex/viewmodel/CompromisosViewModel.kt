package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.CompromisoRepository
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.local.entity.EmpresaClienteEntity
import com.example.pagoflex.data.local.entity.ReporteEntity
import com.example.pagoflex.data.remote.ComprobanteResponse
import com.example.pagoflex.data.remote.CompromisoResponse
import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.model.EstadoReporte
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompromisosViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)
    private val repositorio = CompromisoRepository()

    private var rutUsuario: String? = null
    private var usuarioCodigo: String? = null

    var compromisos by mutableStateOf<List<Compromiso>>(emptyList())
        private set

    var comprobantes by mutableStateOf<List<Comprobante>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    private var empresas: Map<String, EmpresaClienteEntity> = emptyMap()

    private var ultimoComprobante: Comprobante? = null
    private var contadorReporte = 20

    fun cargarUsuario(rut: String) {
        rutUsuario = rut
        usuarioCodigo = null
        compromisos = emptyList()
        comprobantes = emptyList()
        viewModelScope.launch {
            val rutSinFormato = rut.trim().uppercase().replace(".", "").replace("-", "")
            usuarioCodigo = db.usuarioFinalDao().buscarPorRut(rutSinFormato)?.codigo
            recargar()
        }
    }

    private suspend fun recargar() {
        val rut = rutUsuario ?: return
        try {
            empresas = db.empresaClienteDao().listarTodas().associateBy { it.codigo }
            val lista = repositorio.listarPorUsuario(rut)
            val pagos = repositorio.listarComprobantes(rut)
            compromisos = lista.map { it.aModelo() }
            val compromisoPorFolio = compromisos.associateBy { it.folio }
            comprobantes = pagos.map { it.aModelo(compromisoPorFolio) }
        } catch (e: Exception) {
            mensaje = e.message
        }
    }

    fun consumirMensaje() { mensaje = null }

    val porPagar: List<Compromiso>
        get() = compromisos.filter { it.estaPorPagar }

    val totalPorPagar: Int
        get() = porPagar.sumOf { it.totalAPagar }

    val cantidadVencidos: Int
        get() = compromisos.count { it.estado == EstadoCompromiso.VENCIDO }

    val proximoVencimiento: String?
        get() = porPagar.minByOrNull { aOrden(it.fechaVencimiento) }?.fechaVencimiento

    val porcentajeCumplimiento: Int
        get() = if (comprobantes.isEmpty()) 100
        else comprobantes.count { it.aTiempo } * 100 / comprobantes.size

    fun buscarPorFolio(folio: String): Compromiso? = compromisos.find { it.folio == folio }

    fun buscarComprobante(folio: String): Comprobante? =
        ultimoComprobante?.takeIf { it.folio == folio } ?: comprobantes.find { it.folio == folio }

    fun pagar(folio: String, alTerminar: (Comprobante) -> Unit) {
        viewModelScope.launch {
            try {
                val respuesta = repositorio.pagar(folio)
                recargar()
                val comprobante = respuesta.comprobante
                    .aModelo(compromisos.associateBy { it.folio })
                    .copy(siguienteCuota = respuesta.siguienteCuota?.aModelo())
                ultimoComprobante = comprobante
                alTerminar(comprobante)
            } catch (e: Exception) {
                mensaje = e.message
            }
        }
    }

    fun reportarProblema(folio: String, motivo: String, comentario: String) {
        val rut = rutUsuario ?: return
        val numero = "REP-" + String.format(Locale.US, "%04d", contadorReporte)
        contadorReporte++

        viewModelScope.launch {
            try {
                repositorio.cambiarEstado(folio, EstadoCompromiso.EN_REVISION)
                db.reporteDao().insertar(
                    ReporteEntity(
                        numero = numero,
                        compromisoFolio = folio,
                        usuarioCodigo = usuarioCodigo ?: rut,
                        motivo = motivo,
                        comentario = comentario,
                        fecha = hoyFormateado(),
                        estado = EstadoReporte.ABIERTO,
                        agenteResponsable = null,
                        respuestaUsuario = null
                    )
                )
                mensaje = "Reporte enviado"
                recargar()
            } catch (e: Exception) {
                mensaje = e.message
            }
        }
    }

    private fun CompromisoResponse.aModelo(): Compromiso {
        val empresa = empresas[empresaCodigo]
        return Compromiso(
            folio = folio,
            empresa = empresa?.nombreFantasia ?: empresaCodigo,
            rubro = empresa?.rubro ?: "",
            concepto = concepto,
            cuotaActual = cuotaActual,
            totalCuotas = totalCuotas,
            monto = monto,
            fechaVencimiento = fechaVencimiento,
            estado = estado,
            recargo = recargoAplicado,
            codigoCredito = codigoCredito
        )
    }

    private fun ComprobanteResponse.aModelo(compromisoPorFolio: Map<String, Compromiso>): Comprobante {
        val nombreEmpresa = empresas[empresaCodigo]?.nombreFantasia ?: empresaCodigo
        val compromisoPagado = compromisoPorFolio[compromisoFolio]
        return Comprobante(
            folio = numero,
            folioCompromiso = compromisoFolio,
            concepto = compromisoPagado?.concepto ?: nombreEmpresa,
            empresa = nombreEmpresa,
            monto = montoPagado,
            fechaHora = fechaHora,
            aTiempo = aTiempo,
            canal = canal.etiqueta,
            cuota = compromisoPagado?.cuota,
            creditoTerminado = compromisoPagado?.esUltimaCuota == true
        )
    }

    private fun hoyFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())

    private fun aOrden(fecha: String): Int {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0
        return partes[2].toInt() * 10000 + partes[1].toInt() * 100 + partes[0].toInt()
    }
}
