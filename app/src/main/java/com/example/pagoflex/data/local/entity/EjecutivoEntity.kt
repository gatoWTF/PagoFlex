package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Ejecutivo de empresa cliente (R-03). Pertenece a una sola empresa (S-10).
@Entity(tableName = "ejecutivo")
data class EjecutivoEntity(
    @PrimaryKey val codigo: String,        // EJ-01
    val nombre: String,
    val rut: String,
    val correo: String,
    val telefono: String,
    val empresaCodigo: String,             // referencia logica a empresa_cliente.codigo
    val cargo: String,
    val activo: Boolean
)
