package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.AgenteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgenteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<AgenteEntity>)

    @Query("SELECT * FROM agente ORDER BY codigo")
    fun observarTodos(): Flow<List<AgenteEntity>>

    @Query("SELECT * FROM agente WHERE codigo = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): AgenteEntity?
}
