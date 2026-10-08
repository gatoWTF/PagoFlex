package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Empresa cliente que cobra a traves de PagoFlex (Anexo 8).
@Entity(tableName = "empresa_cliente")
data class EmpresaClienteEntity(
    @PrimaryKey val codigo: String,        // EC-01
    val razonSocial: String,
    val nombreFantasia: String,            // como la reconoce la persona
    val rut: String,
    val rubro: String,                     // Inmobiliaria, Educacion escolar, ONG...
    val correoAtencion: String,
    val telefono: String,
    val recargoAtraso: Int                 // recargo fijo en pesos (RN-03); puede ser 0
)
