package com.example.pagoflex.data.local

import androidx.room.TypeConverter
import com.example.pagoflex.model.AreaAgente
import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.model.EstadoReporte

// Room no sabe guardar enums por si solo; aqui los convertimos a texto y de vuelta.
class Converters {
    @TypeConverter
    fun estadoCompromisoATexto(valor: EstadoCompromiso): String = valor.name

    @TypeConverter
    fun textoAEstadoCompromiso(valor: String): EstadoCompromiso = EstadoCompromiso.valueOf(valor)

    @TypeConverter
    fun canalPagoATexto(valor: CanalPago): String = valor.name

    @TypeConverter
    fun textoACanalPago(valor: String): CanalPago = CanalPago.valueOf(valor)

    @TypeConverter
    fun areaAgenteATexto(valor: AreaAgente): String = valor.name

    @TypeConverter
    fun textoAAreaAgente(valor: String): AreaAgente = AreaAgente.valueOf(valor)

    @TypeConverter
    fun estadoReporteATexto(valor: EstadoReporte): String = valor.name

    @TypeConverter
    fun textoAEstadoReporte(valor: String): EstadoReporte = EstadoReporte.valueOf(valor)
}
