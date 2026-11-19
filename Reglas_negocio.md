# Sistema de Gestión de Hotel: Reglas de negocio

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

Las reglas de negocio describen cómo opera realmente un hotel y son la base de los requisitos funcionales (RF). Cada regla indica el RF donde se aplica.

Estados de una reservación: ACTIVA (confirmada, sin check-in), EN_CURSO (con check-in realizado), FINALIZADA (con check-out realizado) y CANCELADA.

Las marcas `[DECIDIR D-xx]` señalan reglas con una propuesta por defecto que el equipo debe confirmar o cambiar; todas se resumen en la sección 9. Al decidir, eliminen la marca. Las reglas RN-40 en adelante son nuevas y conservan los números originales de RN-01 a RN-39.

## 1. Reservaciones

| ID | Regla | RF |
|---|---|---|
| RN-01 | Una reservación solo puede hacerse para un huésped que esté registrado y activo, y con todas las habitaciones disponibles (RN-40). | RF-05 |
| RN-02 | Una habitación no puede reservarse dos veces en fechas que se traslapan. Dos rangos se traslapan si `entrada1 < salida2` y `entrada2 < salida1`: la salida de una reservación y la entrada de otra pueden caer el mismo día. Solo cuentan las reservaciones ACTIVA y EN_CURSO. | RF-05, RF-07 |
| RN-03 | Si alguna de las habitaciones solicitadas no está disponible, se rechaza toda la reservación; no puede quedar una reservación parcial. | RF-05 |
| RN-04 | Todas las habitaciones de una reservación comparten la misma fecha de entrada y de salida. | RF-05 |
| RN-05 | El número de huéspedes de una reservación no puede superar la capacidad total de sus habitaciones. | RF-05 |
| RN-06 | La fecha de salida debe ser posterior a la fecha de entrada (mínimo una noche). | RF-05, RF-07 |
| RN-07 | Solo se pueden modificar las fechas de una reservación ACTIVA y, al hacerlo, se vuelven a validar disponibilidad, traslapes (sin contar la propia reservación) y fechas. | RF-07 |
| RN-08 | Una reservación solo se puede cancelar mientras esté ACTIVA (sin check-in). Una reservación EN_CURSO, FINALIZADA o CANCELADA no se puede cancelar. | RF-10 |
| RN-09 | Si la cancelación se hace con menos de `HORAS_MINIMAS_CANCELACION` horas de anticipación, se cobra `CARGO_CANCELACION`, que queda registrado en la reservación. La anticipación se mide entre el momento de la cancelación y la fecha de entrada a las `HORA_CHECK_IN`. El cargo solo se registra; no genera factura. `[DECIDIR D-02]` | RF-10 |
| RN-10 | La cancelación aplica a la reservación completa y el cargo por cancelación se cobra una sola vez. | RF-10 |
| RN-40 | Una habitación está disponible para un rango de fechas si está activa, no está en MANTENIMIENTO (RN-14) y no tiene traslape (RN-02). Su estado actual (OCUPADA, LIMPIEZA) no impide reservar fechas futuras. | RF-05, RF-06, RF-07 |
| RN-41 | La fecha de entrada no puede ser anterior a la fecha actual, tanto al crear como al modificar una reservación. `[DECIDIR D-01]` | RF-05, RF-07 |

## 2. Habitaciones y su estado

| ID | Regla | RF |
|---|---|---|
| RN-11 | El número de habitación es único dentro del hotel. | RF-03 |
| RN-12 | El estado de una habitación es uno de: DISPONIBLE, OCUPADA, LIMPIEZA o MANTENIMIENTO. Toda habitación nueva inicia DISPONIBLE. | RF-03, RF-07 |
| RN-13 | El check-in solo puede hacerse sobre una reservación ACTIVA: la reservación pasa a EN_CURSO y todas sus habitaciones a OCUPADA. El check-out solo puede hacerse sobre una reservación EN_CURSO: la reservación pasa a FINALIZADA y sus habitaciones a LIMPIEZA. | RF-11 |
| RN-14 | Una habitación en MANTENIMIENTO no puede ofrecerse en la búsqueda de disponibilidad ni reservarse. | RF-05, RF-06 |
| RN-15 | No se puede dar de baja una habitación que tenga reservaciones activas (en estado ACTIVA o EN_CURSO). | RF-09 |
| RN-42 | El estado OCUPADA solo lo asigna el check-in y solo lo libera el check-out. No se puede asignar OCUPADA manualmente ni cambiar manualmente el estado de una habitación OCUPADA. | RF-07, RF-11 |
| RN-43 | El check-in exige que todas las habitaciones de la reservación estén DISPONIBLES; si alguna está en LIMPIEZA, MANTENIMIENTO u OCUPADA, se rechaza todo el check-in. No se valida que la fecha actual coincida con la fecha de entrada. `[DECIDIR D-03]` | RF-11 |
| RN-44 | El check-in y el check-out los realiza un recepcionista activo. | RF-11 |

