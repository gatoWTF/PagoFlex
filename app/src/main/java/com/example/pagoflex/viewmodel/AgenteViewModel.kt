package com.example.pagoflex.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagoflex.data.local.PagoFlexDatabase
import com.example.pagoflex.data.local.entity.ReporteEntity
import com.example.pagoflex.data.local.entity.UsuarioFinalEntity
import com.example.pagoflex.model.EstadoReporte
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Reporte listo para mostrar en la bandeja del agente.
data class ReporteGestion(
    val numero: String,
    val usuario: String,
    val motivo: String,
    val comentario: String,
    val fecha: String,
    val estado: EstadoReporte
)

// Estado y logica del agente PagoFlex (R-02): gestiona los reportes de problema
// de los usuarios (RF-14, RF-15). Lee y escribe en Room, por eso los cambios de
// estado persisten al cerrar y abrir la app.
class AgenteViewModel(app: Application) : AndroidViewModel(app) {

    private val db = PagoFlexDatabase.obtener(app)

    // Agente fijo de la sesion simulada (coincide con la identidad del rol).
    private val agenteCodigo = "AG-01"

    var reportes by mutableStateOf<List<ReporteGestion>>(emptyList())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    // Copias crudas para poder actualizar el estado sin volver a consultar.
    private var entidades: Map<String, ReporteEntity> = emptyMap()
    private var usuarios: Map<String, UsuarioFinalEntity> = emptyMap()

    init {
        viewModelScope.launch {
            combine(
                db.reporteDao().observarTodos(),
                db.usuarioFinalDao().observarTodos()
            ) { reps, users -> reps to users }.collect { (reps, users) ->
                usuarios = users.associateBy { it.codigo }
                entidades = reps.associateBy { it.numero }
                reportes = reps.map { it.aGestion() }
            }
        }
    }

    val cantidadAbiertos: Int get() = reportes.count { it.estado == EstadoReporte.ABIERTO }

    // Tomar un reporte abierto: pasa a "En gestion" y queda a cargo del agente (RF-15).
    fun tomar(numero: String) {
        val ent = entidades[numero] ?: return
        if (ent.estado != EstadoReporte.ABIERTO) return
        mensaje = "Reporte tomado"
        viewModelScope.launch {
            db.reporteDao().insertar(
                ent.copy(estado = EstadoReporte.EN_GESTION, agenteResponsable = agenteCodigo)
            )
        }
    }

    // Resolver un reporte en gestion (RF-15).
    fun resolver(numero: String) {
        val ent = entidades[numero] ?: return
        if (ent.estado == EstadoReporte.RESUELTO) return
        mensaje = "Reporte resuelto"
        viewModelScope.launch {
            db.reporteDao().insertar(
                ent.copy(
                    estado = EstadoReporte.RESUELTO,
                    agenteResponsable = ent.agenteResponsable ?: agenteCodigo,
                    respuestaUsuario = "Gestionado por soporte."
                )
            )
        }
    }

    fun consumirMensaje() { mensaje = null }

    private fun ReporteEntity.aGestion(): ReporteGestion = ReporteGestion(
        numero = numero,
        usuario = usuarios[usuarioCodigo]?.nombreCompleto ?: usuarioCodigo,
        motivo = motivo,
        comentario = comentario,
        fecha = fecha,
        estado = estado
    )
}
