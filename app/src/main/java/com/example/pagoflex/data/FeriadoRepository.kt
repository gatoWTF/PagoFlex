package com.example.pagoflex.data

import com.example.pagoflex.data.remote.FeriadoApi
import com.example.pagoflex.data.remote.RetrofitFeriados
import com.example.pagoflex.data.remote.aFeriado
import com.example.pagoflex.model.Feriado
import retrofit2.HttpException
import java.io.IOException

class FeriadoRepository(
    private val feriadoApi: FeriadoApi = RetrofitFeriados.feriadoApi
) {

    suspend fun feriadosDelAnio(anio: Int): List<Feriado> = try {
        feriadoApi.feriadosDelAnio(anio).data.orEmpty().map { it.aFeriado() }
    } catch (e: HttpException) {
        throw Exception("La API de feriados respondió con un error (${e.code()})")
    } catch (e: IOException) {
        throw Exception("No se pudieron cargar los feriados")
    }
}
