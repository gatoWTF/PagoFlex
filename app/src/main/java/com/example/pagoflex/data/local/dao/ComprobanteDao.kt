package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.ComprobanteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComprobanteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<ComprobanteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ComprobanteEntity)

    // Historial de pagos de una persona (RF-07)
    @Query("SELECT * FROM comprobante WHERE usuarioCodigo = :usuarioCodigo ORDER BY numero DESC")
    fun observarPorUsuario(usuarioCodigo: String): Flow<List<ComprobanteEntity>>

    // Igual que el anterior pero de una sola lectura (sin Flow), para cargar al inicio.
    @Query("SELECT * FROM comprobante WHERE usuarioCodigo = :usuarioCodigo ORDER BY numero DESC")
    suspend fun listarPorUsuario(usuarioCodigo: String): List<ComprobanteEntity>

    @Query("SELECT * FROM comprobante WHERE compromisoFolio = :folio")
    suspend fun obtenerPorCompromiso(folio: String): ComprobanteEntity?
}
