package com.example.pagoflex.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val URL_USUARIOS = "http://10.0.2.2:8082/"
    private const val URL_COMPROMISOS = "http://10.0.2.2:8083/"

    private fun crear(urlBase: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val usuarioApi: UsuarioApi by lazy { crear(URL_USUARIOS).create(UsuarioApi::class.java) }

    val compromisoApi: CompromisoApi by lazy { crear(URL_COMPROMISOS).create(CompromisoApi::class.java) }
}
