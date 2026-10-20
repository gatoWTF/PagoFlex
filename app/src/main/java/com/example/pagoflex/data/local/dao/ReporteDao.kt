package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.ReporteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<ReporteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ReporteEntity)

    @Query("SELECT * FROM reporte WHERE estado = 'ABIERTO' ORDER BY fecha")
    fun observarAbiertos(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte ORDER BY estado, fecha DESC")
    fun observarTodos(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte WHERE usuarioCodigo = :usuarioCodigo ORDER BY fecha DESC")
    fun observarPorUsuario(usuarioCodigo: String): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reporte WHERE compromisoFolio = :folio AND estado != 'RESUELTO' LIMIT 1")
    suspend fun obtenerAbiertoPorCompromiso(folio: String): ReporteEntity?
}
