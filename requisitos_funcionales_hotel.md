# Sistema de Gestión de Hotel: Requisitos funcionales

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos


- **RF-01:** El sistema debe permitir registrar un huésped con id, nombre, apellido, teléfono, correo y tipo (REGULAR o VIP).
- **RF-02:** El sistema debe permitir registrar un empleado de cualquier rol (Recepcionista, Camarista, Gerente o Mesero), con salario base y fecha de ingreso.
- **RF-03:** El sistema debe permitir registrar habitaciones de tipo sencilla, doble o suite, con número (único), piso, capacidad y tarifa base.
- **RF-04:** El sistema debe permitir registrar un servicio del catálogo eligiendo su categoría (restaurante, spa o lavandería), con código (único), nombre y precio base. El sistema instancia la subclase correspondiente según la categoría.
- **RF-05:** El sistema debe permitir crear una reservación para un huésped y varias habitaciones, validando que el huésped esté dado de alta y todas las habitaciones disponibles, que no haya traslape de fechas y que el número de huéspedes no supere la capacidad.
- **RF-06:** El sistema debe permitir consultar huéspedes, empleados, habitaciones y servicios activos, y buscar habitaciones disponibles en un rango de fechas.
- **RF-07:** El sistema debe permitir actualizar los datos de un huésped (tipo), empleado, habitación (incluido su estado: DISPONIBLE, OCUPADA, LIMPIEZA, MANTENIMIENTO) o servicio, y modificar las fechas de una reservación ACTIVA validando de nuevo la disponibilidad.
- **RF-08:** El sistema debe permitir dar de baja (`activo = false`) a un huésped, empleado, habitación o servicio.
- **RF-09:** El sistema debe rechazar la baja de una habitación con reservaciones activas (en estado ACTIVA o EN_CURSO).
- **RF-10:** El sistema debe permitir cancelar una reservación ACTIVA (estado CANCELADA). Si la cancelación ocurre con menos de `HORAS_MINIMAS_CANCELACION` de anticipación, se aplica `CARGO_CANCELACION`, que queda registrado en la reservación.
- **RF-11:** El sistema debe permitir registrar el check-in y check-out de una reservación. El check-in solo aplica a una reservación ACTIVA: la reservación pasa a EN_CURSO y sus habitaciones a OCUPADA. El check-out solo aplica a una reservación EN_CURSO: la reservación pasa a FINALIZADA y sus habitaciones a LIMPIEZA.
- **RF-12:** El sistema debe registrar los consumos de servicios dentro de la reservación en curso (EN_CURSO), calculando el costo de cada uno según su tipo (**polimorfismo** en `calcularCosto()`).
- **RF-13:** El sistema debe generar la factura de una reservación sumando noches (con `calcularTarifa()` según el tipo de habitación), consumos y el `IMPUESTO`, aplicando `DESCUENTO_VIP` si el huésped es VIP. El check-out requiere la factura pagada.
- **RF-14:** El sistema debe calcular la nómina recorriendo todos los empleados activos y obteniendo el sueldo de cada rol con `calcularSueldo()` (**polimorfismo** sobre `List<Empleado>`).