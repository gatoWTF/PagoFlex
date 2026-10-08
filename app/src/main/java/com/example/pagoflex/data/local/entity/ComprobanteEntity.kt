package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.CanalPago

// Comprobante de un pago. Nunca se modifica ni se elimina (RN-05).
// Incluye pagos hechos por app, portal o agente conversacional (RN-07).
@Entity(tableName = "comprobante")
data class ComprobanteEntity(
    @PrimaryKey val numero: String,        // CPR-000377
    val compromisoFolio: String,           // compromiso pagado
    val usuarioCodigo: String,
    val empresaCodigo: String,
    val fechaHora: String,                 // DD-MM-AAAA HH:mm
    val montoPagado: Int,                  // incluye recargo si corresponde
    val canal: CanalPago,
    val aTiempo: Boolean,
    val medioPago: String                  // nombre del medio; "Pago simulado" por defecto (Aclaracion 6)
)
