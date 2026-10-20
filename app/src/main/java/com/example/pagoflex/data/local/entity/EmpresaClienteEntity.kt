package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "empresa_cliente")
data class EmpresaClienteEntity(
    @PrimaryKey val codigo: String,
    val razonSocial: String,
    val nombreFantasia: String,
    val rut: String,
    val rubro: String,
    val correoAtencion: String,
    val telefono: String,
    val recargoAtraso: Int
)
