package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.EstadoCompromiso

@Entity(tableName = "compromiso")
data class CompromisoEntity(
    @PrimaryKey val folio: String,
    val usuarioCodigo: String,
    val empresaCodigo: String,
    val concepto: String,
    val numeroCuota: String?,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso,
    val recargoAplicado: Int,
    val registradoPor: String
)
