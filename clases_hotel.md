# Sistema de Gestión de Hotel: Lista de clases

Proyecto Final, EE. Diseño y Programación Orientada a Objetos
Universidad Veracruzana, campus Coatzacoalcos

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

## Clases sin herencia

| Clase | Rol en el sistema |
|---|---|
| `Hotel` | Gestor central de todas las colecciones |
| `Reservacion` | Une huésped y habitación, con fechas y estado |
| `ConsumoServicio` | Servicio consumido dentro de una reservación (composición) |
| `Factura` | Cuenta final, compone sus detalles |
| `DetalleFactura` | Línea de la factura (composición) |

## Interfaz

| Interfaz | La implementan |
|---|---|
| `Facturable` | `Habitacion` (y sus 3 tipos) y `Servicio` (y sus 3 tipos) |

