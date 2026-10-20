package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.local.entity.ComprobanteEntity
import com.example.pagoflex.data.local.entity.CompromisoEntity
import com.example.pagoflex.data.local.entity.EmpresaClienteEntity
import com.example.pagoflex.data.local.entity.ReporteEntity
import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.model.EstadoReporte
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompromisosViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)

    private val usuarioCodigo = "UF-01"

    var compromisos by mutableStateOf<List<Compromiso>>(emptyList())
        private set

    var comprobantes by mutableStateOf<List<Comprobante>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    private var empresas: Map<String, EmpresaClienteEntity> = emptyMap()

    private var ultimoComprobante: Comprobante? = null
    private var contadorComprobante = 460
    private var contadorReporte = 20

    init {

        viewModelScope.launch {
            combine(
                db.empresaClienteDao().observarTodas(),
                db.compromisoDao().observarPorUsuario(usuarioCodigo),
                db.comprobanteDao().observarPorUsuario(usuarioCodigo)
            ) { empresasList, compromisoEnts, comprobanteEnts ->
                Triple(empresasList, compromisoEnts, comprobanteEnts)
            }.collect { (empresasList, compromisoEnts, comprobanteEnts) ->
                empresas = empresasList.associateBy { it.codigo }
                val conceptoPorFolio = compromisoEnts.associate { it.folio to it.concepto }
                compromisos = compromisoEnts.map { it.aModelo() }
                comprobantes = comprobanteEnts.map { it.aModelo(conceptoPorFolio) }
            }
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
        comprobantes.find { it.folio == folio } ?: ultimoComprobante?.takeIf { it.folio == folio }

    fun pagar(folio: String): Comprobante {
        val compromiso = compromisos.first { it.folio == folio }
        val aTiempo = compromiso.estado != EstadoCompromiso.VENCIDO
        val numero = "CPR-" + String.format(Locale.US, "%06d", contadorComprobante)
        contadorComprobante++
        val fechaHora = ahoraFormateado()

        val comprobante = Comprobante(
            folio = numero,
            folioCompromiso = compromiso.folio,
            concepto = compromiso.concepto,
            empresa = compromiso.empresa,
            monto = compromiso.totalAPagar,
            fechaHora = fechaHora,
            aTiempo = aTiempo
        )
        ultimoComprobante = comprobante

        viewModelScope.launch {
            val ent = db.compromisoDao().obtenerPorFolio(folio) ?: return@launch
            db.compromisoDao().actualizar(ent.copy(estado = EstadoCompromiso.PAGADO, recargoAplicado = 0))
            db.comprobanteDao().insertar(
                ComprobanteEntity(
                    numero = numero,
                    compromisoFolio = folio,
                    usuarioCodigo = usuarioCodigo,
                    empresaCodigo = ent.empresaCodigo,
                    fechaHora = fechaHora,
                    montoPagado = compromiso.totalAPagar,
                    canal = CanalPago.APP,
                    aTiempo = aTiempo,
                    medioPago = "Pago simulado"
                )
            )
        }
        return comprobante
    }

    fun reportarProblema(folio: String, motivo: String, comentario: String) {
        mensaje = "Reporte enviado"
        val numero = "REP-" + String.format(Locale.US, "%04d", contadorReporte)
        contadorReporte++

        viewModelScope.launch {
            val ent = db.compromisoDao().obtenerPorFolio(folio)
            if (ent != null) {
                db.compromisoDao().actualizar(ent.copy(estado = EstadoCompromiso.EN_REVISION))
            }
            db.reporteDao().insertar(
                ReporteEntity(
                    numero = numero,
                    compromisoFolio = folio,
                    usuarioCodigo = usuarioCodigo,
                    motivo = motivo,
                    comentario = comentario,
                    fecha = hoyFormateado(),
                    estado = EstadoReporte.ABIERTO,
                    agenteResponsable = null,
                    respuestaUsuario = null
                )
            )
        }
    }

    private fun CompromisoEntity.aModelo(): Compromiso {
        val empresa = empresas[empresaCodigo]
        return Compromiso(
            folio = folio,
            empresa = empresa?.nombreFantasia ?: empresaCodigo,
            rubro = empresa?.rubro ?: "",
            concepto = concepto,
            cuota = numeroCuota,
            monto = monto,
            fechaVencimiento = fechaVencimiento,
            estado = estado,
            recargo = recargoAplicado
        )
    }

    private fun ComprobanteEntity.aModelo(conceptoPorFolio: Map<String, String>): Comprobante {
        val nombreEmpresa = empresas[empresaCodigo]?.nombreFantasia ?: empresaCodigo
        return Comprobante(
            folio = numero,
            folioCompromiso = compromisoFolio,

            concepto = conceptoPorFolio[compromisoFolio] ?: nombreEmpresa,
            empresa = nombreEmpresa,
            monto = montoPagado,
            fechaHora = fechaHora,
            aTiempo = aTiempo,
            canal = canal.etiqueta
        )
    }

    private fun ahoraFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Date())

    private fun hoyFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())

    private fun aOrden(fecha: String): Int {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0
        return partes[2].toInt() * 10000 + partes[1].toInt() * 100 + partes[0].toInt()
    }
}
