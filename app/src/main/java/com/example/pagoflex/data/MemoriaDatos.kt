package com.example.pagoflex.data

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
}
