package servicio;

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
import java.util.List;

public class Hotel {
    protected String nombre;
    protected String direccion;
    protected List<Habitacion> habitaciones;
    protected List<Empleado> empleados;
    protected List<Huesped> huespedes;
    protected List<Servicio> servicios;
    protected List<Reservacion> reservaciones;

    public void registrarHuesped(Huesped h) {
    }

    public void registrarEmpleado(Empleado e) {
    }

    public void registrarHabitacion(Habitacion h) {
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
