package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.CanalPago

@Entity(tableName = "comprobante")
data class ComprobanteEntity(
    @PrimaryKey val numero: String,
    val compromisoFolio: String,
    val usuarioCodigo: String,
    val empresaCodigo: String,
    val fechaHora: String,
    val montoPagado: Int,
    val canal: CanalPago,
    val aTiempo: Boolean,
    val medioPago: String
)
