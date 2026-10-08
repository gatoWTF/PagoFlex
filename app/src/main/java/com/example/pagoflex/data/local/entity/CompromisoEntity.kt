package com.example.pagoflex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagoflex.model.EstadoCompromiso

// Compromiso de pago: lo que una persona debe pagar a una empresa cliente (Anexo 8).
// Las referencias a usuario/empresa son logicas (se validan en la app), sin FK de Room,
// para poder cargar comprobantes historicos cuyo compromiso no esta en esta tabla.
@Entity(tableName = "compromiso")
data class CompromisoEntity(
    @PrimaryKey val folio: String,         // CP-2026-0101
    val usuarioCodigo: String,             // UF-xx
    val empresaCodigo: String,             // EC-xx
    val concepto: String,                  // "Arriendo departamento octubre"
    val numeroCuota: String?,              // "10/12" o null si no aplica
    val monto: Int,                        // pesos, entero y mayor que cero (RN-17)
    val fechaVencimiento: String,          // DD-MM-AAAA
    val estado: EstadoCompromiso,
    val recargoAplicado: Int,              // recargo ya sumado si esta Vencido (RN-03); 0 si no
    val registradoPor: String             // EJ-xx o "PagoFlex" si lo cargo el equipo
)
