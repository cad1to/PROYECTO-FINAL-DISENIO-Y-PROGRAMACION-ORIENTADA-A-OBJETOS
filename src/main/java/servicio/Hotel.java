package servicio;

import excepciones.BajaNoPermitidaException;
import excepciones.HotelException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.ReservacionInvalidaException;
import modelo.alojamiento.EstadoHabitacion;
import modelo.alojamiento.Habitacion;
import modelo.catalogo.Servicio;
import modelo.facturacion.Factura;
import modelo.personas.Empleado;
import modelo.personas.Huesped;
import modelo.personas.TipoHuesped;
import modelo.reservas.EstadoReservacion;
import modelo.reservas.Reservacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hotel {
    protected String nombre;
    protected String direccion;
    protected List<Habitacion> habitaciones;
    protected List<Empleado> empleados;
    protected List<Huesped> huespedes;
    protected List<Servicio> servicios;
    protected List<Reservacion> reservaciones;

    public Hotel(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.habitaciones = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.huespedes = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.reservaciones = new ArrayList<>();
    }

    /**
     * Registra un huésped nuevo en el hotel.
     * <p>
     * El id del huésped debe ser único dentro del hotel; por ello se compara
     * contra todos los huéspedes registrados, incluso los dados de baja,
     * porque la baja es lógica y el id sigue ocupado.
     *
     * @param h huésped que se va a registrar
     * @throws RegistroDuplicadoException si ya existe un huésped con el mismo id
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void registrarHuesped(Huesped h) throws HotelException {
        for (Huesped existente : huespedes) {
            if (existente.getId().equals(h.getId())) {
                throw new RegistroDuplicadoException("Ya existe un huésped con el id " + h.getId());
            }
        }
        huespedes.add(h);
    }

    /**
     * Registra un nuevo empleado en la lista del Hotel (RF-02).
     * <p>
     * El id debe ser único entre todos los empleados (RN-51); se debe comparar debido a que
     * las bajas en el hotel son lógicas (RN-34), por ello el id sigue ocupado.
     *
     * @param e empleado nuevo a registrar
     * @throws RegistroDuplicadoException si ya existe un empleado con el mismo id
     * @throws HotelException excepción base para todas las reglas del hotel
     */
    public void registrarEmpleado(Empleado e) throws HotelException {
        for (Empleado existente : empleados) {
            if (existente.getId().equals(e.getId())) {
                throw new RegistroDuplicadoException("Ya existe un empleado con el id "+e.getId());
            }
        }
        empleados.add(e);
    }

    /**
     * Registra una nueva habitación en el hotel (RF-03).
     * <p>
     * El número debe ser único dentro del hotel (RN-11); se compara contra todas las
     * habitaciones, incluso las dadas de baja, porque la baja es lógica y el número sigue
     * ocupado (RN-34).
     *
     * @param h habitación nueva en el hotel
     * @throws RegistroDuplicadoException si ya existe una habitación con el mismo número
     * @throws HotelException excepción base para todas las reglas del hotel
     */
    public void registrarHabitacion(Habitacion h) throws HotelException {
        for (Habitacion existente : habitaciones) {
            if (existente.getNumero() == h.getNumero()) {
                throw new RegistroDuplicadoException("Ya existe la habitación con el número: " + h.getNumero());
            }
        }
        habitaciones.add(h);
    }

    /**
     * Registra un nuevo servicio en el catálogo del hotel (RF-04).
     * <p>
     * El código debe ser único dentro del hotel (RN-33); se compara contra todos
     * los servicios, incluso los dados de baja, porque la baja es lógica y el
     * código sigue ocupado (RN-34). El parámetro puede ser cualquier subclase de
     * {@code Servicio} (restaurante, spa o lavandería).
     *
     * @param s servicio nuevo a registrar
     * @throws RegistroDuplicadoException si ya existe un servicio con el mismo código
     * @throws HotelException             excepción base de las reglas del hotel
     */
    public void registrarServicio(Servicio s) throws HotelException {
        for (Servicio existente : servicios) {
            if (s.getCodigo().equals(existente.getCodigo())) {
                throw new RegistroDuplicadoException("Ya existe un servicio con el código "+s.getCodigo());
            }
        }
        servicios.add(s);
    }

     /*
        Agregación de métodos de búsqueda

        Para evitar repetir ciertas líneas de búsqueda, decidí implementar métodos que se dedicaran a ello
        para evitar repetir código y reciclar funciones.

        Búsqueda de huéspedes, empleados, habitaciones, servicios y reservaciones
      */

    /**
     * Busca un huésped activo por su id.
     * <p>
     * Un huésped dado de baja se trata como no encontrado (RN-56). Es un método
     * auxiliar interno que usan las operaciones de {@code Hotel} que necesitan
     * un huésped registrado y activo (RN-01).
     *
     * @param id id del huésped que se busca
     * @return el huésped activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Huesped buscarHuesped(String id) throws RegistroNoEncontradoException {
        for (Huesped existente : huespedes) {
            if (existente.getId().equals(id)){
                if (existente.isActivo()){
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El huésped "+id+" no fue encontrado");
    }

    /**
     * Busca un empleado activo por su id.
     * <p>
     * Un empleado dado de baja se trata como no encontrado (RN-56).
     *
     * @param id id del empleado que se busca
     * @return el empleado activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Empleado buscarEmpleado(String id) throws RegistroNoEncontradoException {
        for (Empleado existente : empleados) {
            if (existente.getId().equals(id)) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El empleado " + id + " no fue encontrado");
    }

    /**
     * Busca una habitación activa por su número.
     * <p>
     * Una habitación dada de baja se trata como no encontrada (RN-56).
     *
     * @param numero número de la habitación que se busca
     * @return la habitación activa con ese número
     * @throws RegistroNoEncontradoException si no existe o está dada de baja
     */
    private Habitacion buscarHabitacion(int numero) throws RegistroNoEncontradoException {
        for (Habitacion existente : habitaciones) {
            if (existente.getNumero() == numero) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("La habitación " + numero + " no fue encontrada");
    }

    /**
     * Busca un servicio activo por su código.
     * <p>
     * Un servicio dado de baja se trata como no encontrado (RN-56).
     *
     * @param codigo código del servicio que se busca
     * @return el servicio activo con ese código
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Servicio buscarServicio(String codigo) throws RegistroNoEncontradoException {
        for (Servicio existente : servicios) {
            if (existente.getCodigo().equals(codigo)) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El servicio " + codigo + " no fue encontrado");
    }

    /**
     * Busca una reservación por su id.
     * <p>
     * Las reservaciones no tienen baja lógica: se cancelan y se conservan con su
     * estado (RN-37), por eso aquí no se revisa si están activas.
     *
     * @param id id de la reservación que se busca
     * @return la reservación con ese id
     * @throws RegistroNoEncontradoException si no existe ninguna con ese id
     */
    private Reservacion buscarReservacion(String id) throws RegistroNoEncontradoException {
        for (Reservacion existente : reservaciones) {
            if (existente.getId().equals(id)) {
                return existente;
            }
        }
        throw new RegistroNoEncontradoException("La reservación " + id + " no fue encontrada");
    }

    public Reservacion crearReservacion(String idHuesped, List<Integer> numerosHab, LocalDate ini, LocalDate fin, int numHuespedes) {
    }

    /**
     * Lista los huéspedes activos del hotel (RF-06).
     * <p>
     * Los huéspedes dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de huéspedes activos; vacía si no hay ninguno
     */
    public List<Huesped> listarHuespedes() {
        List<Huesped> huespedesActivos = new ArrayList<>();
        for (Huesped existente : huespedes) {
            if (existente.isActivo()) {
                huespedesActivos.add(existente);
            }
        }
        return huespedesActivos;
    }

    /**
     * Lista los empleados activos del hotel (RF-06).
     * <p>
     * Los empleados dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de empleados activos; vacía si no hay ninguno
     */
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleadosActivos = new ArrayList<>();
        for (Empleado existente : empleados) {
            if (existente.isActivo()) {
                empleadosActivos.add(existente);
            }
        }
        return empleadosActivos;
    }

    /**
     * Lista las habitaciones activas del hotel (RF-06).
     * <p>
     * Las habitaciones dadas de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de habitaciones activas; vacía si no hay ninguna
     */
    public List<Habitacion> listarHabitaciones() {
        List<Habitacion> habitacionesActivas = new ArrayList<>();
        for (Habitacion existente : habitaciones) {
            if (existente.isActivo()) {
                habitacionesActivas.add(existente);
            }
        }
        return habitacionesActivas;
    }

    /**
     * Lista los servicios activos del catálogo del hotel (RF-06).
     * <p>
     * Los servicios dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan para el historial de las facturas (RN-34, RN-38). Devuelve una lista
     * nueva, por lo que modificarla no altera la colección interna (RN-39).
     *
     * @return lista de servicios activos; vacía si no hay ninguno
     */
    public List<Servicio> listarServicios() {
        List<Servicio> serviciosActivos = new ArrayList<>();
        for (Servicio existente : servicios) {
            if (existente.isActivo()) {
                serviciosActivos.add(existente);
            }
        }
        return serviciosActivos;
    }

    /**
     * Lista todas las reservaciones del hotel con su estado (RF-06).
     * <p>
     * A diferencia de los demás listados, no se filtran: se incluyen también las
     * FINALIZADAS y CANCELADAS (RN-35). Devuelve una copia de la colección, por lo
     * que modificarla no altera la lista interna (RN-39).
     *
     * @return lista con todas las reservaciones; vacía si no hay ninguna
     */
    public List<Reservacion> listarReservaciones() {
        // El constructor de ArrayList copia toda la lista, no es necesario recorrerla con for
        return new ArrayList<>(reservaciones);
    }

    public List<Habitacion> buscarDisponibles(LocalDate ini, LocalDate fin) {
    }

    public void actualizarTipoHuesped(String id, TipoHuesped t) {
    }

    public void actualizarSalarioEmpleado(String id, double salario) {
    }

    public void actualizarEstadoHabitacion(int num, EstadoHabitacion e) {
    }

    public void actualizarTarifaHabitacion(int num, double tarifa) {
    }

    public void actualizarPrecioServicio(String codigo, double precio) {
    }

    public void modificarFechasReservacion(String idRes, LocalDate ini, LocalDate fin) {
    }

    /**
     * Da de baja lógica a un huésped y cancela sus reservaciones ACTIVA (RF-08, RF-09).
     * <p>
     * El huésped no se elimina: se marca como inactivo para conservar el historial
     * (RN-34). Si tiene una reservación EN_CURSO la baja se rechaza (RN-55). Si no, se
     * cancelan todas sus reservaciones ACTIVA con las reglas de RN-08 a RN-10, usando el
     * mismo momento para todas. La validación se hace antes de cancelar: si se rechaza la
     * baja, no se cancela nada. Como un huésped dado de baja se trata como no encontrado
     * (RN-56), darlo de baja dos veces lanza la misma excepción que un id inexistente.
     *
     * @param id id del huésped que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws BajaNoPermitidaException si tiene una reservación EN_CURSO
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaHuesped(String id) throws HotelException {
        Huesped huesped = buscarHuesped(id);
        List<Reservacion> reservacionesActivas = new ArrayList<>();
        for (Reservacion existente : reservaciones) {
            if (existente.getHuesped().equals(huesped)) {
                if (existente.getEstado() ==  EstadoReservacion.EN_CURSO){
                    throw new BajaNoPermitidaException("El huesped "+id+" tiene una reservacion en curso.");
                }
                if (existente.getEstado() ==  EstadoReservacion.ACTIVA) {
                    reservacionesActivas.add(existente);
                }
            }
        }
        LocalDateTime ahora = LocalDateTime.now();
        for (Reservacion existente : reservacionesActivas) {
            cancelarReservacion(existente.getId(), ahora);
        }
        huesped.darDeBaja();
    }

    /**
     * Da de baja lógica a un empleado (RF-08).
     * <p>
     * El empleado no se elimina: se marca como inactivo para conservar el historial
     * (RN-34). Como un empleado dado de baja se trata como no encontrado (RN-56),
     * darlo de baja dos veces lanza la misma excepción que un id inexistente.
     *
     * @param id id del empleado que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaEmpleado(String id) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        empleado.darDeBaja();
    }

    /**
     * Da de baja lógica a una habitación (RF-08, RF-09).
     * <p>
     * La habitación no se elimina: se marca como inactiva para conservar el historial
     * (RN-34). Se rechaza la baja si la habitación pertenece a alguna reservación ACTIVA
     * o EN_CURSO (RN-15); las FINALIZADAS y CANCELADAS no la bloquean. La validación se
     * hace antes de la baja, por lo que si se rechaza la habitación no cambia. Como una
     * habitación dada de baja se trata como no encontrada (RN-56), darla de baja dos
     * veces lanza la misma excepción que un número inexistente.
     *
     * @param num número de la habitación que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dada de baja
     * @throws BajaNoPermitidaException si tiene reservaciones ACTIVA o EN_CURSO
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaHabitacion(int num) throws HotelException {
        Habitacion habitacion = buscarHabitacion(num);
        for (Reservacion reservacionExistente : reservaciones) {
            EstadoReservacion estadoReservacion = reservacionExistente.getEstado();
            if (estadoReservacion == EstadoReservacion.ACTIVA || estadoReservacion == EstadoReservacion.EN_CURSO) {
                for (Habitacion habitacionExistente : reservacionExistente.getHabitaciones()) {
                    if (habitacionExistente == habitacion) {
                        throw new BajaNoPermitidaException("La habitación " + num
                                + " tiene reservaciones ACTIVA o EN_CURSO.");
                    }
                }
            }
        }
        habitacion.darDeBaja();
    }

    /**
     * Da de baja lógica a un servicio del catálogo (RF-08).
     * <p>
     * El servicio no se elimina: se marca como inactivo, deja de ofrecerse en el
     * catálogo pero sus consumos anteriores se conservan en las facturas (RN-34, RN-38).
     * Como un servicio dado de baja se trata como no encontrado (RN-56), darlo de baja
     * dos veces lanza la misma excepción que un código inexistente.
     *
     * @param codigo código del servicio que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaServicio(String codigo) throws HotelException {
        Servicio servicio = buscarServicio(codigo);
        servicio.darDeBaja();
    }

    /**
     * Cancela una reservación ACTIVA (RF-10).
     * <p>
     * La cancelación aplica a la reservación completa y no la elimina: pasa a CANCELADA y
     * se conserva en el hotel (RN-10, RN-37). Solo se puede cancelar mientras esté ACTIVA
     * (RN-08). Si faltan menos de {@code HORAS_MINIMAS_CANCELACION} horas para la entrada,
     * la reservación registra {@code CARGO_CANCELACION} una sola vez, sin generar factura
     * (RN-09). Hotel solo localiza la reservación; la validación del estado y el cargo los
     * resuelve {@code Reservacion.cancelar} (RN-39).
     *
     * @param idRes id de la reservación que se cancela
     * @param ahora momento en que se solicita la cancelación; con él se mide la anticipación
     * @throws RegistroNoEncontradoException si no existe una reservación con ese id
     * @throws ReservacionInvalidaException  si la reservación no está ACTIVA
     * @throws HotelException                excepción base de las reglas del hotel
     */
    public void cancelarReservacion(String idRes, LocalDateTime ahora) throws HotelException {
        Reservacion reservacion = buscarReservacion(idRes);
        reservacion.cancelar(ahora);
    }

    public void realizarCheckIn(String idRecepcionista, String idRes) {

    }

    public void realizarCheckOut(String idRecepcionista, String idRes) {
    }

    public void registrarConsumo(String idRes, String codigoServicio, int cantidad) {
    }

    public Factura generarFactura(String idRes) {
    }

    public void pagarFactura(String idRes) {
    }

    public void registrarLimpieza(String idCamarista, int numHabitacion) {
    }

    public void registrarPropina(String idMesero, double monto) {
    }

    public double calcularNomina() {
    }

    public double cerrarPeriodoNomina() {
    }
}
