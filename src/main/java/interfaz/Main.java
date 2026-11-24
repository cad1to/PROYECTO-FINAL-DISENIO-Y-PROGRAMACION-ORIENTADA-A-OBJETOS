package interfaz;

import excepciones.HotelException;
import modelo.alojamiento.EstadoHabitacion;
import modelo.alojamiento.Habitacion;
import modelo.alojamiento.HabitacionDoble;
import modelo.alojamiento.HabitacionSencilla;
import modelo.alojamiento.Suite;
import modelo.catalogo.Servicio;
import modelo.catalogo.ServicioLavanderia;
import modelo.catalogo.ServicioRestaurante;
import modelo.catalogo.ServicioSpa;
import modelo.facturacion.DetalleFactura;
import modelo.facturacion.Factura;
import modelo.personas.Camarista;
import modelo.personas.Empleado;
import modelo.personas.Gerente;
import modelo.personas.Huesped;
import modelo.personas.Mesero;
import modelo.personas.Recepcionista;
import modelo.personas.TipoHuesped;
import modelo.reservas.Reservacion;
import servicio.Hotel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Programa ejecutable: ciclo del menú y ejecución de cada opción sobre el {@link Hotel}. */
public class Main {
    private static final Hotel hotel = new Hotel("Hotel", "Coatzacoalcos, Veracruz");
    private static final MenuConsola menu = new MenuConsola();

    public static void main(String[] args) {
        int opcion;
        do {
            menu.mostrarMenu();
            opcion = menu.leerOpcion();
            if (opcion > 31 || opcion < -1) {
                System.out.println("Opción no válida.");
            } else if (opcion == -1) {
                System.out.println("Escribe un número.");
            } else if (opcion > 0) {
                ejecutar(opcion);
            }
        } while (opcion != 0);
        System.out.println("Hasta luego.");
    }

