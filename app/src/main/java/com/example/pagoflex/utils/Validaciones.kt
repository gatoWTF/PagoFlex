package com.example.pagoflex.utils

object Validaciones {
    const val LARGO_MINIMO_CONTRASENA = 6

    // texto@dominio.ext (solo Kotlin, sin depender de Android)
    private val patronCorreo =
        Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$""")

    fun correoValido(correo: String): Boolean = patronCorreo.matches(correo)

    fun contrasenaValida(contrasena: String): Boolean =
        contrasena.length >= LARGO_MINIMO_CONTRASENA

    // Monto en pesos: entero positivo, sin decimales ni separadores (RN-17).
    fun montoValido(texto: String): Boolean {
        val numero = texto.trim().toIntOrNull() ?: return false
        return numero > 0
    }

    // Fecha en formato DD-MM-AAAA con dia y mes plausibles (RN-17).
    fun fechaValida(texto: String): Boolean {
        val coincidencia = Regex("""^(\d{2})-(\d{2})-(\d{4})$""").matchEntire(texto.trim())
            ?: return false
        val (d, m, a) = coincidencia.destructured
        val dia = d.toInt()
        val mes = m.toInt()
        val anio = a.toInt()
        if (mes !in 1..12) return false
        val diasPorMes = intArrayOf(31, if (esBisiesto(anio)) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        return dia in 1..diasPorMes[mes - 1]
    }

    private fun esBisiesto(anio: Int): Boolean =
        (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0)

    // Valida el digito verificador de un RUT chileno con el modulo 11 (RN-18).
    // Acepta formato con o sin puntos y guion: "15.834.207-3" o "158342073".
    fun rutValido(rut: String): Boolean {
        val limpio = rut.trim().uppercase().replace(".", "").replace("-", "")
        if (limpio.length < 2) return false

        val cuerpo = limpio.dropLast(1)
        val digitoVerificador = limpio.last()
        if (cuerpo.isEmpty() || !cuerpo.all { it.isDigit() }) return false

        // Se multiplica cada digito, de derecha a izquierda, por 2,3,4,5,6,7 y se repite.
        var suma = 0
        var multiplo = 2
        for (caracter in cuerpo.reversed()) {
            suma += (caracter - '0') * multiplo
            multiplo = if (multiplo == 7) 2 else multiplo + 1
        }

        val esperado: Char = when (val resto = 11 - (suma % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> '0' + resto
        }
        return digitoVerificador == esperado
    }
}
