package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.AreaAgente

@Entity(tableName = "agente")
data class AgenteEntity(
    @PrimaryKey val codigo: String,
    val nombre: String,
    val rut: String,
    val correo: String,
    val area: AreaAgente,
    val activo: Boolean
)