## 3. Tarifas y costos

Todos los montos están en pesos mexicanos. Los valores de las constantes están en la sección 8.

| ID | Regla | RF |
|---|---|---|
| RN-16 | La tarifa de hospedaje depende del tipo de habitación: la sencilla cobra `tarifaBase × noches`. | RF-13 |
| RN-17 | La habitación doble cobra `(tarifaBase + CARGO_CAMA_EXTRA × (numCamas - 1)) × noches`; es decir, suma `CARGO_CAMA_EXTRA` por noche por cada cama adicional a la primera. | RF-13 |
| RN-18 | La suite cobra `(tarifaBase + cargoExtra) × noches`, donde `cargoExtra` es un cargo fijo por noche propio de cada suite. | RF-13 |
| RN-19 | El costo de un servicio depende de su tipo: el restaurante cobra `precioBase × platillos`. | RF-12 |
| RN-20 | El spa cobra `precioBase × (minutos / MINUTOS_BASE)`; es decir, `precioBase` es el precio de una sesión de `MINUTOS_BASE` minutos. `[DECIDIR D-04]` | RF-12 |
| RN-21 | La lavandería cobra `precioBase × prendas` y, si el servicio es exprés, multiplica ese monto por `(1 + RECARGO_EXPRES)`, donde `RECARGO_EXPRES` es un porcentaje. `[DECIDIR D-05]` | RF-12 |
| RN-45 | Cambiar la tarifa de una habitación o el precio de un servicio no modifica las facturas ya generadas, porque cada `DetalleFactura` conserva concepto, cantidad y monto. | RF-07, RF-13 |

## 4. Consumos, facturación y pago

| ID | Regla | RF |
|---|---|---|
| RN-22 | Los consumos de servicios solo pueden registrarse dentro de una reservación EN_CURSO, sobre un servicio activo del catálogo y antes de que se genere la factura (RN-46). | RF-12 |
| RN-23 | Un consumo no existe fuera de su reservación: si la reservación desaparece, sus consumos también. | RF-12 |
| RN-24 | El subtotal de la factura suma el hospedaje de cada habitación (`calcularCosto(noches)`) y los consumos (`calcularCosto(cantidad)` de cada servicio). | RF-13 |
| RN-25 | Cada concepto de la factura (habitación y servicios) es un `Facturable` y genera un `DetalleFactura`. | RF-13 |
| RN-26 | Los huéspedes de tipo VIP reciben `DESCUENTO_VIP` en su factura. `Factura.descuento` guarda el porcentaje aplicado según el tipo del huésped al momento de generarla (0 si es REGULAR). | RF-13 |
| RN-27 | El check-out de una reservación EN_CURSO solo puede realizarse si la factura de la reservación ya fue generada y está pagada; en caso contrario se rechaza. | RF-11, RF-13, RF-15 |
| RN-46 | La factura se genera una sola vez, sobre una reservación EN_CURSO; después ya no se aceptan consumos. El pago se registra sobre la factura generada y una factura pagada no puede pagarse otra vez. `[DECIDIR D-07]` | RF-12, RF-13, RF-15 |
| RN-47 | El total de la factura es `(subtotal - subtotal × descuento) × (1 + IMPUESTO)`: primero se aplica el descuento VIP y después el impuesto sobre el monto con descuento. `[DECIDIR D-06]` | RF-13 |

## 5. Personal y nómina

