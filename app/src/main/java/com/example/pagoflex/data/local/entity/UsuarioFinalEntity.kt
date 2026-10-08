package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Usuario final (R-01). Datos que deben quedar guardados segun el Anexo 8.
@Entity(tableName = "usuario_final")
data class UsuarioFinalEntity(
    @PrimaryKey val codigo: String,        // UF-01
    val nombreCompleto: String,
    val rut: String,
    val correo: String,
    val telefono: String,
    val comuna: String,
    val fechaIncorporacion: String,        // formato DD-MM-AAAA (RN-17)
    val diasAviso: Int,                    // anticipacion elegida: 1, 3 o 5 (RN-15)
    val avisosActivos: Boolean
)
