# Sistema de Gestión de Hotel: Reglas de negocio

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

Las reglas de negocio describen cómo opera realmente un hotel y son la base de los requisitos funcionales (RF). Cada regla indica el RF donde se aplica.

Estados de una reservación: ACTIVA (confirmada, sin check-in), EN_CURSO (con check-in realizado), FINALIZADA (con check-out realizado) y CANCELADA.

## 1. Reservaciones

| ID | Regla | RF |
|---|---|---|
| RN-01 | Una reservación solo puede hacerse para un huésped que esté dado de alta y con todas las habitaciones disponibles. | RF-05 |
| RN-02 | Una habitación no puede reservarse dos veces en fechas que se traslapan. | RF-05 |
| RN-03 | Si alguna de las habitaciones solicitadas no está disponible, se rechaza toda la reservación; no puede quedar una reservación parcial. | RF-05 |
| RN-04 | Todas las habitaciones de una reservación comparten la misma fecha de entrada y de salida. | RF-05 |
| RN-05 | El número de huéspedes de una reservación no puede superar la capacidad total de sus habitaciones. | RF-05 |
| RN-06 | La fecha de salida debe ser posterior a la fecha de entrada (mínimo una noche). | RF-05, RF-07 |
| RN-07 | Solo se pueden modificar las fechas de una reservación ACTIVA y, al hacerlo, se vuelven a validar disponibilidad, traslapes y fechas. | RF-07 |
| RN-08 | Una reservación solo se puede cancelar mientras esté ACTIVA (sin check-in). Una reservación EN_CURSO, FINALIZADA o CANCELADA no se puede cancelar. | RF-10 |
| RN-09 | Si la cancelación se hace con menos de `HORAS_MINIMAS_CANCELACION` de anticipación, se cobra `CARGO_CANCELACION`, que queda registrado en la reservación. | RF-10 |
| RN-10 | La cancelación aplica a la reservación completa y el cargo por cancelación se cobra una sola vez. | RF-10 |

## 2. Habitaciones y su estado

| ID | Regla | RF |
|---|---|---|
| RN-11 | El número de habitación es único dentro del hotel. | RF-03 |
| RN-12 | El estado de una habitación es uno de: DISPONIBLE, OCUPADA, LIMPIEZA o MANTENIMIENTO. | RF-07 |
| RN-13 | El check-in solo puede hacerse sobre una reservación ACTIVA: la reservación pasa a EN_CURSO y todas sus habitaciones a OCUPADA. El check-out solo puede hacerse sobre una reservación EN_CURSO: la reservación pasa a FINALIZADA y sus habitaciones a LIMPIEZA. | RF-11 |
| RN-14 | Una habitación en MANTENIMIENTO no puede ofrecerse en la búsqueda de disponibilidad. | RF-06 |
| RN-15 | No se puede dar de baja una habitación que tenga reservaciones activas (en estado ACTIVA o EN_CURSO). | RF-09 |

## 3. Tarifas y costos

| ID | Regla | RF |
|---|---|---|
| RN-16 | La tarifa de hospedaje depende del tipo de habitación: la sencilla usa la tarifa base por noche. | RF-13 |
| RN-17 | La habitación doble cobra la tarifa base por noche y suma `CARGO_CAMA_EXTRA` por noche por cada cama adicional a la primera (`numCamas - 1`). | RF-13 |
| RN-18 | La suite suma un cargo extra fijo por noche a la tarifa base. | RF-13 |
| RN-19 | El costo de un servicio depende de su tipo: el restaurante cobra por platillo. | RF-12 |
| RN-20 | El spa cobra según la duración en minutos, tomando `MINUTOS_BASE` como referencia del precio base. | RF-12 |
| RN-21 | La lavandería cobra por prenda y aplica `RECARGO_EXPRES` si el servicio es exprés. | RF-12 |

## 4. Consumos, facturación y pago

| ID | Regla | RF |
|---|---|---|
| RN-22 | Los consumos de servicios solo pueden registrarse dentro de una reservación EN_CURSO. | RF-12 |
| RN-23 | Un consumo no existe fuera de su reservación: si la reservación desaparece, sus consumos también. | RF-12 |
| RN-24 | La factura suma el hospedaje de cada habitación (noches por tarifa), los consumos y el `IMPUESTO`. | RF-13 |
| RN-25 | Cada concepto de la factura (habitación y servicios) es un `Facturable` y genera un `DetalleFactura`. | RF-13 |
| RN-26 | Los huéspedes de tipo VIP reciben `DESCUENTO_VIP` en su factura. | RF-13 |
| RN-27 | El check-out de una reservación EN_CURSO solo puede realizarse si la factura de la reservación está pagada. | RF-11, RF-13 |

## 5. Personal y nómina

| ID | Regla | RF |
|---|---|---|
| RN-28 | El sueldo de cada empleado depende de su rol: el recepcionista cobra su salario base. | RF-14 |
| RN-29 | La camarista cobra según las habitaciones que limpió. | RF-14 |
| RN-30 | El gerente cobra su salario base más un bono mensual. | RF-14 |
| RN-31 | El mesero cobra su salario base más las propinas acumuladas. | RF-14 |
| RN-32 | La nómina solo incluye a los empleados activos. | RF-14 |

## 6. Altas, bajas y datos

| ID | Regla | RF |
|---|---|---|
| RN-33 | El código de cada servicio del catálogo es único. | RF-04 |
| RN-34 | Ningún elemento se elimina físicamente: la baja es lógica (`activo = false`) para conservar el historial de reservaciones, facturas y nómina. | RF-08 |
| RN-35 | Las consultas del sistema muestran solo elementos activos. | RF-06 |
| RN-36 | Del huésped solo puede actualizarse su tipo (REGULAR o VIP). | RF-07 |
| RN-37 | La baja de una reservación es su cancelación (estado CANCELADA), no se borra. | RF-10 |
| RN-38 | Un servicio dado de baja deja de ofrecerse en el catálogo, pero sus consumos anteriores se conservan en las facturas. | RF-08 |
| RN-39 | La clase `Hotel` es la única que gestiona las colecciones de habitaciones, empleados, huéspedes, servicios y reservaciones. | RF-01 a RF-14 |