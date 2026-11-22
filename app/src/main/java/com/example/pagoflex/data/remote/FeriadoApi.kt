package com.example.pagoflex.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface FeriadoApi {

    @GET("holidays/{anio}.json")
    suspend fun feriadosDelAnio(@Path("anio") anio: Int): FeriadosResponse
}
