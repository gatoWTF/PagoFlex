package com.example.pagoflex.model

data class Usuario(
    val id: Long,
    val nombre: String,
    val rut: String,
    val correo: String,
    val rol: RolUsuario
)
