package servicio;

import excepciones.HotelException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import modelo.alojamiento.EstadoHabitacion;
import modelo.alojamiento.Habitacion;
import modelo.catalogo.Servicio;
import modelo.facturacion.Factura;
import modelo.personas.Empleado;
import modelo.personas.Huesped;
import modelo.personas.TipoHuesped;
import modelo.reservas.Reservacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    public Reservacion crearReservacion(String idHuesped, List<Integer> numerosHab, LocalDate ini, LocalDate fin, int numHuespedes) {
    }

    public List<Huesped> listarHuespedes() {
    }

    public List<Empleado> listarEmpleados() {
    }

    public List<Habitacion> listarHabitaciones() {
    }

    public List<Servicio> listarServicios() {
    }

    public List<Reservacion> listarReservaciones() {
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

    public void darDeBajaHuesped(String id) {
    }

    public void darDeBajaEmpleado(String id) {
    }

    public void darDeBajaHabitacion(int num) {
    }

    public void darDeBajaServicio(String codigo) {
    }

    public void cancelarReservacion(String idRes, LocalDateTime ahora) {
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
