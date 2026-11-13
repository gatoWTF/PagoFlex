package com.example.pagoflex.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CompromisoApi {

    @GET("api/compromisos")
    suspend fun listarPorUsuario(@Query("rut") rut: String): List<CompromisoResponse>

    @GET("api/compromisos")
    suspend fun listarPorEmpresa(@Query("empresa") empresa: String): List<CompromisoResponse>

    @GET("api/compromisos/{folio}")
    suspend fun obtener(@Path("folio") folio: String): CompromisoResponse

    @POST("api/compromisos")
    suspend fun registrar(@Body datos: NuevoCompromisoRequest): CompromisoResponse

    @PUT("api/compromisos/{folio}/estado")
    suspend fun cambiarEstado(@Path("folio") folio: String, @Body datos: CambiarEstadoRequest): CompromisoResponse

    @POST("api/compromisos/{folio}/pagar")
    suspend fun pagar(@Path("folio") folio: String, @Body datos: PagoRequest): ComprobanteResponse

    @GET("api/comprobantes")
    suspend fun listarComprobantes(@Query("rut") rut: String): List<ComprobanteResponse>
}
