package com.example.pagoflex.data

import com.example.pagoflex.model.Comprobante
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso

// Datos de ejemplo en memoria para construir y probar las pantallas.
// Corresponden a Camila Rojas (UF-01) del Anexo 9. Temporal: luego se leen desde Room.
object MemoriaDatos {

    val compromisosDeEjemplo = listOf(
        Compromiso("CP-2026-0101", "Plaza Oriente", "Administración de arriendos", "Arriendo departamento octubre", "10/12", 520000, "10-10-2026", EstadoCompromiso.PENDIENTE),
        Compromiso("CP-2026-0102", "Club Los Halcones", "Club deportivo", "Cuota social octubre", "10/12", 15000, "05-10-2026", EstadoCompromiso.VENCIDO, recargo = 2000),
        Compromiso("CP-2026-0103", "Manos Abiertas", "ONG", "Aporte mensual octubre", null, 10000, "15-10-2026", EstadoCompromiso.PENDIENTE),
        Compromiso("CP-2026-0104", "Crédito Andino", "Crédito de consumo", "Crédito de consumo", "7/24", 86400, "25-09-2026", EstadoCompromiso.PAGADO),
        Compromiso("CP-2026-0105", "Crédito Andino", "Crédito de consumo", "Crédito de consumo", "8/24", 86400, "25-10-2026", EstadoCompromiso.PENDIENTE)
    )

    // Pagos anteriores de Camila para el historial (RF-07). Mezcla de a tiempo y atrasados
    // para que el indicador de cumplimiento tenga datos (RF-08).
    val historialDeEjemplo = listOf(
        Comprobante("CPR-000451", "CP-2026-0104", "Crédito de consumo", "Crédito Andino", 86400, "24-09-2026 10:42", aTiempo = true),
        Comprobante("CPR-000405", "CP-2026-0091", "Aporte mensual septiembre", "Manos Abiertas", 10000, "15-09-2026 08:22", aTiempo = true),
        Comprobante("CPR-000402", "CP-2026-0090", "Cuota social septiembre", "Club Los Halcones", 17000, "07-09-2026 18:40", aTiempo = false),
        Comprobante("CPR-000398", "CP-2026-0089", "Arriendo departamento septiembre", "Plaza Oriente", 520000, "09-09-2026 13:05", aTiempo = true)
    )
}
