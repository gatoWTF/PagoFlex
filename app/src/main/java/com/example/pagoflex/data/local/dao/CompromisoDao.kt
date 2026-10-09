package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pagoflex.data.local.entity.CompromisoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompromisoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<CompromisoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: CompromisoEntity)

    @Update
    suspend fun actualizar(item: CompromisoEntity)

    // Todos los compromisos de una persona (RF-03)
    @Query("SELECT * FROM compromiso WHERE usuarioCodigo = :usuarioCodigo ORDER BY fechaVencimiento")
    fun observarPorUsuario(usuarioCodigo: String): Flow<List<CompromisoEntity>>

    // Igual que el anterior pero de una sola lectura (sin Flow), para cargar al inicio.
    @Query("SELECT * FROM compromiso WHERE usuarioCodigo = :usuarioCodigo ORDER BY fechaVencimiento")
    suspend fun listarPorUsuario(usuarioCodigo: String): List<CompromisoEntity>

    // Lo que falta pagar de una persona: pendientes y vencidos (RF-02)
    @Query("SELECT * FROM compromiso WHERE usuarioCodigo = :usuarioCodigo AND estado IN ('PENDIENTE','VENCIDO') ORDER BY fechaVencimiento")
    fun observarPorPagar(usuarioCodigo: String): Flow<List<CompromisoEntity>>

    // Compromisos de una empresa, para el ejecutivo (RF-17)
    @Query("SELECT * FROM compromiso WHERE empresaCodigo = :empresaCodigo ORDER BY fechaVencimiento")
    fun observarPorEmpresa(empresaCodigo: String): Flow<List<CompromisoEntity>>

    @Query("SELECT * FROM compromiso WHERE folio = :folio")
    suspend fun obtenerPorFolio(folio: String): CompromisoEntity?

    @Query("SELECT COUNT(*) FROM compromiso")
    suspend fun contar(): Int
}
