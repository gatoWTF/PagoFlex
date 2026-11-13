package com.example.pagoflex.data

import com.example.pagoflex.data.remote.CambiarEstadoRequest
import com.example.pagoflex.data.remote.ComprobanteResponse
import com.example.pagoflex.data.remote.CompromisoApi
import com.example.pagoflex.data.remote.CompromisoResponse
import com.example.pagoflex.data.remote.ErrorApi
import com.example.pagoflex.data.remote.NuevoCompromisoRequest
import com.example.pagoflex.data.remote.PagoRequest
import com.example.pagoflex.data.remote.PagoResponse
import com.example.pagoflex.data.remote.RetrofitClient
import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.EstadoCompromiso
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class CompromisoRepository(
    private val compromisoApi: CompromisoApi = RetrofitClient.compromisoApi
) {

    suspend fun listarPorUsuario(rut: String): List<CompromisoResponse> =
        llamar { compromisoApi.listarPorUsuario(rut) }

    suspend fun listarPorEmpresa(empresaCodigo: String): List<CompromisoResponse> =
        llamar { compromisoApi.listarPorEmpresa(empresaCodigo) }

    suspend fun obtener(folio: String): CompromisoResponse =
        llamar { compromisoApi.obtener(folio) }

    suspend fun registrar(datos: NuevoCompromisoRequest): CompromisoResponse =
        llamar { compromisoApi.registrar(datos) }

    suspend fun cambiarEstado(folio: String, estado: EstadoCompromiso): CompromisoResponse =
        llamar { compromisoApi.cambiarEstado(folio, CambiarEstadoRequest(estado)) }

    suspend fun pagar(folio: String): PagoResponse =
        llamar { compromisoApi.pagar(folio, PagoRequest(CanalPago.APP, "Pago simulado")) }

    suspend fun listarComprobantes(rut: String): List<ComprobanteResponse> =
        llamar { compromisoApi.listarComprobantes(rut) }

    private suspend fun <T> llamar(peticion: suspend () -> T): T = try {
        peticion()
    } catch (e: HttpException) {
        throw Exception(mensajeDelServidor(e))
    } catch (e: IOException) {
        throw Exception("No se pudo conectar con el servidor de compromisos")
    }

    private fun mensajeDelServidor(e: HttpException): String {
        val cuerpo = e.response()?.errorBody()?.string()
        val mensaje = try {
            Gson().fromJson(cuerpo, ErrorApi::class.java)?.message
        } catch (ex: Exception) {
            null
        }
        return if (mensaje.isNullOrBlank()) "Error del servidor (${e.code()})" else mensaje
    }
}
