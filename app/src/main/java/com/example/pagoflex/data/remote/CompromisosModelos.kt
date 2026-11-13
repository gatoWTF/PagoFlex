package com.example.pagoflex.data.remote

import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.EstadoCompromiso

data class CompromisoResponse(
    val folio: String,
    val rutUsuario: String,
    val nombreUsuario: String,
    val empresaCodigo: String,
    val concepto: String,
    val numeroCuota: String?,
    val monto: Int,
    val fechaVencimiento: String,
    val estado: EstadoCompromiso,
    val recargoAplicado: Int,
    val registradoPor: String
)

data class ComprobanteResponse(
    val numero: String,
    val compromisoFolio: String,
    val rutUsuario: String,
    val empresaCodigo: String,
    val fechaHora: String,
    val montoPagado: Int,
    val canal: CanalPago,
    val aTiempo: Boolean,
    val medioPago: String
)

data class NuevoCompromisoRequest(
    val rutUsuario: String,
    val nombreUsuario: String,
    val empresaCodigo: String,
    val concepto: String,
    val numeroCuota: String?,
    val monto: Int,
    val fechaVencimiento: String,
    val registradoPor: String
)

data class CambiarEstadoRequest(
    val estado: EstadoCompromiso
)

data class PagoRequest(
    val canal: CanalPago,
    val medioPago: String
)
