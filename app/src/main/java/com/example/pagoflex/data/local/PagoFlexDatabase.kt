package com.example.pagoflex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pagoflex.data.local.dao.AgenteDao
import com.example.pagoflex.data.local.dao.ComprobanteDao
import com.example.pagoflex.data.local.dao.CompromisoDao
import com.example.pagoflex.data.local.dao.EjecutivoDao
import com.example.pagoflex.data.local.dao.EmpresaClienteDao
import com.example.pagoflex.data.local.dao.ReporteDao
import com.example.pagoflex.data.local.dao.UsuarioFinalDao
import com.example.pagoflex.data.local.entity.AgenteEntity
import com.example.pagoflex.data.local.entity.ComprobanteEntity
import com.example.pagoflex.data.local.entity.CompromisoEntity
import com.example.pagoflex.data.local.entity.EjecutivoEntity
import com.example.pagoflex.data.local.entity.EmpresaClienteEntity
import com.example.pagoflex.data.local.entity.ReporteEntity
import com.example.pagoflex.data.local.entity.UsuarioFinalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioFinalEntity::class,
        EmpresaClienteEntity::class,
        AgenteEntity::class,
        EjecutivoEntity::class,
        CompromisoEntity::class,
        ComprobanteEntity::class,
        ReporteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PagoFlexDatabase : RoomDatabase() {

    abstract fun usuarioFinalDao(): UsuarioFinalDao
    abstract fun empresaClienteDao(): EmpresaClienteDao
    abstract fun agenteDao(): AgenteDao
    abstract fun ejecutivoDao(): EjecutivoDao
    abstract fun compromisoDao(): CompromisoDao
    abstract fun comprobanteDao(): ComprobanteDao
    abstract fun reporteDao(): ReporteDao

    companion object {
        @Volatile
        private var instancia: PagoFlexDatabase? = null

        // Una sola instancia para toda la app (patron singleton).
        fun obtener(context: Context): PagoFlexDatabase =
            instancia ?: synchronized(this) {
                instancia ?: construir(context).also { instancia = it }
            }

        private fun construir(context: Context): PagoFlexDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                PagoFlexDatabase::class.java,
                "pagoflex.db"
            )
                .addCallback(SemillaCallback(context.applicationContext))
                // Si cambia el esquema (nueva columna/tabla) y subimos la version,
                // recrea la base en vez de crashear. Util para modificar en la defensa.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }

    // Carga los datos del Anexo 9 la primera vez que se crea la base.
    private class SemillaCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val base = obtener(context)
                base.empresaClienteDao().insertarTodas(DatosSemilla.empresas)
                base.usuarioFinalDao().insertarTodos(DatosSemilla.usuariosFinales)
                base.agenteDao().insertarTodos(DatosSemilla.agentes)
                base.ejecutivoDao().insertarTodos(DatosSemilla.ejecutivos)
                base.compromisoDao().insertarTodos(DatosSemilla.compromisos)
                base.comprobanteDao().insertarTodos(DatosSemilla.comprobantes)
                base.reporteDao().insertarTodos(DatosSemilla.reportes)
            }
        }
    }
}
