# Sistema de Gestión de Hotel: Reglas de negocio

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

Las reglas de negocio describen cómo opera realmente un hotel y son la base de los requisitos funcionales (RF). Cada regla indica el RF donde se aplica.

## 1. Reservaciones

| ID | Regla | RF |
|---|---|---|
| RN-01 | Una reservación solo puede hacerse para un huesped que este dado de alta y todas las habitaciones disponibles. | RF-05 |
| RN-02 | Una habitación no puede reservarse dos veces en fechas que se traslapan. | RF-05, RF-07 |
| RN-03 | El número de huéspedes de una reservación no puede superar la capacidad de la habitación. | RF-05 |
| RN-04 | La fecha de salida debe ser posterior a la fecha de entrada (mínimo una noche). | RF-05, RF-07 |
| RN-05 | Al modificar las fechas de una reservación se vuelven a validar el traslape y la capacidad. | RF-07 |
| RN-06 | Una reservación se puede cancelar mientras esté en estado ACTIVA. Una reservación FINALIZADA no se puede cancelar. | RF-09 |
| RN-07 | Si la cancelación se hace con menos de `HORAS_MINIMAS_CANCELACION` de anticipación, se cobra `CARGO_CANCELACION`. | RF-09 |

## 2. Habitaciones y su estado

| ID | Regla | RF |
|---|---|---|
| RN-08 | El número de habitación es único dentro del hotel. | RF-03 |
| RN-09 | El estado de una habitación es uno de: DISPONIBLE, OCUPADA, LIMPIEZA o MANTENIMIENTO. | RF-07 |
| RN-10 | Al hacer check-in, la habitación pasa a OCUPADA. Al hacer check-out, pasa a LIMPIEZA. | RF-10 |
| RN-11 | Una habitación en MANTENIMIENTO no puede ofrecerse en la búsqueda de disponibilidad. | RF-06 |
| RN-12 | No se puede dar de baja una habitación que tenga reservaciones activas. | RF-08 |

## 3. Tarifas y costos

| ID | Regla | RF |
|---|---|---|
| RN-13 | La tarifa de hospedaje depende del tipo de habitación: la sencilla usa la tarifa base por noche. | RF-11 |
| RN-14 | La habitación doble suma `CARGO_PERSONA_EXTRA` por cada persona adicional sobre la ocupación base. | RF-11 |
| RN-15 | La suite suma un cargo extra fijo por noche a la tarifa base. | RF-11 |
| RN-16 | El costo de un servicio depende de su tipo: el restaurante cobra por platillo. | RF-10 |
| RN-17 | El spa cobra según la duración en minutos, tomando `MINUTOS_BASE` como referencia del precio base. | RF-10 |
| RN-18 | La lavandería cobra por prenda y aplica `RECARGO_EXPRES` si el servicio es exprés. | RF-10 |

## 4. Consumos, facturación y pago

| ID | Regla | RF |
|---|---|---|
| RN-19 | Los consumos de servicios solo pueden registrarse dentro de una reservación activa. | RF-10 |
| RN-20 | Un consumo no existe fuera de su reservación: si la reservación desaparece, sus consumos también. | RF-10 |
| RN-21 | La factura suma el hospedaje (noches por tarifa), los consumos y el `IMPUESTO`. | RF-11 |
| RN-22 | Cada concepto de la factura (habitación y servicios) es un `Facturable` y genera un `DetalleFactura`. | RF-11 |
| RN-23 | Los huéspedes de tipo VIP reciben `DESCUENTO_VIP` en su factura. | RF-11 |
| RN-24 | El check-out solo puede realizarse si la factura de la reservación está pagada. | RF-10, RF-11 |

## 5. Personal y nómina

| ID | Regla | RF |
|---|---|---|
| RN-25 | El sueldo de cada empleado depende de su rol: el recepcionista cobra su salario base. | RF-12 |
| RN-26 | La camarista cobra según las habitaciones que limpió. | RF-12 |
| RN-27 | El gerente cobra su salario base más un bono mensual. | RF-12 |
| RN-28 | El mesero cobra su salario base más las propinas acumuladas. | RF-12 |
| RN-29 | La nómina solo incluye a los empleados activos. | RF-12 |

## 6. Altas, bajas y datos

| ID | Regla | RF |
|---|---|---|
| RN-30 | El código de cada servicio del catálogo es único. | RF-04 |
| RN-31 | Ningún elemento se elimina físicamente: la baja es lógica (`activo = false`) para conservar el historial de reservaciones, facturas y nómina. | RF-08 |
| RN-32 | Las consultas del sistema muestran solo elementos activos. | RF-06 |
| RN-33 | La baja de una reservación es su cancelación (estado CANCELADA), no se borra. | RF-09 |
| RN-34 | Un servicio dado de baja deja de ofrecerse en el catálogo, pero sus consumos anteriores se conservan en las facturas. | RF-08 |
| RN-35 | La clase `Hotel` es la única que gestiona las colecciones de habitaciones, empleados, huéspedes, servicios y reservaciones. | RF-01 a RF-12 |