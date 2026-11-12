# PagoFlex — Microservicios

Servicios Spring Boot 4.1 (Java 21, Maven, JPA, MySQL) que consume la app con Retrofit.

| Servicio | Puerto | Base de datos | Responsabilidad |
|---|---|---|---|
| `roles-service` | 8081 | `pagoflex_roles` | Catálogo de roles |
| `usuarios-service` | 8082 | `pagoflex_usuarios` | Registro, login, perfil y cambio de contraseña (BCrypt). Le pide el nombre del rol a `roles-service` con WebClient |

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
4. Para detener un servicio: `Ctrl+C` en su pestaña y responder `S`.

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

- La contraseña se guarda con **BCrypt** (nunca en texto plano) y la respuesta nunca la incluye.
- Toda cuenta creada desde la app queda como usuario final (rol 1).
- Al iniciar por primera vez se cargan las personas de la semilla de la app (usuarios finales, ejecutivos y agentes). Su contraseña inicial está en `DatosIniciales.java` (`CONTRASENA_INICIAL`).
- En Spring Boot 4 la propiedad para que el error traiga `"message"` es `spring.web.error.include-message=always` (la antigua `server.error.include-message` ya no funciona).
