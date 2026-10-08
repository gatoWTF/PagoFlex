package com.example.pagoflex.data.local

import com.example.pagoflex.data.local.entity.AgenteEntity
import com.example.pagoflex.data.local.entity.ComprobanteEntity
import com.example.pagoflex.data.local.entity.CompromisoEntity
import com.example.pagoflex.data.local.entity.EjecutivoEntity
import com.example.pagoflex.data.local.entity.EmpresaClienteEntity
import com.example.pagoflex.data.local.entity.ReporteEntity
import com.example.pagoflex.data.local.entity.UsuarioFinalEntity
import com.example.pagoflex.model.AreaAgente
import com.example.pagoflex.model.CanalPago
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.model.EstadoReporte

// Datos de ejemplo del Anexo 9 (ficticios). Se cargan una sola vez al crear la base.
// Los nombres de personas y empresas conservan tildes porque son datos visibles al usuario.
object DatosSemilla {

    val usuariosFinales = listOf(
        UsuarioFinalEntity("UF-01", "Camila Andrea Rojas Peña", "15.834.207-3", "crojas@correo.ejemplo", "+56 9 0000 0101", "Maipú", "14-03-2023", 3, true),
        UsuarioFinalEntity("UF-02", "Luis Alberto Muñoz Tapia", "12.476.318-5", "lmunoz@correo.ejemplo", "+56 9 0000 0102", "Puente Alto", "02-11-2023", 1, true),
        UsuarioFinalEntity("UF-03", "Rosa Elena Contreras Díaz", "6.893.541-5", "rcontreras@correo.ejemplo", "+56 9 0000 0103", "San Miguel", "20-06-2024", 5, true),
        UsuarioFinalEntity("UF-04", "Matías Ignacio Soto Vergara", "21.045.873-5", "msoto@correo.ejemplo", "+56 9 0000 0104", "La Florida", "07-03-2025", 3, false),
        UsuarioFinalEntity("UF-05", "Fernanda Paz Godoy Lagos", "17.562.904-1", "fgodoy@correo.ejemplo", "+56 9 0000 0105", "Ñuñoa", "15-01-2024", 3, true),
        UsuarioFinalEntity("UF-06", "Héctor Manuel Pizarro Campos", "10.318.265-4", "hpizarro@correo.ejemplo", "+56 9 0000 0106", "Quilicura", "28-08-2023", 3, true),
        UsuarioFinalEntity("UF-07", "Javiera Constanza Reyes Molina", "19.287.430-0", "jreyes@correo.ejemplo", "+56 9 0000 0107", "San Bernardo", "03-05-2025", 1, true),
        UsuarioFinalEntity("UF-08", "Benjamín Esteban Araya Fuentes", "18.704.396-4", "baraya@correo.ejemplo", "+56 9 0000 0108", "Estación Central", "11-09-2026", 3, true)
    )

    val empresas = listOf(
        EmpresaClienteEntity("EC-01", "Inmobiliaria Altos del Maipo SpA", "Altos del Maipo", "76.845.213-K", "Inmobiliaria", "atencion@altosmaipo.ejemplo", "+56 2 0000 0201", 8500),
        EmpresaClienteEntity("EC-02", "Corporación Educacional Bosque Nativo", "Colegio Bosque Nativo", "65.132.874-8", "Educación escolar", "pagos@bosquenativo.ejemplo", "+56 2 0000 0202", 5000),
        EmpresaClienteEntity("EC-03", "Crédito Andino S.A.", "Crédito Andino", "96.571.438-3", "Crédito de consumo", "clientes@creditoandino.ejemplo", "+56 2 0000 0203", 12000),
        EmpresaClienteEntity("EC-04", "Seguros Horizonte Sur S.A.", "Horizonte Sur", "96.814.205-4", "Seguros", "contacto@horizontesur.ejemplo", "+56 2 0000 0204", 3000),
        EmpresaClienteEntity("EC-05", "Fundación Manos Abiertas", "Manos Abiertas", "65.247.190-0", "ONG", "socios@manosabiertas.ejemplo", "+56 2 0000 0205", 0),
        EmpresaClienteEntity("EC-06", "Club Deportivo Los Halcones", "Club Los Halcones", "65.398.412-K", "Club deportivo", "tesoreria@loshalcones.ejemplo", "+56 2 0000 0206", 2000),
        EmpresaClienteEntity("EC-07", "Instituto Técnico Cordillera SpA", "Instituto Cordillera", "77.214.583-7", "Educación superior", "finanzas@itcordillera.ejemplo", "+56 2 0000 0207", 6000),
        EmpresaClienteEntity("EC-08", "Arriendos Plaza Oriente Ltda.", "Plaza Oriente", "76.432.871-K", "Administración de arriendos", "cobranza@plazaoriente.ejemplo", "+56 2 0000 0208", 7500)
    )

