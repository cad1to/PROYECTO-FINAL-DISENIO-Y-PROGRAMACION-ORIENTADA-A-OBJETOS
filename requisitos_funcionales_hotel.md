# Sistema de Gestión de Hotel: Requisitos funcionales

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

Cada requisito indica entre paréntesis las reglas de negocio (RN) que lo gobiernan, definidas en `Reglas_negocio.md`. Los RF-15, RF-16 y RF-17 son nuevos; el resto conserva su número original.

## Altas

- **RF-01:** El sistema debe permitir registrar un huésped con id (único), nombre, apellido, teléfono, correo y tipo (REGULAR o VIP). (RN-39, RN-51, RN-53)
- **RF-02:** El sistema debe permitir registrar un empleado de cualquier rol (Recepcionista, Camarista, Gerente o Mesero) con id (único), nombre, apellido, teléfono, correo, salario base y fecha de ingreso, más el dato propio del rol (turno del recepcionista, bono mensual del gerente); la camarista y el mesero inician con sus contadores en cero. (RN-28 a RN-31, RN-51, RN-53)
- **RF-03:** El sistema debe permitir registrar habitaciones de tipo sencilla, doble (con número de camas) o suite (con cargo extra por noche), con número (único), piso, capacidad y tarifa base. La habitación inicia en estado DISPONIBLE. (RN-11, RN-12, RN-16 a RN-18, RN-53)
- **RF-04:** El sistema debe permitir registrar un servicio del catálogo eligiendo su categoría (restaurante, spa o lavandería), con código (único), nombre y precio base; en lavandería se indica además si es exprés. El sistema instancia la subclase correspondiente según la categoría. (RN-19 a RN-21, RN-33, RN-53)
- **RF-05:** El sistema debe permitir crear una reservación para un huésped y una o varias habitaciones, validando que el huésped esté registrado y activo, que todas las habitaciones estén disponibles en el rango de fechas (activas, fuera de MANTENIMIENTO y sin traslape), que las fechas sean válidas y que el número de huéspedes no supere la capacidad total. La reservación nace ACTIVA con un id generado por el sistema; si falla alguna validación, no se crea nada. (RN-01 a RN-06, RN-14, RN-40, RN-41, RN-52, RN-56)

## Consultas

- **RF-06:** El sistema debe permitir consultar huéspedes, empleados, habitaciones y servicios activos, consultar las reservaciones con su estado y buscar habitaciones disponibles en un rango de fechas. (RN-14, RN-35, RN-40)

## Actualizaciones

- **RF-07:** El sistema debe permitir actualizar el tipo de un huésped, el salario base de un empleado, el estado (DISPONIBLE, OCUPADA, LIMPIEZA, MANTENIMIENTO) y la tarifa base de una habitación, y el precio base de un servicio; y modificar las fechas de una reservación ACTIVA validando de nuevo disponibilidad, traslapes y fechas. (RN-02, RN-06, RN-07, RN-12, RN-36, RN-40 a RN-42, RN-45, RN-54)

## Bajas

- **RF-08:** El sistema debe permitir dar de baja lógica (`activo = false`) a un huésped, empleado, habitación o servicio. (RN-34, RN-38, RN-55, RN-56)
- **RF-09:** El sistema debe rechazar la baja de una habitación o de un huésped con reservaciones activas (en estado ACTIVA o EN_CURSO). (RN-15, RN-55)
- **RF-10:** El sistema debe permitir cancelar una reservación ACTIVA (estado CANCELADA). Si la cancelación ocurre con menos de `HORAS_MINIMAS_CANCELACION` horas de anticipación, se aplica `CARGO_CANCELACION`, que queda registrado en la reservación. (RN-08 a RN-10, RN-37)

## Operación del hotel, facturación y personal

- **RF-11:** El sistema debe permitir que un recepcionista activo registre el check-in y el check-out de una reservación. El check-in solo aplica a una reservación ACTIVA cuyas habitaciones estén todas DISPONIBLES: la reservación pasa a EN_CURSO y sus habitaciones a OCUPADA. El check-out solo aplica a una reservación EN_CURSO con la factura pagada: la reservación pasa a FINALIZADA y sus habitaciones a LIMPIEZA. (RN-13, RN-27, RN-42 a RN-44)
- **RF-12:** El sistema debe registrar los consumos de servicios dentro de la reservación en curso (EN_CURSO) mientras no se haya generado su factura, calculando el costo de cada uno según su tipo (**polimorfismo** en `calcularCosto()`). (RN-19 a RN-23, RN-46)
- **RF-13:** El sistema debe generar, una sola vez, la factura de una reservación EN_CURSO: cada habitación (noches, con `calcularCosto()` que delega en `calcularTarifa()` según el tipo de habitación) y cada consumo es un `Facturable` que genera un `DetalleFactura`; después se aplica `DESCUENTO_VIP` si el huésped es VIP y el `IMPUESTO`. (RN-16 a RN-18, RN-24 a RN-27, RN-45 a RN-47, RN-52)
- **RF-14:** El sistema debe calcular la nómina recorriendo todos los empleados activos y obteniendo el sueldo de cada rol con `calcularSueldo()` (**polimorfismo** sobre `List<Empleado>`), mostrando el sueldo de cada uno y el total. (RN-28 a RN-32, RN-50)
- **RF-15:** El sistema debe permitir registrar el pago de la factura generada de una reservación; el check-out requiere la factura pagada. (RN-27, RN-46)
- **RF-16:** El sistema debe permitir que una camarista activa registre la limpieza de una habitación en estado LIMPIEZA: la habitación pasa a DISPONIBLE y se incrementa su contador de habitaciones limpiadas. (RN-48)
- **RF-17:** El sistema debe permitir registrar las propinas recibidas por un mesero activo, acumulándolas en su total. (RN-49)
