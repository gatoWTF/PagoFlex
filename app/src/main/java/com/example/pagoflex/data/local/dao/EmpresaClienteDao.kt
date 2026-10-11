package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.EmpresaClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpresaClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(items: List<EmpresaClienteEntity>)

    @Query("SELECT * FROM empresa_cliente ORDER BY codigo")
    fun observarTodas(): Flow<List<EmpresaClienteEntity>>

    // Lectura de una sola vez, para resolver nombre y rubro de cada compromiso.
    @Query("SELECT * FROM empresa_cliente")
    suspend fun listarTodas(): List<EmpresaClienteEntity>

    @Query("SELECT * FROM empresa_cliente WHERE codigo = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): EmpresaClienteEntity?
}