| ID | Regla | RF |
|---|---|---|
| RN-28 | El sueldo de cada empleado depende de su rol: el recepcionista cobra su salario base. | RF-14 |
| RN-29 | La camarista cobra `salarioBase + habitacionesLimpiadas × PAGO_POR_HABITACION`. `[DECIDIR D-08]` | RF-14 |
| RN-30 | El gerente cobra su salario base más un bono mensual. | RF-14 |
| RN-31 | El mesero cobra su salario base más las propinas acumuladas. | RF-14 |
| RN-32 | La nómina solo incluye a los empleados activos. | RF-14 |
| RN-48 | La limpieza solo puede registrarse sobre una habitación en LIMPIEZA y por una camarista activa: la habitación pasa a DISPONIBLE y `habitacionesLimpiadas` de la camarista aumenta en 1. | RF-16 |
| RN-49 | Las propinas se registran sobre un mesero activo con un monto positivo y se acumulan en `propinasAcumuladas`. | RF-17 |
| RN-50 | La nómina es un cálculo de consulta: no reinicia los contadores de limpiezas ni de propinas. `[DECIDIR D-09]` | RF-14 |

## 6. Altas, bajas y datos

| ID | Regla | RF |
|---|---|---|
| RN-33 | El código de cada servicio del catálogo es único. | RF-04 |
| RN-34 | Ningún elemento se elimina físicamente: la baja es lógica (`activo = false`) para conservar el historial de reservaciones, facturas y nómina. | RF-08 |
| RN-35 | Las consultas de huéspedes, empleados, habitaciones y servicios muestran solo elementos activos. Las reservaciones se consultan con su estado, incluidas las FINALIZADAS y CANCELADAS. | RF-06 |
| RN-36 | Del huésped solo puede actualizarse su tipo (REGULAR o VIP). | RF-07 |
| RN-37 | La baja de una reservación es su cancelación (estado CANCELADA), no se borra. | RF-10 |
| RN-38 | Un servicio dado de baja deja de ofrecerse en el catálogo, pero sus consumos anteriores se conservan en las facturas. | RF-08 |
| RN-39 | La clase `Hotel` es la única que gestiona las colecciones de habitaciones, empleados, huéspedes, servicios y reservaciones; las demás clases validan sus propios estados. | RF-01 a RF-17 |
| RN-51 | El id de huésped y el id de empleado son únicos dentro del hotel. | RF-01, RF-02 |
| RN-52 | El id de reservación y el folio de factura los genera el sistema y son únicos. | RF-05, RF-13 |
| RN-53 | Los textos obligatorios no pueden estar vacíos; salarios, tarifas, precios, capacidad, número de camas, número de huéspedes y cantidades deben ser positivos. | RF-01 a RF-07, RF-12 |
| RN-54 | Del empleado solo puede actualizarse su salario base; de la habitación, su estado (RN-42) y su tarifa base; del servicio, su precio base. | RF-07 |
| RN-55 | No se puede dar de baja a un huésped que tenga reservaciones ACTIVA o EN_CURSO. `[DECIDIR D-10]` | RF-08, RF-09 |
| RN-56 | Un elemento dado de baja no participa en nuevas operaciones (no se reserva su habitación, no se registra su consumo, no cobra nómina) y se trata como no encontrado, pero se conserva en el historial. | RF-05, RF-08, RF-12, RF-14 |

## 7. Reglas y excepciones

| Excepción | Cuándo se lanza | Reglas |
|---|---|---|
| `RegistroDuplicadoException` | Número de habitación, código de servicio o id de huésped o empleado repetido. | RN-11, RN-33, RN-51 |
| `RegistroNoEncontradoException` | Id, número, código o reservación inexistente o dado de baja. | RN-01, RN-56 |
| `ReservacionInvalidaException` | Fechas inválidas o pasadas, traslape, habitación no disponible, capacidad excedida, operación no permitida para el estado de la reservación, consumo fuera de EN_CURSO o con factura generada. | RN-02, RN-03, RN-05, RN-06, RN-07, RN-08, RN-22, RN-40, RN-41, RN-43, RN-46 |
| `BajaNoPermitidaException` | Baja de habitación o huésped con reservaciones ACTIVA o EN_CURSO. | RN-15, RN-55 |
| `PagoPendienteException` | Check-out sin factura generada o sin pagar. | RN-27 |
| `HotelException` | Excepción base; se lanza directamente en reglas generales: estado OCUPADA manual, recepcionista inactivo, limpieza sobre habitación que no está en LIMPIEZA, propina inválida, pago repetido. | RN-42, RN-44, RN-46, RN-48, RN-49 |
| `IllegalArgumentException` | Dato inválido en constructores y setters (no verificada). | RN-53 |

