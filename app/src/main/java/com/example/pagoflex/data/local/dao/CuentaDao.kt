package com.example.pagoflex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagoflex.data.local.entity.CuentaEntity

@Dao
interface CuentaDao{
    @Insert
    suspend fun insertar (cuenta: CuentaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertarTodas (cuentas: List<CuentaEntity>)

    @Query("Select * From cuenta WHERE correo = :correo")
    suspend fun buscarPorCorreo (correo: String): CuentaEntity?
}