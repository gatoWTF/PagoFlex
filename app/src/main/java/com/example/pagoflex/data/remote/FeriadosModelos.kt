package com.example.pagoflex.data.remote

import com.example.pagoflex.model.Feriado

data class FeriadosResponse(
    val status: String?,
    val data: List<FeriadoDto>?
)

data class FeriadoDto(
    val date: String,
    val title: String,
    val inalienable: Boolean?
)

fun FeriadoDto.aFeriado(): Feriado {
    val (anio, mes, dia) = date.split("-")
    return Feriado(
        fecha = "$dia-$mes-$anio",
        nombre = title,
        irrenunciable = inalienable == true
    )
}
