package com.example.pagoflex.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFeriados {

    private const val URL_API_FERIADOS = "https://api.boostr.cl/"

    val feriadoApi: FeriadoApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_API_FERIADOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeriadoApi::class.java)
    }
}
