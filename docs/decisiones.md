# Decisiones · Extensión 5 (Ventanas de mantenimiento) · Regla C

Nazareno Masetto · Ingeniería de Software Aplicada · UM 2026

## Qué se implementó

- **Regla C:** `GET /api/oncall/guardia/{rotacionId}?instante=...` devuelve quién está de guardia en una rotación en un instante dado.
- **Extensión 5:** `GET /api/oncall/mantenimiento/{servicioId}?instante=...` indica si un servicio está dentro de una ventana de mantenimiento, y si hay que suprimir alertas y/o escalamiento.
- Si no se manda `instante`, se usa el momento actual. El formato es ISO-8601 (`2026-10-30T20:00:00Z`).

## Dónde vive el código

Todo el negocio está en paquetes `oncall/` (`repository/oncall`, `service/oncall`, `web/rest/oncall`, y sus tests). Ningún archivo generado por JHipster fue modificado: los servicios propios **componen** los repositorios generados en vez de editarlos. Así, regenerar desde `oncall.jh` no borra la regla.

La lógica temporal está separada en dos clases puras, sin base de datos (`ResolutorDeGuardia` y `CalendarioDeVentana`). Los servicios solo buscan los datos y les delegan la decisión. Eso permite probar los casos borde con tests unitarios simples.

## Casos borde: qué decidí y por qué

**1. Límites del turno.** Un turno cubre el intervalo semiabierto `[desde, hasta)`. Si un turno termina a las 09:00 y otro empieza a las 09:00, a las 09:00 en punto está de guardia solo el segundo. Así dos turnos consecutivos nunca se superponen por un instante ni dejan un hueco.

**2. Huecos sin cobertura.** No es un error: la respuesta es `200` con `cubierto: false`, `motivo: "SIN_COBERTURA"` y la lista de responsables vacía. Quien consume la regla (por ejemplo, el escalamiento) decide qué hacer. Lanzar una excepción haría que un hueco, que es una situación esperable, rompa el flujo.

**3. Turnos solapados.** Se devuelven **todos** los que cubren el instante. El **primario** es el que empezó más tarde (el que acaba de tomar la guardia); el resto quedan como **secundarios**. Ejemplo: uno termina 09:00 y otro empieza 08:30; a las 08:45 el primario es el entrante y el saliente queda de respaldo. Esto también resuelve "primario y secundario simultáneos" sin agregar un campo de rol al modelo.

**4. Reemplazos.** Si algún turno que cubre el instante tiene `esReemplazo = true`, **pisa** a los turnos regulares: solo se devuelven los reemplazos. Fuera del horario del reemplazo, vuelve a mandar el turno regular.

**5. Rotación inactiva.** Responde `cubierto: false` con `motivo: "ROTACION_INACTIVA"`, aunque haya turnos cargados.

**6. Rotación o servicio inexistente.** `404`. A diferencia de un hueco, eso sí es un pedido inválido.

**7. Zonas horarias.** Los turnos se guardan como `Instant` (UTC), así que el instante en que alguien está de guardia no es ambiguo. La respuesta incluye además la hora local en la `zonaHoraria` de la rotación, para que una persona la lea sin hacer cuentas. Una zona horaria inválida se trata como UTC.

## Ventanas de mantenimiento (la vuelta de tuerca)

Una ventana recurrente es el mismo problema que una rotación: un patrón en hora local que se convierte en intervalos concretos.

- `inicio`/`fin` definen la **primera ocurrencia**; su duración se repite en cada una.
- **La hora se mantiene en hora local** de `zonaHoraria`. "Todos los martes de 02:00 a 04:00" sigue siendo 02:00 local después de un cambio de horario de verano, aunque en UTC se corra una hora. Lo contrario (fijar la hora en UTC) haría que el mantenimiento se mueva solo dos veces al año.
- Recurrencia: `NINGUNA` (una sola vez), `DIARIA`, `SEMANAL` (días en `diasSemana`, por ejemplo `TUE` o `MAR,JUE`; vacío = el mismo día que la primera ocurrencia) y `MENSUAL` (mismo día del mes; si ese día no existe, como el 31 en febrero, ese mes no hay ventana). Se deja de repetir después de `recurrenciaHasta`.
- Una ventana que cruza la medianoche (23:00 a 01:00) pertenece al día en que **empieza**.
- **Excepciones** (se identifican por la fecha local de inicio de la ocurrencia):
  - `CANCELACION`: esa ocurrencia no existe.
  - `REPROGRAMACION`: esa ocurrencia se reemplaza por `[nuevoInicio, nuevoFin)`, aunque caiga en otro día.
  - `EXTENSION`: la ocurrencia termina en `nuevoFin` en vez de su fin normal.
- Una ventana con `activa = false` nunca está activa.
- Si un servicio está en **varias ventanas a la vez**, se suprime lo que pida **cualquiera** de ellas (criterio conservador: si alguien declaró mantenimiento, no se despierta a nadie por eso).

## Cómo lo verifiqué

Tests unitarios en `src/test/java/.../service/oncall/`, uno por caso borde:

- `ResolutorDeGuardiaTest`: turno único, fin del turno exacto, hueco, solapamiento (primario = entrante), reemplazo que pisa al regular y devuelve el control al terminar.
- `CalendarioDeVentanaTest`: ocurrencia semanal, otro día de la semana, **cambio de horario en Madrid el 25/10/2026** (la ventana sigue en 02:00 local), cancelación, extensión, reprogramación, `recurrenciaHasta`, ventana que cruza la medianoche y ventana desactivada.

Se corren con `./mvnw test -Dtest='ResolutorDeGuardiaTest,CalendarioDeVentanaTest'`.

Además, probé los endpoints con la app levantada y datos cargados a mano (una rotación con un turno de hoy) desde la documentación de la API (Administración → API).

## Lo que queda afuera

- Ventanas generadas a partir de una rotación (por ejemplo, "durante la guardia de X"): no se modeló.
- Aplicar la supresión al ingresar alertas: corresponde a la regla A (deduplicación), que consumirá `MantenimientoService`.
