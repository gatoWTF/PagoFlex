package com.example.pagoflex.data

import com.example.pagoflex.data.remote.ActualizarUsuarioRequest
import com.example.pagoflex.data.remote.CambiarContrasenaRequest
import com.example.pagoflex.data.remote.ErrorApi
import com.example.pagoflex.data.remote.LoginRequest
import com.example.pagoflex.data.remote.RegistroRequest
import com.example.pagoflex.data.remote.RetrofitClient
import com.example.pagoflex.data.remote.UsuarioApi
import com.example.pagoflex.data.remote.aUsuario
import com.example.pagoflex.model.Usuario
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class UsuarioRepository(
    private val usuarioApi: UsuarioApi = RetrofitClient.usuarioApi
) {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario =
        llamar { usuarioApi.iniciarSesion(LoginRequest(correo, contrasena)) }.aUsuario()

    suspend fun registrar(nombre: String, rut: String, correo: String, contrasena: String): Usuario =
        llamar { usuarioApi.registrar(RegistroRequest(nombre, rut, correo, contrasena)) }.aUsuario()

    suspend fun obtener(id: Long): Usuario =
        llamar { usuarioApi.obtener(id) }.aUsuario()

    suspend fun actualizar(id: Long, nombre: String, rut: String, correo: String): Usuario =
        llamar { usuarioApi.actualizar(id, ActualizarUsuarioRequest(nombre, rut, correo)) }.aUsuario()

    suspend fun cambiarContrasena(id: Long, actual: String, nueva: String) =
        llamar { usuarioApi.cambiarContrasena(id, CambiarContrasenaRequest(actual, nueva)) }

    private suspend fun <T> llamar(peticion: suspend () -> T): T = try {
        peticion()
    } catch (e: HttpException) {
        throw Exception(mensajeDelServidor(e))
    } catch (e: IOException) {
        throw Exception("No se pudo conectar con el servidor")
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