## 8. Constantes del sistema

Los valores son propuestas del equipo; confirmen o cambien según D-02, D-04, D-05, D-06 y D-08.

| Constante | Clase | Valor propuesto | Regla |
|---|---|---|---|
| `HORAS_MINIMAS_CANCELACION` | `Reservacion` | 48 horas | RN-09 |
| `CARGO_CANCELACION` | `Reservacion` | 500.00 | RN-09 |
| `HORA_CHECK_IN` | `Reservacion` | 15:00 | RN-09 |
| `CARGO_CAMA_EXTRA` | `HabitacionDoble` | 300.00 por noche | RN-17 |
| `MINUTOS_BASE` | `ServicioSpa` | 60 minutos | RN-20 |
| `RECARGO_EXPRES` | `ServicioLavanderia` | 0.25 (25 %) | RN-21 |
| `DESCUENTO_VIP` | `Huesped` | 0.10 (10 %) | RN-26 |
| `IMPUESTO` | `Factura` | 0.16 (16 %) | RN-47 |
| `PAGO_POR_HABITACION` | `Camarista` | 50.00 | RN-29 |

## 9. Decisiones por confirmar

|Decision| ID | Tema | Propuesta por defecto | Alternativa | Regla |
|---|---|---|---|---|---|
|Alternativa| D-01 | Fechas pasadas | No se permite una fecha de entrada anterior a hoy. | Permitirlas (útil para demos y casos de prueba con fechas fijas). | RN-41 |
|Defecto| D-02 | Cancelación tardía | 48 horas, cargo de 500.00 y check-in a las 15:00; el cargo solo se registra. | Otros valores, o generar una factura por el cargo. | RN-09 |
|Defecto | D-03 | Check-in y fecha actual | No se valida que hoy coincida con la fecha de entrada (simulación). | Permitir check-in solo desde la fecha de entrada. | RN-43 |
|Alternativa | D-04 | Fórmula del spa | `precioBase` es el precio por `MINUTOS_BASE` (60) minutos y se prorratea. | Precio fijo por sesión sin importar la duración. | RN-20 |
|Defecto |D-05 | Recargo exprés | Porcentaje (25 %) sobre el subtotal de la lavandería. | Monto fijo por servicio. | RN-21 |
|Defecto |D-06 | Total de la factura | Descuento VIP primero (10 %), impuesto después (16 %). | Impuesto sobre el subtotal sin descuento. | RN-26, RN-47 |
| Defecto|D-07 | Cuándo se factura | Una sola vez, con la reservación EN_CURSO; después no hay más consumos. | Permitir regenerar la factura mientras no esté pagada. | RN-46 |
| Defecto|D-08 | Sueldo de la camarista | Solo `salariobase`. | Solo pago por habitación, sin salario base. | RN-29 |
| Alternativa|D-09 | Contadores de nómina | La nómina no reinicia propinas. | Agregar un cierre de periodo que los reinicie. | RN-50 |
|Alternativa |D-10 | Baja de huésped | Se rechaza si tiene reservaciones ACTIVA o EN_CURSO. | Permitirla y cancelar sus reservaciones solo caundo estans ACTIVA. | RN-55 |
| Defecto|D-11 | Atributo `tieneJacuzzi` | Se elimina de `Suite` porque ninguna regla lo usa. | Conservarlo y definir su efecto (por ejemplo, un cargo extra). | UML y lista de clases |
| Defecto|D-12 | Interfaz `Desactivable` | Se agrega para reutilizar la lógica de baja lógica y de listados de activos. | Sin interfaz, repitiendo `activo` y `darDeBaja()` en las tres jerarquías. | UML y lista de clases |
