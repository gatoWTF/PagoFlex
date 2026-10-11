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

// Estado y logica de los compromisos del usuario final. Lee de Room con Flow
// (cuando la base cambia, la UI se actualiza sola) y escribe los cambios, por eso
// los datos persisten al cerrar y abrir la app.
class CompromisosViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)

    // Usuario fijo de la sesion simulada (coincide con el selector de rol).
    private val usuarioCodigo = "UF-01"

    var compromisos by mutableStateOf<List<Compromiso>>(emptyList())
        private set

    // Historial de pagos (RF-07).
    var comprobantes by mutableStateOf<List<Comprobante>>(emptyList())
        private set

    // Mensaje puntual para mostrar en una Snackbar; la pantalla lo consume y lo limpia.
    var mensaje by mutableStateOf<String?>(null)
        private set

    // Codigo -> empresa, para mostrar nombre y rubro legibles.
    private var empresas: Map<String, EmpresaClienteEntity> = emptyMap()
    // Ultimo comprobante emitido, para mostrarlo al instante aunque la base aun no refresque.
    private var ultimoComprobante: Comprobante? = null
    private var contadorComprobante = 460
    private var contadorReporte = 20

    init {
        // Observa empresas, compromisos y comprobantes a la vez; cualquier cambio en la
        // base (incluida la carga de la semilla la primera vez) actualiza la pantalla.
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

    // --- Lectura derivada para las pantallas ---

    val porPagar: List<Compromiso>
        get() = compromisos.filter { it.estaPorPagar }

    val totalPorPagar: Int
        get() = porPagar.sumOf { it.totalAPagar }

    val cantidadVencidos: Int
        get() = compromisos.count { it.estado == EstadoCompromiso.VENCIDO }

    val proximoVencimiento: String?
        get() = porPagar.minByOrNull { aOrden(it.fechaVencimiento) }?.fechaVencimiento

    // Indicador de cumplimiento: porcentaje de pagos hechos a tiempo (RF-08, RN-10).
    val porcentajeCumplimiento: Int
        get() = if (comprobantes.isEmpty()) 100
        else comprobantes.count { it.aTiempo } * 100 / comprobantes.size

    fun buscarPorFolio(folio: String): Compromiso? = compromisos.find { it.folio == folio }

    fun buscarComprobante(folio: String): Comprobante? =
        comprobantes.find { it.folio == folio } ?: ultimoComprobante?.takeIf { it.folio == folio }

    // --- Acciones (persisten en la base; la UI se refresca via Flow) ---

    // Pago simulado (RF-05): el compromiso pasa a Pagado y se emite un comprobante (RF-06, RN-05).
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

    // Reportar un problema (RF-09): el compromiso queda En revision y no se puede pagar (RN-08).
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

    // --- Mapeo entidad -> modelo de pantalla ---

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
            // El comprobante no guarda el concepto; se toma del compromiso si sigue cargado.
            concepto = conceptoPorFolio[compromisoFolio] ?: nombreEmpresa,
            empresa = nombreEmpresa,
            monto = montoPagado,
            fechaHora = fechaHora,
            aTiempo = aTiempo,
            canal = canal.etiqueta
        )
    }

    // --- Utilidades ---

    private fun ahoraFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(Date())

    private fun hoyFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())

    // Convierte DD-MM-AAAA en un numero AAAAMMDD para poder ordenar por fecha.
    private fun aOrden(fecha: String): Int {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0
        return partes[2].toInt() * 10000 + partes[1].toInt() * 100 + partes[0].toInt()
    }
}
