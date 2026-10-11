# Rúbricas de evaluación — PagoFlex (DSY1105)

Referencia extraída de las pautas oficiales del profesor:
- `Ev. Parcial 2_Estudiante_Encargo_DSY1105.pdf`
- `Evaluación Parcial 3_Estudiante_Presentación_DSY1105.pdf`

> Reglas críticas (ambas evaluaciones):
> - **Si la app no se ejecuta en la defensa, todos los ítems posteriores al error = 0.**
> - **Cualquier commit después de la fecha de entrega = 1.0 automático** (grupal e individual). Congelar el repo.
> - Entrega en GitHub público (con acceso al docente) + AVA + Trello. Activar el enlace individual de AVA o no hay derecho a defensa.

---

## Parcial 2 — Encargo (grupal, 8%)

Niveles de logro: **100%** muy buen desempeño · **80%** buen desempeño · **60%** aceptable · **30%** incipiente · **0%** no logrado.

| Indicador | Peso | Qué evalúa |
|-----------|------|-----------|
| **IE 2.1.1** | 15% | Interfaz visual coherente, jerárquica, con navegación funcional entre vistas y usabilidad |
| **IE 2.1.2** | 15% | Formularios completos con validaciones visuales por campo, íconos y mensajes (niveles por % de formularios cubiertos) |
| **IE 2.2.1** | 10% | Lógica de validación centralizada y **desacoplada** de la UI, respondiendo a cambios de estado |
| **IE 2.2.2** | 10% | Animaciones visuales funcionales que aportan fluidez y retroalimentación |
| **IE 2.3.1** | 15% | Estructura modular (MVVM, separación de responsabilidades) + **persistencia local funcionando** |
| **IE 2.3.2** | **20%** | GitHub + Trello: commits distribuidos, issues, participación grupal (indicador de mayor peso) |
| **IE 2.4.1** | 15% | Acceso a **al menos 2 recursos nativos** del dispositivo, integrados con coherencia en la UI |

Requisitos de entrega: carpeta del proyecto, `README.md` (descripción, nombres, funcionalidades, pasos para ejecutar), commits técnicos y distribuidos, Trello con reparto de tareas.

Bloque temático más pesado: **formularios + validación = 25%** (IE 2.1.2 + IE 2.2.1). Indicador individual más pesado: **Git/Trello 20%**.

---

## Parcial 3 — Presentación / defensa (individual, 32%)

Niveles: **100%** muy buen desempeño · **60%** aceptable · **30%** incipiente · **0%** no logrado.

| Indicador | Qué evalúa |
|-----------|-----------|
| **IE 2.1.1** | Explica la validación de formularios y es capaz de integrar condiciones nuevas / corregir errores en vivo |
| **IE 2.1.2** | Dominio técnico del diseño visual, estructura de interfaces y navegación (ejecutando en vivo) |
| **IE 2.2.1** | Demuestra modularidad incorporando una clase/módulo/componente/animación nueva en vivo |
| **IE 2.2.2** | Explica estructuras de control, gestión de estado y funcionalidades visuales |
| **IE 2.3.1** | Demuestra implementación o **modificación** de persistencia local en vivo |
| **IE 2.3.2** | Explica la arquitectura (MVVM) y justifica GitHub / Trello |
| **IE 2.4.1** | Explica la integración de recursos nativos: permisos, flujos y medidas |
| **IE 2.4.2** | Demuestra en tiempo real el recurso nativo dentro de la UI |

### Nota clave sobre "modificar en vivo"
Varios indicadores piden modificar código a pedido del profe (agregar un campo/validación, crear un Composable, etc.). Si NO logras la modificación pero **explicas correctamente**, caes en el nivel **"aceptable (60%)"**, no en el máximo:
- **IE 2.1.1 (60%)**: describe la lógica en general, pero "sin demostrar capacidad de modificación durante la defensa".
- **IE 2.3.1 (60%)**: explica la función, pero "presenta dificultad para demostrar la modificación".

El **100%** en esos indicadores exige realizar la modificación en vivo. Explicar bien es la red de seguridad que evita el 0%.
