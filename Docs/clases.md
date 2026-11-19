# Sistema de Gestión de Hotel: Lista de clases

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

Esta lista corresponde al diagrama `uml_hotel.mmd`. En el diagrama y en el código se omiten los getters y setters triviales. Las decisiones D-11 y D-12 están en `Reglas_negocio.md` (sección 9).

## Packages

Los namespaces del UML corresponden a packages Java (por ejemplo, `modelo_personas` es `modelo.personas`). El package `servicio` es la capa de lógica del hotel y no debe confundirse con la clase `Servicio`, que vive en `modelo.catalogo`.

| Package | Contenido |
|---|---|
| `modelo.comun` | `Desactivable` |
| `modelo.personas` | `Persona`, `Huesped`, `Empleado`, `Recepcionista`, `Camarista`, `Gerente`, `Mesero`, `TipoHuesped` |
| `modelo.alojamiento` | `Habitacion`, `HabitacionSencilla`, `HabitacionDoble`, `Suite`, `EstadoHabitacion` |
| `modelo.reservas` | `Reservacion`, `ConsumoServicio`, `EstadoReservacion` |
| `modelo.catalogo` | `Servicio`, `ServicioRestaurante`, `ServicioSpa`, `ServicioLavanderia`, `CategoriaServicio` |
| `modelo.facturacion` | `Facturable`, `Factura`, `DetalleFactura` |
| `servicio` | `Hotel` |
| `interfaz` | `MenuConsola`, `Main` |
| `excepciones` | `HotelException`, `RegistroDuplicadoException`, `RegistroNoEncontradoException`, `ReservacionInvalidaException`, `BajaNoPermitidaException`, `PagoPendienteException` |

## Jerarquías de herencia

| Clase | Clase padre | Clases hijas | Tipo |
|---|---|---|---|
| `Persona` | n/a | `Huesped`, `Empleado` | Abstracta |
| `Huesped` | `Persona` | n/a | Concreta |
| `Empleado` | `Persona` | `Recepcionista`, `Camarista`, `Gerente`, `Mesero` | Abstracta |
| `Recepcionista` | `Empleado` | n/a | Concreta |
| `Camarista` | `Empleado` | n/a | Concreta |
| `Gerente` | `Empleado` | n/a | Concreta |
| `Mesero` | `Empleado` | n/a | Concreta |
| `Habitacion` | n/a | `HabitacionSencilla`, `HabitacionDoble`, `Suite` | Abstracta |
| `HabitacionSencilla` | `Habitacion` | n/a | Concreta |
| `HabitacionDoble` | `Habitacion` | n/a | Concreta |
| `Suite` | `Habitacion` | n/a | Concreta |
| `Servicio` | n/a | `ServicioRestaurante`, `ServicioSpa`, `ServicioLavanderia` | Abstracta |
| `ServicioRestaurante` | `Servicio` | n/a | Concreta |
| `ServicioSpa` | `Servicio` | n/a | Concreta |
| `ServicioLavanderia` | `Servicio` | n/a | Concreta |
| `HotelException` | `Exception` | `RegistroDuplicadoException`, `RegistroNoEncontradoException`, `ReservacionInvalidaException`, `BajaNoPermitidaException`, `PagoPendienteException` | Concreta (verificada) |

## Clases sin herencia

| Clase | Rol en el sistema |
|---|---|
| `Hotel` | Gestor central de todas las colecciones; orquesta las operaciones y hace las validaciones que cruzan entidades (disponibilidad, traslapes, bajas), y delega el resto a cada entidad |
| `Reservacion` | Une huésped y habitaciones, con fechas y estado; valida sus propias transiciones de estado |
| `ConsumoServicio` | Servicio consumido dentro de una reservación (composición) |
| `Factura` | Cuenta final, compone sus detalles |
| `DetalleFactura` | Línea de la factura con concepto, cantidad y monto (composición) |
| `MenuConsola` | Interfaz de usuario por consola; solo usa a `Hotel` |
| `Main` | Punto de entrada: crea el `Hotel` y el `MenuConsola` |

## Enumeraciones

