package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.EstadoReporte

// Reporte de problema sobre un compromiso (RF-09, RN-09).
// Un compromiso solo puede tener un reporte abierto a la vez.
@Entity(tableName = "reporte")
data class ReporteEntity(
    @PrimaryKey val numero: String,        // REP-0011
    val compromisoFolio: String,
    val usuarioCodigo: String,
    val motivo: String,                    // "Ya pague por otro medio", "No reconozco este cobro"...
    val comentario: String,
    val fecha: String,                     // DD-MM-AAAA
    val estado: EstadoReporte,
    val agenteResponsable: String?,        // AG-xx que lo tomo, o null
    val respuestaUsuario: String?          // respuesta de soporte, o null si no esta resuelto
)
