package com.example.pagoflex.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val URL_GATEWAY = "http://10.0.2.2:8080/"

    private val cliente: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_GATEWAY)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val usuarioApi: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }

    val compromisoApi: CompromisoApi by lazy { retrofit.create(CompromisoApi::class.java) }
}