| Enumeración | Valores | La usa |
|---|---|---|
| `TipoHuesped` | REGULAR, VIP | `Huesped` |
| `EstadoHabitacion` | DISPONIBLE, OCUPADA, LIMPIEZA, MANTENIMIENTO | `Habitacion` |
| `EstadoReservacion` | ACTIVA, EN_CURSO, FINALIZADA, CANCELADA | `Reservacion` |
| `CategoriaServicio` | RESTAURANTE, SPA, LAVANDERIA | `Servicio` (fija en el constructor de cada subclase, sin setter) |

## Interfaces

| Interfaz | La implementan | Para qué sirve |
|---|---|---|
| `Facturable` | `Habitacion` (y sus 3 tipos) y `Servicio` (y sus 3 tipos) | Permite que `Factura` trate igual a habitaciones y servicios (`getConcepto()`, `calcularCosto()`) |
| `Desactivable` | `Persona` (y sus hijas), `Habitacion` (y sus 3 tipos) y `Servicio` (y sus 3 tipos) | Baja lógica común (`isActivo()`, `darDeBaja()`): permite a `Hotel` reutilizar un mismo código para listar activos y dar de baja en las cuatro colecciones |

## Relaciones y su justificación

| Tipo | Relación | Justificación |
|---|---|---|
| Agregación | `Hotel` con `Habitacion`, `Empleado`, `Huesped` y `Servicio` | Se crean fuera del hotel y se registran en él; la baja es lógica y los objetos pueden existir sin el `Hotel` |
| Asociación | `Hotel` con `Reservacion` | El hotel gestiona las reservaciones, pero cada una nace de una operación entre huésped y habitaciones y se conserva como historial |
| Asociación | `Reservacion` con `Huesped` y con `Habitacion` | Una reservación referencia a un huésped y a sus habitaciones, que existen por sí mismos |
| Asociación | `ConsumoServicio` con `Servicio` | El consumo referencia un servicio del catálogo, que sigue existiendo aunque se dé de baja (RN-38) |
| Asociación | `Reservacion` con `Factura` | La factura se genera sobre la reservación en el momento del cobro |
| Composición | `Reservacion` con `ConsumoServicio` | Un consumo no existe fuera de su reservación (RN-23) |
| Composición | `Factura` con `DetalleFactura` | Los detalles nacen y mueren con su factura |
| Dependencia | `Recepcionista` con `Reservacion` | Usa la reservación solo durante el check-in y el check-out |
| Dependencia | `Camarista` con `Habitacion` | Usa la habitación solo al registrar su limpieza |
| Dependencia | `Factura` con `Facturable` | Recibe un `Facturable` como parámetro de `agregarDetalle()` |
| Dependencia | `Hotel` con `Factura`, `Recepcionista`, `Camarista`, `Mesero` y las excepciones | Crea facturas, delega check-in, limpieza y propinas a los empleados, y lanza excepciones |
| Dependencia | `MenuConsola` y `Main` con `Hotel` | La interfaz solo usa el hotel; `Main` lo crea |

## Dónde aparece cada concepto de POO

Sirve de guía para los comentarios de trazabilidad y para el video.

| Concepto | Dónde |
|---|---|
| Herencia de 3 niveles | `Persona` → `Empleado` → `Recepcionista`, `Camarista`, `Gerente`, `Mesero` |
| Clases abstractas con métodos abstractos y concretos | `Persona`, `Empleado` (`getPuesto()` abstracto; `calcularSueldo()`, `cerrarPeriodo()` y `descripcion()` concretos), `Habitacion`, `Servicio` |
| Sobreescritura con comportamiento distinto | `calcularSueldo()` (gerente y mesero suman bono o propinas al salario base heredado), `cerrarPeriodo()` (el mesero reinicia sus propinas), `getPuesto()` (4 roles), `calcularTarifa()` (3 habitaciones), `calcularCosto()` (3 servicios), `descripcion()` |
| Polimorfismo en ejecución | `List<Empleado>` en la nómina y en el cierre de periodo, `List<Habitacion>` y `List<Facturable>` en la factura, consumos de distintos `Servicio` |
| Interfaces | `Facturable`, `Desactivable` |
| Reutilización de código | `Empleado.calcularSueldo()` devuelve el salario base y lo heredan recepcionista y camarista; gerente y mesero lo extienden con `super.calcularSueldo()`. `Desactivable` evita repetir la baja lógica |
| Encapsulación | Atributos `protected` con getters/setters o métodos de negocio |
| Eliminación de objetos | Baja lógica con `activo = false` (RN-34), justificada por conservar el historial de reservaciones, facturas y nómina |
