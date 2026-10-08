package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.UsuarioFinalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioFinalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<UsuarioFinalEntity>)

    @Query("SELECT * FROM usuario_final ORDER BY codigo")
    fun observarTodos(): Flow<List<UsuarioFinalEntity>>

    @Query("SELECT * FROM usuario_final WHERE codigo = :codigo")
    suspend fun obtenerPorCodigo(codigo: String): UsuarioFinalEntity?

    @Query("SELECT COUNT(*) FROM usuario_final")
    suspend fun contar(): Int
}