    val agentes = listOf(
        AgenteEntity("AG-01", "Andrés Felipe Lagos Meza", "16.248.317-K", "alagos@pagoflex.ejemplo", AreaAgente.SOPORTE, true),
        AgenteEntity("AG-02", "Daniela Sofía Ortiz Bravo", "18.352.091-1", "dortiz@pagoflex.ejemplo", AreaAgente.SOPORTE, true),
        AgenteEntity("AG-03", "Ricardo José Salinas Cáceres", "13.907.264-2", "rsalinas@pagoflex.ejemplo", AreaAgente.SOPORTE, false),
        AgenteEntity("AG-04", "Valentina Ignacia Paredes Silva", "17.781.435-0", "vparedes@pagoflex.ejemplo", AreaAgente.COMERCIAL, true),
        AgenteEntity("AG-05", "Tomás Alejandro Vidal Rojas", "19.564.028-9", "tvidal@pagoflex.ejemplo", AreaAgente.COMERCIAL, true)
    )

    val ejecutivos = listOf(
        EjecutivoEntity("EJ-01", "Paula Andrea Saavedra Núñez", "14.629.853-2", "psaavedra@altosmaipo.ejemplo", "+56 9 0000 0401", "EC-01", "Ejecutiva de cobranza", true),
        EjecutivoEntity("EJ-02", "Marco Antonio Fuentes Riquelme", "11.853.720-3", "mfuentes@bosquenativo.ejemplo", "+56 9 0000 0402", "EC-02", "Encargado de finanzas", true),
        EjecutivoEntity("EJ-03", "Claudia Beatriz Henríquez Soto", "15.370.946-7", "chenriquez@creditoandino.ejemplo", "+56 9 0000 0403", "EC-03", "Jefa de cobranza", true),
        EjecutivoEntity("EJ-04", "Felipe Ignacio Morales Tapia", "17.095.482-3", "fmorales@creditoandino.ejemplo", "+56 9 0000 0404", "EC-03", "Ejecutivo de cobranza", true),
        EjecutivoEntity("EJ-05", "Gloria Inés Carrasco Vega", "9.846.217-1", "gcarrasco@loshalcones.ejemplo", "+56 9 0000 0405", "EC-06", "Tesorera", true),
        EjecutivoEntity("EJ-06", "Sebastián Andrés Núñez Olivares", "18.930.571-0", "snunez@itcordillera.ejemplo", "+56 9 0000 0406", "EC-07", "Analista de finanzas", true),
        EjecutivoEntity("EJ-07", "Mónica Patricia Leiva Castro", "12.584.396-4", "mleiva@plazaoriente.ejemplo", "+56 9 0000 0407", "EC-08", "Administradora de cobranza", true)
    )

    // recargoAplicado = total a pagar - monto cuando esta Vencido; 0 en el resto (RN-03).
    val compromisos = listOf(
        CompromisoEntity("CP-2026-0101", "UF-01", "EC-08", "Arriendo departamento octubre", "10/12", 520000, "10-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-07"),
        CompromisoEntity("CP-2026-0102", "UF-01", "EC-06", "Cuota social octubre", "10/12", 15000, "05-10-2026", EstadoCompromiso.VENCIDO, 2000, "EJ-05"),
        CompromisoEntity("CP-2026-0103", "UF-01", "EC-05", "Aporte mensual octubre", null, 10000, "15-10-2026", EstadoCompromiso.PENDIENTE, 0, "PagoFlex"),
        CompromisoEntity("CP-2026-0104", "UF-01", "EC-03", "Crédito de consumo", "7/24", 86400, "25-09-2026", EstadoCompromiso.PAGADO, 0, "EJ-03"),
        CompromisoEntity("CP-2026-0105", "UF-01", "EC-03", "Crédito de consumo", "8/24", 86400, "25-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-03"),
        CompromisoEntity("CP-2026-0106", "UF-02", "EC-01", "Cuota pie departamento", "14/36", 310000, "30-09-2026", EstadoCompromiso.VENCIDO, 8500, "EJ-01"),
        CompromisoEntity("CP-2026-0107", "UF-02", "EC-01", "Cuota pie departamento", "15/36", 310000, "30-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-01"),
        CompromisoEntity("CP-2026-0108", "UF-03", "EC-04", "Seguro de hogar octubre", "10/12", 18900, "01-10-2026", EstadoCompromiso.PAGADO, 0, "PagoFlex"),
        CompromisoEntity("CP-2026-0109", "UF-04", "EC-07", "Arancel octubre", "8/10", 142000, "05-10-2026", EstadoCompromiso.EN_REVISION, 0, "EJ-06"),
        CompromisoEntity("CP-2026-0110", "UF-05", "EC-02", "Mensualidad octubre", "8/10", 185000, "10-10-2026", EstadoCompromiso.PENDIENTE, 0, "EJ-02"),
        CompromisoEntity("CP-2026-0111", "UF-06", "EC-03", "Crédito de consumo", "19/36", 54200, "20-09-2026", EstadoCompromiso.ANULADO, 0, "EJ-04"),
        CompromisoEntity("CP-2026-0112", "UF-07", "EC-06", "Cuota social octubre", "10/12", 15000, "05-10-2026", EstadoCompromiso.PAGADO, 0, "EJ-05")
    )

