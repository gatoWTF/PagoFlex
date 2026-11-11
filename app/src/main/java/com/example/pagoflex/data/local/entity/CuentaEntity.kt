package com.example.pagoflex.data.local.entity

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.example.pagoflex.model.RolUsuario

@Entity(tableName = "cuenta")
data class CuentaEntity(
    @PrimaryKey val correo: String,
    val nombre: String,
    val rut: String,
    val contrasena: String,
    val rol:RolUsuario,
)