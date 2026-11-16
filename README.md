# PagoFlex

Punto de contacto móvil para una **fintech de recaudación y cobranza**. Las personas (usuarios finales) ven y pagan sus **compromisos de pago** (arriendos, créditos, cuotas, mensualidades) con las **empresas clientes** de PagoFlex, revisan su historial y su cumplimiento, reportan problemas y reciben avisos de vencimiento. La app es un punto de contacto adicional pensado para generar hábito de uso pese a la baja frecuencia (mensual).

Caso **DSY1105 – Desarrollo de Aplicaciones Móviles** (Duoc UC). App **Kotlin + Jetpack Compose** con arquitectura **MVVM**, conectada por **Retrofit** a **3 microservicios Spring Boot** que guardan los datos en **MySQL**. Solo usa datos ficticios (Anexo 9).

## Cómo está armado

```
┌──────────────────────────┐   Retrofit (HTTP/JSON)   ┌───────────────────────────┐
│  App Android             │ ───────────────────────► │  usuarios-service  :8082  │──► MySQL pagoflex_usuarios
│  Kotlin + Compose (MVVM) │                          │        │ WebClient        │
│                          │                          │        ▼                  │
│  Room (local): empresas, │                          │  roles-service     :8081  │──► MySQL pagoflex_roles
│  perfiles y reportes     │ ───────────────────────► │  compromisos-service :8083│──► MySQL pagoflex_compromisos
└──────────────────────────┘                          └───────────────────────────┘
```

- **usuarios-service** (8082): registro, login, editar perfil y cambiar contraseña. Las contraseñas se guardan con **BCrypt**. Le pregunta el nombre del rol a roles-service.
- **roles-service** (8081): catálogo de roles (1 Usuario final, 2 Ejecutivo de empresa, 3 Agente PagoFlex).
- **compromisos-service** (8083): compromisos (cuotas de cada crédito), pagos, comprobantes, vencimientos y anulaciones.
- **Room** (en el teléfono): nombres de las empresas, datos propios de cada rol (empresa del ejecutivo, código del agente) y los reportes de problema.

El detalle de cada endpoint está en [`microservicios/README.md`](microservicios/README.md).

## Roles

El login es real: el correo y la contraseña se verifican en **usuarios-service**, y el rol viene del servidor (no se deduce del correo). Si los datos no coinciden, la app muestra un mensaje genérico sin decir qué campo falló. Toda cuenta creada desde **Crear cuenta** queda como usuario final.

| Rol | Cuenta de ejemplo | Qué hace |
|---|---|---|
| **Usuario final** | `crojas@correo.ejemplo` | Ve su situación del mes, paga, revisa historial y cumplimiento, reporta problemas, edita su perfil |
| **Ejecutivo de empresa** | `chenriquez@creditoandino.ejemplo` | Ve la cobranza de su empresa, registra créditos en cuotas y anula compromisos con motivo |
| **Agente PagoFlex** | `alagos@pagoflex.ejemplo` | Gestiona la bandeja de reportes de problema (tomar / resolver) |

Las cuentas de la semilla se crean solas la primera vez que se enciende usuarios-service. Su contraseña inicial está en `microservicios/usuarios-service/src/main/java/com/pagoflex/usuarios/config/DatosIniciales.java` (`CONTRASENA_INICIAL`).

## Funcionalidades

**Usuario final**
- Situación del mes: total por pagar, próximo vencimiento y cantidad de vencidos.
- Lista de compromisos y detalle con la cuota ("8 de 24"); **pago simulado** con **comprobante**. Al pagar una cuota el sistema genera y muestra la siguiente.
- **Historial** de pagos con filtro e **indicador de cumplimiento** (% de pagos a tiempo).
- **Reportar un problema** sobre un compromiso (queda En revisión hasta que un agente lo resuelva).
- **Perfil** con **foto de la cámara**, **editar nombre y correo** y **cambiar contraseña**.
- **Avisos de vencimiento**: al entrar, la app pide el permiso de notificaciones explicando para qué sirve.
- **Configuración**: tema claro/oscuro y días de anticipación de los avisos.

**Ejecutivo**: cobranza del mes de su empresa, **registrar créditos** (cantidad de cuotas, monto, cuota inicial y vencimiento con calendario), **anular** con motivo, filtro (Todos / Vigentes / Anulados), perfil y configuración.

**Agente**: bandeja de reportes (Abierto → En gestión → Resuelto); al resolver, el compromiso vuelve a Pendiente o Vencido. Perfil y configuración.

### Recursos nativos
- **Cámara**: foto de perfil (permiso `CAMERA`).
- **Notificaciones locales**: aviso de vencimiento (permiso `POST_NOTIFICATIONS`, Android 13+).

### Reglas de negocio implementadas
- **Estados**: Pendiente, Vencido, Pagado, En revisión, Anulado. Uno en revisión o anulado no se paga; uno pagado no se vuelve a pagar.
- **RN-02 / RN-03**: al día siguiente de su fecha, una cuota pasa sola a **Vencido** y se le suma **una vez** el recargo fijo de su empresa (las ONG tienen $0).
- **RN-05**: un comprobante nunca se modifica ni se elimina.
- **RN-20**: la fecha de vencimiento no puede ser anterior a hoy, y la misma cuota (persona + empresa + concepto + número de cuota) no se registra dos veces.
- **RN-21**: solo el ejecutivo de la empresa dueña anula, con motivo obligatorio (Repactación, Error de registro, Pagado directamente a la empresa u Otro con comentario). Un compromiso pagado nunca se anula.
- **Validaciones**: RUT por módulo 11, nombre sin números, correo con formato válido, contraseña de 6+ caracteres con mayúscula, minúscula y número (en el registro), montos enteros en pesos chilenos (`$86.400`) y fechas `DD-MM-AAAA`.