    // Pagos historicos. Algunos compromisos (de septiembre) no estan en la tabla compromiso
    // a proposito: igual deben verse en el historial (RF-07, RN-07).
    val comprobantes = listOf(
        ComprobanteEntity("CPR-000377", "CP-2026-0072", "UF-02", "EC-01", "02-09-2026 21:14", 318500, CanalPago.AGENTE_CONVERSACIONAL, false, "Pago simulado"),
        ComprobanteEntity("CPR-000398", "CP-2026-0089", "UF-01", "EC-08", "09-09-2026 13:05", 520000, CanalPago.PORTAL, true, "Pago simulado"),
        ComprobanteEntity("CPR-000402", "CP-2026-0090", "UF-01", "EC-06", "07-09-2026 18:40", 17000, CanalPago.PORTAL, false, "Pago simulado"),
        ComprobanteEntity("CPR-000405", "CP-2026-0091", "UF-01", "EC-05", "15-09-2026 08:22", 10000, CanalPago.AGENTE_CONVERSACIONAL, true, "Pago simulado"),
        ComprobanteEntity("CPR-000451", "CP-2026-0104", "UF-01", "EC-03", "24-09-2026 10:42", 86400, CanalPago.PORTAL, true, "Tarjeta de débito"),
        ComprobanteEntity("CPR-000452", "CP-2026-0108", "UF-03", "EC-04", "30-09-2026 19:05", 18900, CanalPago.AGENTE_CONVERSACIONAL, true, "Pago simulado"),
        ComprobanteEntity("CPR-000453", "CP-2026-0112", "UF-07", "EC-06", "04-10-2026 08:15", 15000, CanalPago.APP, true, "Pago simulado"),
        ComprobanteEntity("CPR-000447", "CP-2026-0098", "UF-05", "EC-02", "10-09-2026 22:31", 185000, CanalPago.PORTAL, true, "Pago simulado")
    )

    val reportes = listOf(
        ReporteEntity("REP-0011", "CP-2026-0109", "UF-04", "Ya pagué por otro medio", "Pagué por transferencia directa al instituto el 03-10.", "03-10-2026", EstadoReporte.EN_GESTION, "AG-02", null),
        ReporteEntity("REP-0009", "CP-2026-0111", "UF-06", "El monto no corresponde", "Repacté el crédito y me siguen cobrando la cuota antigua.", "18-09-2026", EstadoReporte.RESUELTO, "AG-01", "La empresa anuló la cuota por repactación (22-09-2026)."),
        ReporteEntity("REP-0007", "CP-2026-0081", "UF-05", "No reconozco este cobro", "Me aparece una matrícula que ya pagué en marzo.", "02-09-2026", EstadoReporte.RESUELTO, "AG-02", "Cobro duplicado anulado por el colegio (05-09-2026)."),
        ReporteEntity("REP-0005", "CP-2026-0064", "UF-01", "Otro", "El concepto dice \"agosto\" y es la cuota de julio.", "28-07-2026", EstadoReporte.RESUELTO, "AG-01", "Se corrigió el concepto; el monto era correcto (30-07-2026)."),
        ReporteEntity("REP-0012", "CP-2026-0106", "UF-02", "El monto no corresponde", "No sé por qué me cobran $318.500.", "05-10-2026", EstadoReporte.ABIERTO, null, null),
        ReporteEntity("REP-0010", "CP-2026-0095", "UF-08", "No reconozco este cobro", "Me llegó un cobro de un club al que no pertenezco.", "30-09-2026", EstadoReporte.ABIERTO, null, null)
    )
}
