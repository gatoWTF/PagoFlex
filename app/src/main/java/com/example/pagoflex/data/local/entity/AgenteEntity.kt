package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.AreaAgente

// Agente PagoFlex (R-02): soporte o comercial (Anexo 8 y 9).
@Entity(tableName = "agente")
data class AgenteEntity(
    @PrimaryKey val codigo: String,        // AG-01
    val nombre: String,
    val rut: String,
    val correo: String,
    val area: AreaAgente,
    val activo: Boolean
)
