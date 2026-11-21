package servicio;

import excepciones.HotelException;
import excepciones.RegistroDuplicadoException;
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
        for(Empleado existente : empleados) {
            if(existente.getId().equals(e.getId())) {
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
            if(existente.getNumero() == h.getNumero()) {
                throw new RegistroDuplicadoException("Ya existe la habitación con el número: " + h.getNumero());
            }
        }
        habitaciones.add(h);
    }

    public void registrarServicio(Servicio s) {
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
