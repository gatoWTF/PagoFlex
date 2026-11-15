# PagoFlex — Microservicios

Servicios Spring Boot 4.1 (Java 21, Maven, JPA, MySQL) que consume la app con Retrofit.

| Servicio | Puerto | Base de datos | Responsabilidad |
|---|---|---|---|
| `roles-service` | 8081 | `pagoflex_roles` | Catálogo de roles |
| `usuarios-service` | 8082 | `pagoflex_usuarios` | Registro, login, perfil y cambio de contraseña (BCrypt). Le pide el nombre del rol a `roles-service` con WebClient |
| `compromisos-service` | 8083 | `pagoflex_compromisos` | Compromisos de pago y comprobantes. Cada compromiso guarda el RUT de su dueño (el mismo de su cuenta) |

## Cómo levantarlos (desde Android Studio)

1. Iniciar MySQL en Laragon (puerto 3306, usuario `root` sin contraseña). Las bases se crean solas.
2. En Android Studio, pestaña **Terminal**, desde la carpeta del proyecto:
   ```
   .\microservicios\iniciar-roles.bat
   ```
3. Abrir otra pestaña de Terminal (botón **+**) y ejecutar:
   ```
   .\microservicios\iniciar-usuarios.bat
   ```
4. En una tercera pestaña:
   ```
   .\microservicios\iniciar-compromisos.bat
   ```
5. Para detener un servicio: `Ctrl+C` en su pestaña y responder `S`.

Los scripts usan el Java que trae Android Studio y el Maven Wrapper (`mvnw`) de cada servicio, así que no hace falta instalar Maven ni usar IntelliJ. La primera vez descargan Maven y las librerías (tarda unos minutos).

La app (emulador) llega al PC por `http://10.0.2.2:8082/`.

## Contrato de roles-service (los ids importan)

`usuarios-service` y la app identifican el rol por su **id**. `DatosIniciales` de `roles-service` debe crearlos en este orden:

| id | nombre |
|---|---|
| 1 | Usuario final |
| 2 | Ejecutivo de empresa |
| 3 | Agente PagoFlex |

Endpoints: `GET /api/roles` y `GET /api/roles/{id}` (404 si no existe).

## usuarios-service — `http://localhost:8082`

| Método | Ruta | Cuerpo | Respuesta |
|---|---|---|---|
| GET | `/api/usuarios/{id}` | — | 200 usuario · 404 |
| POST | `/api/usuarios` | `{"nombre","rut","correo","contrasena"}` | 201 · 400 datos o clave débil · 409 correo o RUT repetido |
| POST | `/api/usuarios/login` | `{"correo","contrasena"}` | 200 usuario · 401 credenciales incorrectas |
| PUT | `/api/usuarios/{id}` | `{"nombre","rut","correo"}` | 200 · 404 · 409 |
| PUT | `/api/usuarios/{id}/contrasena` | `{"actual","nueva"}` | 204 · 401 clave actual incorrecta · 400 |

## compromisos-service — `http://localhost:8083`

| Método | Ruta | Cuerpo | Respuesta |
|---|---|---|---|
| GET | `/api/compromisos?rut=...` | — | Compromisos del usuario (el RUT puede ir con o sin puntos) |
| GET | `/api/compromisos?empresa=EC-03` | — | Compromisos de una empresa |
| GET | `/api/compromisos/{folio}` | — | 200 · 404 |
| POST | `/api/compromisos` | `{"rutUsuario","nombreUsuario","empresaCodigo","concepto","totalCuotas","cuotaInicial","monto","fechaVencimiento","registradoPor"}` | 201 con la cuota inicial creada · 400 |
| PUT | `/api/compromisos/{folio}/estado` | `{"estado"}` (`EN_REVISION`, `PENDIENTE` o `VENCIDO`) | 200 · 400 si se pide `ANULADO` o `PAGADO` · 409 si el cambio no está permitido |
| PUT | `/api/compromisos/{folio}/anular` | `{"motivo","comentario","ejecutivoCodigo","empresaCodigo"}` | 200 · 400 sin motivo, u `OTRO` sin comentario · 403 otra empresa · 409 pagado o ya anulado |
| POST | `/api/compromisos/{folio}/pagar` | `{"canal","medioPago"}` (opcional) | 201 `{"comprobante", "siguienteCuota"}` · 409 si no se puede pagar |
| GET | `/api/comprobantes?rut=...` | — | Historial de pagos del usuario |

### Créditos en cuotas

- Cada compromiso es **una cuota** de un crédito. Las cuotas del mismo crédito comparten `codigoCredito` (`CR-2026-0001`, ...) y guardan `cuotaActual` y `totalCuotas` ("8 de 24").
- Al registrar, el ejecutivo indica la cantidad de cuotas, el monto de cada cuota, la cuota inicial y el vencimiento de esa cuota. La cuota inicial es 1 en un crédito nuevo; es mayor si la empresa trae un crédito que ya venía pagándose (las cuotas anteriores no se crean). Debe estar entre 1 y la cantidad de cuotas (máximo 120).
- El servidor crea solo la cuota inicial. Al pagar una cuota crea la siguiente (mismo monto, vencimiento un mes después, folio nuevo) y la devuelve en `siguienteCuota`. Si era la última, `siguienteCuota` viene `null`.
- El folio (`CP-2026-0115`, ...), el código de crédito y el número de comprobante los genera el servidor, siguiendo al último guardado.
- Cambios de estado permitidos: pendiente o vencido → en revisión; en revisión → pendiente o vencido. Para pagar se usa `/pagar` y para anular `/anular`.

### Vencimiento y recargo (RN-02, RN-03)

- Una cuota vence a las 23:59 de su fecha. Desde el día siguiente pasa sola a **Vencido** y se le suma **una sola vez** el recargo fijo de su empresa (tabla `empresas`, columna `recargo_atraso`; las ONG tienen $0).
- La revisión la hace `VencimientoService`: todos los días a las 00:01 (`@Scheduled`) y además antes de cada consulta o pago, así la app siempre ve el estado al día.
- Al pagar se cobra monto + recargo, y el comprobante marca `aTiempo` comparando la fecha de pago con la de vencimiento.
- Si un agente devuelve a pendiente una cuota en revisión cuya fecha ya pasó, queda Vencido con su recargo.

### Anulación (RN-21)

- Solo la empresa dueña puede anular, desde Pendiente, Vencido o En revisión. Un compromiso pagado nunca se anula.
- Motivo obligatorio: `REPACTACION`, `ERROR_DE_REGISTRO`, `PAGADO_DIRECTAMENTE` u `OTRO` (este último exige comentario). Se guardan el motivo, el comentario, el ejecutivo y la fecha.
- No existe `DELETE` de compromisos ni de comprobantes: en una fintech no se borran deudas ni pagos (RN-05: un comprobante nunca se modifica ni se elimina). Anular es un borrado lógico que deja la trazabilidad.

## Notas de usuarios-service

- La contraseña se guarda con **BCrypt** (nunca en texto plano) y la respuesta nunca la incluye.
- Toda cuenta creada desde la app queda como usuario final (rol 1).
- Al iniciar por primera vez se cargan las personas de la semilla de la app (usuarios finales, ejecutivos y agentes). Su contraseña inicial está en `DatosIniciales.java` (`CONTRASENA_INICIAL`).
- En Spring Boot 4 la propiedad para que el error traiga `"message"` es `spring.web.error.include-message=always` (la antigua `server.error.include-message` ya no funciona).
