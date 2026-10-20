package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_final")
data class UsuarioFinalEntity(
    @PrimaryKey val codigo: String,
    val nombreCompleto: String,
    val rut: String,
    val correo: String,
    val telefono: String,
    val comuna: String,
    val fechaIncorporacion: String,
    val diasAviso: Int,
    val avisosActivos: Boolean
)
