package com.example.pagoflex.utils

object Validaciones {
    const val LARGO_MINIMO_CONTRASENA = 6

    // texto@dominio.ext (solo Kotlin, sin depender de Android)
    private val patronCorreo =
        Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$""")

    fun correoValido(correo: String): Boolean = patronCorreo.matches(correo)

    fun contrasenaValida(contrasena: String): Boolean =
        contrasena.length >= LARGO_MINIMO_CONTRASENA
}