    /** Ejecuta la opción elegida; los errores del hotel o de validación se muestran al usuario. */
    private static void ejecutar(int opcion) {
        try {
            switch (opcion) {
                case 1 -> registrarHuesped();
                case 2 -> registrarEmpleado();
                case 3 -> registrarHabitacion();
                case 4 -> registrarServicio();
                case 5 -> crearReservacion();
                case 6 -> listarHuespedes();
                case 7 -> listarEmpleados();
                case 8 -> listarHabitaciones();
                case 9 -> listarServicios();
                case 10 -> listarReservaciones();
                case 11 -> buscarDisponibles();
                case 12 -> actualizarTipoHuesped();
                case 13 -> actualizarSalarioEmpleado();
                case 14 -> actualizarEstadoHabitacion();
                case 15 -> actualizarTarifaHabitacion();
                case 16 -> actualizarPrecioServicio();
                case 17 -> modificarFechasReservacion();
                case 23 -> realizarCheckIn();
                case 24 -> realizarCheckOut();
                case 25 -> registrarConsumo();
                case 26 -> generarFactura();
                case 27 -> pagarFactura();
                case 18 -> darDeBajaHuesped();
                case 19 -> darDeBajaEmpleado();
                case 20 -> darDeBajaHabitacion();
                case 21 -> darDeBajaServicio();
                case 22 -> cancelarReservacion();
                default -> System.out.println("Opción " + opcion + " aún no implementada.");
            }
        } catch (HotelException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- ALTAS

    private static void registrarHuesped() throws HotelException {
        System.out.println("\n-- Registrar huésped --");
        String id = menu.leerTexto("Id: ");
        String nombre = menu.leerTexto("Nombre: ");
        String apellido = menu.leerTexto("Apellido: ");
        String telefono = menu.leerTexto("Teléfono: ");
        String correo = menu.leerTexto("Correo: ");
        int t = menu.leerEnteroEnRango("Tipo (1. Regular, 2. VIP): ", 1, 2);
        TipoHuesped tipo = (t == 1) ? TipoHuesped.REGULAR : TipoHuesped.VIP;

        hotel.registrarHuesped(new Huesped(id, nombre, apellido, telefono, correo, tipo));
        System.out.println("Huésped registrado.");
    }

    private static void registrarEmpleado() throws HotelException {
        System.out.println("\n-- Registrar empleado --");
        int t = menu.leerEnteroEnRango("Puesto (1. Recepcionista, 2. Mesero, 3. Camarista, 4. Gerente): ", 1, 4);
        String id = menu.leerTexto("Id: ");
        String nombre = menu.leerTexto("Nombre: ");
        String apellido = menu.leerTexto("Apellido: ");
        String telefono = menu.leerTexto("Teléfono: ");
        String correo = menu.leerTexto("Correo: ");
        double salario = menu.leerDecimal("Salario base: ");
        LocalDate ingreso = menu.leerFecha("Fecha de ingreso (AAAA-MM-DD): ");

        Empleado empleado;
        switch (t) {
            case 1 -> {
                String turno = menu.leerTexto("Turno (ej. Matutino): ");
                empleado = new Recepcionista(id, nombre, apellido, telefono, correo, salario, ingreso, turno);
            }
            case 2 -> empleado = new Mesero(id, nombre, apellido, telefono, correo, salario, ingreso);
            case 3 -> empleado = new Camarista(id, nombre, apellido, telefono, correo, salario, ingreso);
            default -> {
                double bono = menu.leerDecimal("Bono mensual: ");
                empleado = new Gerente(id, nombre, apellido, telefono, correo, salario, ingreso, bono);
            }
        }
        hotel.registrarEmpleado(empleado);
        System.out.println("Empleado registrado.");
    }

    private static void registrarHabitacion() throws HotelException {
        System.out.println("\n-- Registrar habitación --");
        int t = menu.leerEnteroEnRango("Tipo (1. Sencilla, 2. Doble, 3. Suite): ", 1, 3);
        int numero = menu.leerEntero("Número: ");
        int piso = menu.leerEntero("Piso: ");
        int capacidad = menu.leerEntero("Capacidad (personas): ");
        double tarifa = menu.leerDecimal("Tarifa base por noche: ");

        Habitacion habitacion;
        switch (t) {
            case 1 -> habitacion = new HabitacionSencilla(numero, piso, capacidad, tarifa);
            case 2 -> {
                int camas = menu.leerEntero("Número de camas: ");
                habitacion = new HabitacionDoble(numero, piso, capacidad, tarifa, camas);
            }
            default -> {
                double cargo = menu.leerDecimal("Cargo extra de la suite: ");
                habitacion = new Suite(numero, piso, capacidad, tarifa, cargo);
            }
        }
        hotel.registrarHabitacion(habitacion);
        System.out.println("Habitación registrada.");
    }

    private static void registrarServicio() throws HotelException {
        System.out.println("\n-- Registrar servicio --");
        int t = menu.leerEnteroEnRango("Categoría (1. Restaurante, 2. Spa, 3. Lavandería): ", 1, 3);
        String codigo = menu.leerTexto("Código: ");
        String nombre = menu.leerTexto("Nombre: ");
        double precio = menu.leerDecimal("Precio base: ");

        Servicio servicio;
        switch (t) {
            case 1 -> servicio = new ServicioRestaurante(codigo, nombre, precio);
            case 2 -> servicio = new ServicioSpa(codigo, nombre, precio);
            default -> {
                boolean expres = menu.leerSiNo("¿Es servicio exprés? (s/n): ");
                servicio = new ServicioLavanderia(codigo, nombre, precio, expres);
            }
        }
        hotel.registrarServicio(servicio);
        System.out.println("Servicio registrado.");
    }

    private static void crearReservacion() throws HotelException {
        System.out.println("\n-- Crear reservación --");
        String idHuesped = menu.leerTexto("Id del huésped: ");
        List<Integer> habitaciones = menu.leerListaEnteros("Números de habitación (separados por coma): ");
        LocalDate entrada = menu.leerFecha("Fecha de entrada (AAAA-MM-DD): ");
        LocalDate salida = menu.leerFecha("Fecha de salida (AAAA-MM-DD): ");
        int huespedes = menu.leerEntero("Número de huéspedes: ");

        Reservacion r = hotel.crearReservacion(idHuesped, habitaciones, entrada, salida, huespedes);
        System.out.println("Reservación creada con id " + r.getId() + ".");
    }

    // ------------------------------------------------------------ CONSULTAS

    private static void listarHuespedes() {
        System.out.println("\n-- Huéspedes --");
        List<Huesped> lista = hotel.listarHuespedes();
        if (lista.isEmpty()) {
            System.out.println("No hay huéspedes registrados.");
        }
        for (Huesped h : lista) {
            System.out.printf("%s | %s | %s | tel. %s | %s%n", h.getId(), h.getNombreCompleto(),
                    h.getTipo(), h.getTelefono(), h.getCorreo());
        }
    }

    private static void listarEmpleados() {
        System.out.println("\n-- Empleados --");
        List<Empleado> lista = hotel.listarEmpleados();
        if (lista.isEmpty()) {
            System.out.println("No hay empleados registrados.");
        }
        for (Empleado e : lista) {
            System.out.printf("%s | %s | %s | salario $%.2f | ingreso %s%n", e.getId(),
                    e.getNombreCompleto(), e.getClass().getSimpleName(), e.getSalarioBase(),
                    e.getFechaIngreso());
        }
    }

    private static void listarHabitaciones() {
        System.out.println("\n-- Habitaciones --");
        List<Habitacion> lista = hotel.listarHabitaciones();
        if (lista.isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
        }
        for (Habitacion h : lista) {
            System.out.printf("#%d | %s | piso %d | capacidad %d | tarifa $%.2f | %s%n", h.getNumero(),
                    h.getClass().getSimpleName(), h.getPiso(), h.getCapacidad(), h.getTarifaBase(),
                    h.getEstado());
        }
    }

    private static void listarServicios() {
        System.out.println("\n-- Servicios --");
        List<Servicio> lista = hotel.listarServicios();
        if (lista.isEmpty()) {
            System.out.println("No hay servicios registrados.");
        }
        for (Servicio s : lista) {
            System.out.printf("%s | %s | %s | precio base $%.2f%n", s.getCodigo(), s.getNombre(),
                    s.getCategoria(), s.getPrecioBase());
        }
    }

    private static void listarReservaciones() {
        System.out.println("\n-- Reservaciones --");
        List<Reservacion> lista = hotel.listarReservaciones();
        if (lista.isEmpty()) {
            System.out.println("No hay reservaciones registradas.");
        }
        for (Reservacion r : lista) {
            List<Integer> numeros = r.getHabitaciones().stream().map(Habitacion::getNumero).toList();
            System.out.printf("%s | %s | hab. %s | %s a %s | %d huésped(es) | %s%n", r.getId(),
                    r.getHuesped().getNombreCompleto(), numeros, r.getFechaEntrada(),
                    r.getFechaSalida(), r.getNumHuespedes(), r.getEstado());
        }
    }

    private static void buscarDisponibles() {
        System.out.println("\n-- Habitaciones disponibles --");
        LocalDate ini = menu.leerFecha("Fecha de entrada (AAAA-MM-DD): ");
        LocalDate fin = menu.leerFecha("Fecha de salida (AAAA-MM-DD): ");
        List<Habitacion> lista = hotel.buscarDisponibles(ini, fin);
        if (lista.isEmpty()) {
            System.out.println("No hay habitaciones disponibles en ese periodo.");
        }
        for (Habitacion h : lista) {
            System.out.printf("#%d | %s | piso %d | capacidad %d | tarifa $%.2f%n", h.getNumero(),
                    h.getClass().getSimpleName(), h.getPiso(), h.getCapacidad(), h.getTarifaBase());
        }
    }

    // ------------------------------------------------------- ACTUALIZACIONES

    private static void actualizarTipoHuesped() throws HotelException {
        System.out.println("\n-- Actualizar tipo de huésped --");
        String id = menu.leerTexto("Id del huésped: ");
        int t = menu.leerEnteroEnRango("Nuevo tipo (1. Regular, 2. VIP): ", 1, 2);
        hotel.actualizarTipoHuesped(id, t == 1 ? TipoHuesped.REGULAR : TipoHuesped.VIP);
        System.out.println("Tipo de huésped actualizado.");
    }

    private static void actualizarSalarioEmpleado() throws HotelException {
        System.out.println("\n-- Actualizar salario de empleado --");
        String id = menu.leerTexto("Id del empleado: ");
        double salario = menu.leerDecimal("Nuevo salario base: ");
        hotel.actualizarSalarioEmpleado(id, salario);
        System.out.println("Salario actualizado.");
    }

    private static void actualizarEstadoHabitacion() throws HotelException {
        System.out.println("\n-- Actualizar estado de habitación --");
        int numero = menu.leerEntero("Número de habitación: ");
        // OCUPADA no se ofrece: solo la asigna el check-in (RN-42).
        int e = menu.leerEnteroEnRango("Nuevo estado (1. Disponible, 2. Limpieza, 3. Mantenimiento): ", 1, 3);
        EstadoHabitacion estado = switch (e) {
            case 1 -> EstadoHabitacion.DISPONIBLE;
            case 2 -> EstadoHabitacion.LIMPIEZA;
            default -> EstadoHabitacion.MANTENIMIENTO;
        };
        hotel.actualizarEstadoHabitacion(numero, estado);
        System.out.println("Estado de la habitación actualizado.");
    }

    private static void actualizarTarifaHabitacion() throws HotelException {
        System.out.println("\n-- Actualizar tarifa de habitación --");
        int numero = menu.leerEntero("Número de habitación: ");
        double tarifa = menu.leerDecimal("Nueva tarifa base por noche: ");
        hotel.actualizarTarifaHabitacion(numero, tarifa);
        System.out.println("Tarifa actualizada.");
    }

    private static void actualizarPrecioServicio() throws HotelException {
        System.out.println("\n-- Actualizar precio de servicio --");
        String codigo = menu.leerTexto("Código del servicio: ");
        double precio = menu.leerDecimal("Nuevo precio base: ");
        hotel.actualizarPrecioServicio(codigo, precio);
        System.out.println("Precio actualizado.");
    }

    private static void modificarFechasReservacion() throws HotelException {
        System.out.println("\n-- Modificar fechas de reservación --");
        String id = menu.leerTexto("Id de la reservación: ");
        LocalDate ini = menu.leerFecha("Nueva fecha de entrada (AAAA-MM-DD): ");
        LocalDate fin = menu.leerFecha("Nueva fecha de salida (AAAA-MM-DD): ");
        hotel.modificarFechasReservacion(id, ini, fin);
        System.out.println("Fechas de la reservación actualizadas.");
    }

    // ------------------------------------------------ OPERACIÓN Y FACTURACIÓN

    private static void realizarCheckIn() throws HotelException {
        System.out.println("\n-- Check-in --");
        String idRecepcionista = menu.leerTexto("Id del recepcionista: ");
        String idReservacion = menu.leerTexto("Id de la reservación: ");
        hotel.realizarCheckIn(idRecepcionista, idReservacion);
        System.out.println("Check-in realizado.");
    }

    private static void realizarCheckOut() throws HotelException {
        System.out.println("\n-- Check-out --");
        String idRecepcionista = menu.leerTexto("Id del recepcionista: ");
        String idReservacion = menu.leerTexto("Id de la reservación: ");
        hotel.realizarCheckOut(idRecepcionista, idReservacion);
        System.out.println("Check-out realizado.");
    }

    private static void registrarConsumo() throws HotelException {
        System.out.println("\n-- Registrar consumo de servicio --");
        String idReservacion = menu.leerTexto("Id de la reservación: ");
        String codigo = menu.leerTexto("Código del servicio: ");
        int cantidad = menu.leerEntero("Cantidad: ");
        hotel.registrarConsumo(idReservacion, codigo, cantidad);
        System.out.println("Consumo registrado.");
    }

    private static void generarFactura() throws HotelException {
        System.out.println("\n-- Generar factura --");
        Factura f = hotel.generarFactura(menu.leerTexto("Id de la reservación: "));

        System.out.println("Factura " + f.getFolio() + " | emisión " + f.getFechaEmision());
        for (DetalleFactura d : f.getDetalles()) {
            System.out.printf("  %-35s x%d  $%.2f%n", d.getConcepto(), d.getCantidad(), d.getMonto());
        }
        System.out.printf("Subtotal:  $%.2f%n", f.calcularSubtotal());
        System.out.printf("Descuento: %.0f%%%n", f.getDescuento() * 100);
        System.out.printf("IVA:       %.0f%%%n", Factura.IMPUESTO * 100);
        System.out.printf("TOTAL:     $%.2f%n", f.calcularTotal());
        System.out.println("Estado: " + (f.isPagada() ? "pagada" : "pendiente de pago"));
    }

    private static void pagarFactura() throws HotelException {
        System.out.println("\n-- Pagar factura --");
        hotel.pagarFactura(menu.leerTexto("Id de la reservación: "));
        System.out.println("Factura pagada.");
    }

    // ---------------------------------------------------------------- BAJAS

    private static void darDeBajaHuesped() throws HotelException {
        System.out.println("\n-- Dar de baja huésped --");
        hotel.darDeBajaHuesped(menu.leerTexto("Id del huésped: "));
        System.out.println("Huésped dado de baja.");
    }

    private static void darDeBajaEmpleado() throws HotelException {
        System.out.println("\n-- Dar de baja empleado --");
        hotel.darDeBajaEmpleado(menu.leerTexto("Id del empleado: "));
        System.out.println("Empleado dado de baja.");
    }

    private static void darDeBajaHabitacion() throws HotelException {
        System.out.println("\n-- Dar de baja habitación --");
        hotel.darDeBajaHabitacion(menu.leerEntero("Número de habitación: "));
        System.out.println("Habitación dada de baja.");
    }

    private static void darDeBajaServicio() throws HotelException {
        System.out.println("\n-- Dar de baja servicio --");
        hotel.darDeBajaServicio(menu.leerTexto("Código del servicio: "));
        System.out.println("Servicio dado de baja.");
    }

    private static void cancelarReservacion() throws HotelException {
        System.out.println("\n-- Cancelar reservación --");
        String id = menu.leerTexto("Id de la reservación: ");
        hotel.cancelarReservacion(id, LocalDateTime.now());

        double cargo = hotel.listarReservaciones().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .map(Reservacion::getCargoCancelacion)
                .orElse(0.0);
        System.out.println("Reservación cancelada."
                + (cargo > 0 ? " Cargo por cancelación tardía: $" + String.format("%.2f", cargo) : ""));
    }
}
