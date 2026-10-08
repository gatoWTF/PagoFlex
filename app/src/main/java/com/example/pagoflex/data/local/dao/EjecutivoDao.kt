package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.EjecutivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EjecutivoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<EjecutivoEntity>)

    @Query("SELECT * FROM ejecutivo ORDER BY codigo")
    fun observarTodos(): Flow<List<EjecutivoEntity>>

    @Query("SELECT * FROM ejecutivo WHERE empresaCodigo = :empresaCodigo ORDER BY codigo")
    fun observarPorEmpresa(empresaCodigo: String): Flow<List<EjecutivoEntity>>

    @Query("SELECT * FROM ejecutivo WHERE codigo = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): EjecutivoEntity?
}
