package com.example.pagoflex.data

import com.example.pagoflex.data.remote.AnularRequest
import com.example.pagoflex.data.remote.CambiarEstadoRequest
import com.example.pagoflex.data.remote.ComprobanteResponse
import com.example.pagoflex.data.remote.CompromisoApi
import com.example.pagoflex.data.remote.CompromisoResponse
import com.example.pagoflex.data.remote.ErrorApi
import com.example.pagoflex.data.remote.GestionReporteRequest
import com.example.pagoflex.data.remote.NuevoCompromisoRequest
import com.example.pagoflex.data.remote.NuevoReporteRequest
import com.example.pagoflex.data.remote.PagoRequest
import com.example.pagoflex.data.remote.PagoResponse
import com.example.pagoflex.data.remote.ReporteResponse
import com.example.pagoflex.data.remote.RetrofitClient
import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.model.MotivoAnulacion
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

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

    suspend fun anular(
        folio: String,
        motivo: MotivoAnulacion,
        comentario: String,
        ejecutivoCodigo: String,
        empresaCodigo: String
    ): CompromisoResponse =
        llamar {
            compromisoApi.anular(
                folio,
                AnularRequest(motivo, comentario.trim().ifBlank { null }, ejecutivoCodigo, empresaCodigo)
            )
        }

    suspend fun pagar(folio: String): PagoResponse =
        llamar { compromisoApi.pagar(folio, PagoRequest(CanalPago.APP, "Pago simulado")) }

    suspend fun listarComprobantes(rut: String): List<ComprobanteResponse> =
        llamar { compromisoApi.listarComprobantes(rut) }

    suspend fun listarReportes(): List<ReporteResponse> =
        llamar { compromisoApi.listarReportes() }

    suspend fun crearReporte(folio: String, rut: String, motivo: String, comentario: String): ReporteResponse =
        llamar { compromisoApi.crearReporte(NuevoReporteRequest(folio, rut, motivo, comentario)) }

    suspend fun tomarReporte(numero: String, agenteCodigo: String): ReporteResponse =
        llamar { compromisoApi.tomarReporte(numero, GestionReporteRequest(agenteCodigo)) }

    suspend fun resolverReporte(numero: String, agenteCodigo: String): ReporteResponse =
        llamar { compromisoApi.resolverReporte(numero, GestionReporteRequest(agenteCodigo)) }

    private suspend fun <T> llamar(peticion: suspend () -> T): T = try {
        peticion()
    } catch (e: HttpException) {
        throw Exception(mensajeDelServidor(e))
    } catch (e: SocketTimeoutException) {
        throw Exception("El servidor de compromisos tardó demasiado en responder. Intenta de nuevo en unos segundos")
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
