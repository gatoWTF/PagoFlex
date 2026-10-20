package com.example.pagoflex.utils

import java.util.Locale

object FormatoMoneda {

    fun clp(monto: Int): String =
        "$" + String.format(Locale.US, "%,d", monto).replace(',', '.')
}
