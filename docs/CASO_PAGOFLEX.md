# PagoFlex — Mapa del caso a la implementación

Documento de referencia para el desarrollo y la defensa. Resume cómo el caso
fintech de cobranza (Caso DSY1105) se traduce a la app Android.

> Avance inicial. Se irá completando a medida que avanza el proyecto.

## 1. El caso en una frase

PagoFlex es una fintech de **recaudación y cobranza**: las personas (usuarios
finales) pagan **compromisos de pago** (arriendo, créditos, mensualidades,
cuotas) a **empresas clientes**. La app es un punto de contacto móvil que les
permite ver su situación, pagar (simulado), guardar comprobantes y generar
hábito de uso.

## 2. Los tres roles (sesión simulada, RF-01)

No hay login real: se elige el rol/usuario al entrar (FA-01).

| Rol | Código | Qué hace en la app |
|-----|--------|--------------------|
| Usuario final | R-01 | Ve su situación, paga, revisa historial, reporta problemas, configura avisos |
| Agente PagoFlex | R-02 | Gestiona los reportes de problema de todos los usuarios |
| Ejecutivo de empresa | R-03 | Registra/anula compromisos de su empresa y ve su cobranza |

Cada rol ve solo lo que le corresponde (RNF-13, RN-22).

## 3. Modelo de datos (Room) ↔ Anexo 8

Entidades en `data/local/entity/` (persistencia local con Room/SQLite):

| Entidad | Tabla | Origen |
|---------|-------|--------|
| `UsuarioFinalEntity` | `usuario_final` | Anexo 8 · usuario final |
| `EmpresaClienteEntity` | `empresa_cliente` | Anexo 8 · empresa cliente |
| `AgenteEntity` | `agente` | Anexo 8 · agente PagoFlex |
| `EjecutivoEntity` | `ejecutivo` | Anexo 8 · ejecutivo |
| `CompromisoEntity` | `compromiso` | Anexo 8 · compromiso de pago |
| `ComprobanteEntity` | `comprobante` | Anexo 8 · pago / comprobante |
| `ReporteEntity` | `reporte` | Anexo 8 · reporte de problema |

Los datos de ejemplo del Anexo 9 se precargan una sola vez desde
`DatosSemilla.kt` (8 usuarios, 8 empresas, 5 agentes, 7 ejecutivos,
12 compromisos, 8 comprobantes, 6 reportes).

## 4. Reglas de negocio clave (a implementar en los ViewModels)

- **RN-01** Estados del compromiso: Pendiente, Vencido, Pagado, En revisión, Anulado.
- **RN-03** Recargo por atraso: total = monto + recargo si está vencido.
- **RN-04** Orden de pago: en una misma empresa se paga primero la cuota más antigua.
- **RN-05** Comprobante inmutable por cada pago.
- **RN-08/09** No se puede pagar un compromiso En revisión o Anulado.
- **RN-10** Indicador de cumplimiento: % de pagos a tiempo en 12 meses.
- **RN-17/18** Validaciones: montos enteros en pesos, fechas DD-MM-AAAA, RUT módulo 11.
- **RN-24** Tasa de cobranza del mes (para el ejecutivo).

## 5. Alcance elegido (RF)

**Usuario final (prioridad):** RF-02 situación del mes · RF-03 lista de
compromisos · RF-04 detalle · RF-05/06 pago simulado + comprobante ·
RF-07 historial · RF-08 cumplimiento · RF-09 reportar problema.

**Hábito (RF-10):** se eligió **RF-11 avisos de vencimiento** (1/3/5 días),
emparejado con notificaciones locales como recurso nativo.

**Roles internos (acotados):** RF-14/15 agente gestiona reportes ·
RF-17/18/19/20 ejecutivo registra/anula compromisos y ve su cobranza.

**Recursos nativos (2):** notificaciones locales (avisos) y cámara/galería.

## 6. Estado de avance

- [x] Persistencia local con Room + datos semilla (Anexo 9)
- [x] Room conectado al flujo del usuario final: lee con Flow y escribe (pagar, reportar) → persiste al cerrar/abrir. Ejecutivo aún en memoria (siguiente paso)
- [x] Sesión simulada / selector de rol (RF-01) — usuario fijo por rol para pruebas
- [x] NavHost (navigation-compose) con un home por rol
- [x] Pago simulado + comprobante (RF-05/06)
- [x] Historial con indicador de cumplimiento (RF-07/08)
- [x] Reportar problema como formulario validado (RF-09) → pasa a En revisión
- [x] Pantallas del usuario final: Inicio, Detalle, Historial, Reportar y Configuración
- [x] Configuración: modo claro/oscuro/sistema (persistente) + ajustes de avisos (RF-11)
- [x] Ejecutivo conectado a Room: registrar y anular persisten
- [ ] Avisos de vencimiento (RF-11): falta la notificación local que los dispara
- [~] Roles internos: ejecutivo con registrar (formulario validado) / anular / cobranza del mes (RF-17/18/20); agente aún placeholder
- [ ] Segundo recurso nativo (cámara/galería)

> Nota de diseño: el selector de rol es la sesión simulada (sin contraseña). Hoy
> deja elegir el rol con un usuario fijo de la semilla, útil para probar. En la
> versión final el rol se declara según el cargo de quien inicia sesión, sin
> permitir elegir la cuenta de otra persona ni ver sus datos.
