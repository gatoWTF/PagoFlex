# PagoFlex

Punto de contacto móvil para una **fintech de recaudación y cobranza**. Las personas (usuarios finales) ven y pagan sus **compromisos de pago** (arriendos, créditos, cuotas, mensualidades) con las **empresas clientes** de PagoFlex, revisan su historial y su cumplimiento, reportan problemas y reciben avisos de vencimiento. La app es un punto de contacto adicional pensado para generar hábito de uso pese a la baja frecuencia (mensual).

Caso **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC). Prototipo funcional (MVP) del *happy path*, desarrollado en **Kotlin + Jetpack Compose** con arquitectura **MVVM** y persistencia local con **Room/SQLite**. Solo usa datos ficticios (Anexo 9).

## Roles (sesión simulada, sin login real)

No hay autenticación real: el rol se deduce del **dominio del correo** al iniciar sesión o registrarse (así nadie se asigna un rol que no le corresponde).

| Rol | Correo de ejemplo | Qué hace |
|---|---|---|
| **Usuario final** | `crojas@correo.ejemplo` (o cualquier correo personal: gmail, hotmail, etc.) | Ve su situación del mes, paga, revisa historial y cumplimiento, reporta problemas, configura avisos, edita su perfil |
| **Ejecutivo de empresa** | `chenriquez@creditoandino.ejemplo` | Ve la cobranza del mes, registra y anula compromisos de su empresa, filtra la lista |
| **Agente PagoFlex** | `alagos@pagoflex.ejemplo` | Gestiona la bandeja de reportes de problema (tomar / resolver) |

Contraseña: cualquiera de **6+ caracteres** (no se valida contra un backend). Un correo genérico (gmail, hotmail, duoc, etc.) entra siempre como **usuario final**.

## Funcionalidades

**Usuario final**
- Situación del mes: total por pagar, próximo vencimiento y vencidos.
- Lista de compromisos y detalle; **pago simulado** con **comprobante** inmutable.
- **Historial** de pagos con filtro e **indicador de cumplimiento** (% de pagos a tiempo).
- **Reportar un problema** sobre un compromiso.
- **Perfil**: datos de cuenta (rol, nombre, RUT, correo) en solo lectura, más correo secundario y teléfono editables, y **foto de perfil con la cámara**.
- **Configuración**: tema claro/oscuro y días de anticipación de los avisos.

**Ejecutivo** — tasa de cobranza del mes, registrar/anular compromisos, filtro (Todos/Vigentes/Anulados), perfil y configuración.

**Agente** — bandeja de reportes con estados (Abierto → En gestión → Resuelto), perfil y configuración.

### Recursos nativos
- **Cámara** — foto de perfil (permiso `CAMERA`, `ActivityResult`).
- **Notificaciones locales** — aviso de vencimiento (permiso `POST_NOTIFICATIONS`).

### Reglas de negocio (extracto)
- Estados del compromiso: Pendiente, Vencido, Pagado, En revisión, Anulado.
- Un compromiso en revisión o anulado no se puede pagar; uno pagado no se vuelve a pagar.
- Montos enteros en pesos chilenos (`$86.400`), fechas `DD-MM-AAAA`, **RUT validado por módulo 11**.
- El rol se deriva del dominio del correo.

## Tecnologías

- **Kotlin 2.2.10** y **Jetpack Compose** (BOM 2026.02.01) con Material 3.
- Arquitectura **MVVM**; estado con `mutableStateOf`, ViewModels y `StateFlow`/`Flow`.
- **Room/SQLite** (con KSP) para la persistencia local; datos semilla del Anexo 9.
- **Navigation Compose** para la navegación entre pantallas y roles.
- **Coroutines/Flow** para leer y escribir en la base de datos.
- Android Gradle Plugin 9.4.1 · minSdk 24 (Android 7.0) · targetSdk 37 · probado en emulador Android 15 (API 35).

## Arquitectura

Patrón **MVVM** por capas: las pantallas (`ui`) solo muestran estado y emiten acciones; la lógica vive en los `viewmodel`; los datos se persisten con Room en `data/local`.

```
app/src/main/java/com/example/pagoflex/
├── MainActivity.kt          NavHost y wiring de la app
├── model/                   enums y modelos de dominio (Compromiso, Rol, EstadoReporte, ...)
├── data/
│   ├── DerivadorDeRol.kt    deduce el rol según el dominio del correo
│   └── local/               Room: PagoFlexDatabase, DAOs, entidades, DatosSemilla
├── viewmodel/               Compromisos, Ejecutivo, Agente, Login, Registro, Sesion, Configuracion
├── ui/
│   ├── screens/             Login, Registro, Inicio, Detalle, Comprobante, Historial,
│   │                        Configuracion, Perfil, ReportarProblema, HomeEjecutivo, HomeAgente
│   ├── components/          BotonPrincipal/Secundario, CampoTexto/Contrasena, FilaCompromiso, ...
│   ├── styles/              EstilosBoton, EstilosCampo, EstilosTarjeta
│   ├── theme/               Color, Dimens, Theme, Type
│   └── navigation/          Rutas
└── utils/                   FormatoMoneda, Validaciones, Notificaciones
```

## Cómo ejecutarla

1. Abre el proyecto con una versión reciente de **Android Studio** (con el SDK de Android instalado).
2. Espera a que termine la sincronización de Gradle.
3. Elige un emulador o celular con **Android 7.0 (API 24) o superior** y presiona **Run**.
4. En el login usa uno de los correos de ejemplo de la tabla de arriba y cualquier contraseña de 6+ caracteres, o crea una cuenta nueva (entrarás como usuario final).

Los datos vienen de la semilla del Anexo 9 y se guardan en Room: los cambios (pagos, reportes, compromisos) **persisten** al cerrar y abrir la app.

## Pruebas

Pruebas unitarias de la lógica (validación de RUT por módulo 11 y asignación de rol por correo):

```bash
./gradlew testDebugUnitTest
```

## Autor

**<Tu Gabriel Antonio Molina Apablaza>** — <Carrera>, Duoc UC
