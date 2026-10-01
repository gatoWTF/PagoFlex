package com.example.pagoflex.utils

import java.util.Locale

object FormatoMoneda {
    // 25990 -> "$25.990"
    fun clp(monto: Int): String =
        "$" + String.format(Locale.US, "%,d", monto).replace(',', '.')
}
