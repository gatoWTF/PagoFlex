package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejecutivo")
data class EjecutivoEntity(
    @PrimaryKey val codigo: String,
    val nombre: String,
    val rut: String,
    val correo: String,
    val telefono: String,
    val empresaCodigo: String,
    val cargo: String,
    val activo: Boolean
)
