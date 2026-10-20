package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.EstadoReporte

@Entity(tableName = "reporte")
data class ReporteEntity(
    @PrimaryKey val numero: String,
    val compromisoFolio: String,
    val usuarioCodigo: String,
    val motivo: String,
    val comentario: String,
    val fecha: String,
    val estado: EstadoReporte,
    val agenteResponsable: String?,
    val respuestaUsuario: String?
)
