package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.local.entity.CompromisoEntity
import com.example.pagoflex.data.local.entity.UsuarioFinalEntity
import com.example.pagoflex.model.CompromisoEmpresa
import com.example.pagoflex.model.EstadoCompromiso
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estado y logica del ejecutivo de empresa cliente (R-03). Lee y escribe en Room:
// registrar y anular compromisos persisten al cerrar y abrir la app.
class EjecutivoViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)

    // Empresa fija de la sesion simulada (coincide con el selector de rol).
    private val empresaCodigo = "EC-03"
    val empresa = "Crédito Andino"

    var compromisos by mutableStateOf<List<CompromisoEmpresa>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    // Codigo -> usuario, para mostrar el nombre y RUT del deudor y para buscar por RUT.
    private var usuarios: Map<String, UsuarioFinalEntity> = emptyMap()
    private var contadorFolio = 130
    private var contadorUsuario = 900

    init {
        viewModelScope.launch {
            combine(
                db.compromisoDao().observarPorEmpresa(empresaCodigo),
                db.usuarioFinalDao().observarTodos()
            ) { compromisoEnts, usuarioList ->
                compromisoEnts to usuarioList
            }.collect { (compromisoEnts, usuarioList) ->
                usuarios = usuarioList.associateBy { it.codigo }
                compromisos = compromisoEnts.map { it.aModelo() }
            }
        }
    }

    fun consumirMensaje() { mensaje = null }

    // --- Cobranza del mes (RF-20, RN-24) ---

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

    // --- Acciones ---

    // Registrar un compromiso (RF-17). Si el RUT no existe aun, se crea el deudor.
    fun registrarCompromiso(
        deudor: String,
        rutDeudor: String,
        concepto: String,
        monto: Int,
        fechaVencimiento: String
    ) {
        mensaje = "Compromiso registrado"
        val folio = "CP-2026-" + String.format(Locale.US, "%04d", contadorFolio)
        contadorFolio++

        viewModelScope.launch {
            val codigoUsuario = resolverUsuario(deudor.trim(), rutDeudor.trim())
            db.compromisoDao().insertar(
                CompromisoEntity(
                    folio = folio,
                    usuarioCodigo = codigoUsuario,
                    empresaCodigo = empresaCodigo,
                    concepto = concepto.trim(),
                    numeroCuota = null,
                    monto = monto,
                    fechaVencimiento = fechaVencimiento.trim(),
                    estado = EstadoCompromiso.PENDIENTE,
                    recargoAplicado = 0,
                    registradoPor = "EJ-03"
                )
            )
        }
    }

    // Anular un compromiso (RF-18): solo si sigue por cobrar (RN-01).
    fun anular(folio: String) {
        mensaje = "Compromiso anulado"
        viewModelScope.launch {
            val ent = db.compromisoDao().obtenerPorFolio(folio) ?: return@launch
            if (ent.estado == EstadoCompromiso.PENDIENTE || ent.estado == EstadoCompromiso.VENCIDO) {
                db.compromisoDao().actualizar(ent.copy(estado = EstadoCompromiso.ANULADO))
            }
        }
    }

    // Devuelve el codigo de un usuario existente con ese RUT, o crea uno nuevo.
    private suspend fun resolverUsuario(nombre: String, rut: String): String {
        val existente = usuarios.values.find { normalizarRut(it.rut) == normalizarRut(rut) }
        if (existente != null) return existente.codigo

        val codigo = "UF-" + String.format(Locale.US, "%03d", contadorUsuario)
        contadorUsuario++
        db.usuarioFinalDao().insertarTodos(
            listOf(
                UsuarioFinalEntity(
                    codigo = codigo,
                    nombreCompleto = nombre,
                    rut = rut,
                    correo = "",
                    telefono = "",
                    comuna = "",
                    fechaIncorporacion = hoyFormateado(),
                    diasAviso = 3,
                    avisosActivos = true
                )
            )
        )
        return codigo
    }

    // --- Mapeo entidad -> modelo de pantalla ---

    private fun CompromisoEntity.aModelo(): CompromisoEmpresa {
        val usuario = usuarios[usuarioCodigo]
        return CompromisoEmpresa(
            folio = folio,
            deudor = usuario?.nombreCompleto ?: usuarioCodigo,
            rutDeudor = usuario?.rut ?: "",
            concepto = concepto,
            monto = monto,
            fechaVencimiento = fechaVencimiento,
            estado = estado
        )
    }

    private fun normalizarRut(rut: String): String =
        rut.trim().uppercase().replace(".", "").replace("-", "")

    private fun hoyFormateado(): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
}
