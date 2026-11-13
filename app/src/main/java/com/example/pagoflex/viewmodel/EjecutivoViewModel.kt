package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.CompromisoRepository
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.remote.CompromisoResponse
import com.example.pagoflex.data.remote.NuevoCompromisoRequest
import com.example.pagoflex.model.CompromisoEmpresa
import com.example.pagoflex.model.EstadoCompromiso
import kotlinx.coroutines.launch

class EjecutivoViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)
    private val repositorio = CompromisoRepository()

    private var empresaCodigo: String? = null
    private var ejecutivoCodigo: String? = null

    var empresa by mutableStateOf("")
        private set

    var compromisos by mutableStateOf<List<CompromisoEmpresa>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun cargarEjecutivo(correo: String) {
        empresaCodigo = null
        ejecutivoCodigo = null
        empresa = ""
        compromisos = emptyList()
        viewModelScope.launch {
            val ejecutivo = db.ejecutivoDao().buscarPorCorreo(correo.trim()) ?: return@launch
            ejecutivoCodigo = ejecutivo.codigo
            empresaCodigo = ejecutivo.empresaCodigo
            empresa = db.empresaClienteDao().obtenerPorCodigo(ejecutivo.empresaCodigo)?.nombreFantasia
                ?: ejecutivo.empresaCodigo
            recargar()
        }
    }

    private suspend fun recargar() {
        val codigo = empresaCodigo ?: return
        try {
            compromisos = repositorio.listarPorEmpresa(codigo).map { it.aModelo() }
        } catch (e: Exception) {
            mensaje = e.message
        }
    }

    fun consumirMensaje() { mensaje = null }

    private val porCobrar: List<CompromisoEmpresa>
        get() = compromisos.filter {
            it.estado == EstadoCompromiso.PENDIENTE || it.estado == EstadoCompromiso.VENCIDO
        }

    private val pagados: List<CompromisoEmpresa>
        get() = compromisos.filter { it.estado == EstadoCompromiso.PAGADO }

    val totalRecaudado: Int get() = pagados.sumOf { it.monto }
    val totalPorCobrar: Int get() = porCobrar.sumOf { it.monto }

    val tasaCobranza: Int
        get() {
            val base = pagados.size + porCobrar.size
            return if (base == 0) 0 else pagados.size * 100 / base
        }

    fun registrarCompromiso(
        deudor: String,
        rutDeudor: String,
        concepto: String,
        monto: Int,
        fechaVencimiento: String
    ) {
        val empresaActual = empresaCodigo ?: return
        val registrador = ejecutivoCodigo ?: return
        viewModelScope.launch {
            try {
                repositorio.registrar(
                    NuevoCompromisoRequest(
                        rutUsuario = rutDeudor.trim(),
                        nombreUsuario = deudor.trim(),
                        empresaCodigo = empresaActual,
                        concepto = concepto.trim(),
                        numeroCuota = null,
                        monto = monto,
                        fechaVencimiento = fechaVencimiento.trim(),
                        registradoPor = registrador
                    )
                )
                mensaje = "Compromiso registrado"
                recargar()
            } catch (e: Exception) {
                mensaje = e.message
            }
        }
    }

    fun anular(folio: String) {
        viewModelScope.launch {
            try {
                repositorio.cambiarEstado(folio, EstadoCompromiso.ANULADO)
                mensaje = "Compromiso anulado"
                recargar()
            } catch (e: Exception) {
                mensaje = e.message
            }
        }
    }

    private fun CompromisoResponse.aModelo(): CompromisoEmpresa = CompromisoEmpresa(
        folio = folio,
        deudor = nombreUsuario,
        rutDeudor = rutUsuario,
        concepto = concepto,
        monto = monto,
        fechaVencimiento = fechaVencimiento,
        estado = estado
    )
}