## Tecnologías

**App**
- **Kotlin 2.2.10** y **Jetpack Compose** (BOM 2026.02.01) con Material 3.
- Arquitectura **MVVM**: estado con `mutableStateOf` en los ViewModels.
- **Retrofit 3.0.0** + Gson para consumir los microservicios.
- **Room 2.8.1** (con KSP) para los datos locales.
- **Navigation Compose**, **Coroutines**.
- Android Gradle Plugin 9.4.1 · minSdk 24 (Android 7.0) · targetSdk 35.

**Microservicios**
- **Spring Boot 4.1** con **Java 21** y Maven Wrapper (no hace falta instalar Maven).
- **Spring Data JPA** + **MySQL 8.4** (Laragon). Las tablas se crean solas.
- **BCrypt** (`spring-security-crypto`) para las contraseñas y **WebClient** para que usuarios-service consulte a roles-service.
- Capas por servicio: `controller` → `service` → `repository` → `model` (entidades) y `dto`.

## Estructura

```
app/src/main/java/com/example/pagoflex/
├── MainActivity.kt          NavHost y conexión entre pantallas y ViewModels
├── model/                   modelos y enums (Compromiso, Usuario, RolUsuario, MotivoAnulacion, ...)
├── data/
│   ├── remote/              Retrofit: RetrofitClient, UsuarioApi, CompromisoApi y sus modelos JSON
│   ├── local/               Room: PagoFlexDatabase, DAOs, entidades, DatosSemilla
│   └── *Repository.kt       UsuarioRepository y CompromisoRepository (llaman a la API)
├── viewmodel/               Login, Registro, Sesion, EditarPerfil, CambiarContrasena,
│                            Compromisos, Ejecutivo, Agente, Configuracion
├── ui/
│   ├── screens/             Login, Registro, Inicio, Detalle, Comprobante, Historial, Perfil,
│   │                        EditarPerfil, CambiarContrasena, ReportarProblema, Configuracion,
│   │                        HomeEjecutivo, RegistrarCompromiso, HomeAgente
│   ├── components/          botones, campos, FilaCompromiso, Snackbar, ...
│   ├── styles/ · theme/     estilos, colores, tipografía y medidas
│   └── navigation/          Rutas
└── utils/                   Validaciones, FormatoMoneda, Notificaciones

microservicios/
├── roles-service/           :8081
├── usuarios-service/        :8082
├── compromisos-service/     :8083
└── iniciar-*.bat            un script por servicio
```

## Cómo ejecutarla

1. Abre **Laragon** y presiona **Iniciar todo** (MySQL en el puerto 3306, usuario `root` sin contraseña). Las bases se crean solas.
2. Enciende los 3 microservicios con un solo comando en la **Terminal** de Android Studio (PowerShell):
   ```powershell
   Get-ChildItem .\microservicios\iniciar-*.bat | ForEach-Object { Start-Process $_.FullName }
   ```
   Se abren 3 ventanas, una por servicio. Cada uno está listo cuando su ventana muestra `Started ...Application`. Para apagarlos, cierra las ventanas. La primera vez tardan unos minutos porque descargan las librerías.
3. Abre el proyecto en **Android Studio**, espera la sincronización de Gradle y ejecuta la app (**Run**) en un emulador con Android 7.0 (API 24) o superior.
4. Entra con una cuenta de ejemplo de la tabla de Roles, o crea una cuenta nueva.

La app llega a los servicios por `http://10.0.2.2` (la dirección del PC vista desde el emulador). Para usar un teléfono real hay que cambiar esa dirección en `data/remote/RetrofitClient.kt` por la IP del PC en la red.

## Pruebas

**Pruebas unitarias** (JUnit, sin emulador), con el patrón preparar / ejecutar / verificar:

| Archivo | Qué prueba |
|---|---|
| `utils/ValidacionesTest` | RUT (módulo 11), montos, formato de fecha y fecha no anterior a hoy |
| `viewmodel/LoginViewModelTest` | campos vacíos y que el login no valide el formato del correo |
| `viewmodel/RegistroViewModelTest` | formulario vacío, nombre con números, RUT inválido, contraseña sin mayúscula y datos correctos |
| `viewmodel/EditarPerfilViewModelTest` | nombre con números y correo inválido |
| `viewmodel/SesionViewModelTest` | datos de la cuenta al iniciar sesión, foto de perfil y mensajes |

```powershell
.\gradlew testDebugUnitTest
```

También desde Android Studio: clic derecho en la carpeta `test` → **Run 'Tests in…'**.

**Prueba instrumentada** (necesita un emulador encendido): `SesionFotoInstrumentedTest`, que guarda la foto de perfil (usa `Bitmap`, que solo existe en Android).

```powershell
.\gradlew connectedDebugAndroidTest
```

## Autor

Gabriel Antonio Molina Apablaza — <Carrera>, Duoc UC
