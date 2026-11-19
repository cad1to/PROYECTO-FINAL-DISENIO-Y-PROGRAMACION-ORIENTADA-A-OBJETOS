package modelo.reservas;

import modelo.alojamiento.Habitacion;
import modelo.catalogo.Servicio;
import modelo.facturacion.Factura;
import modelo.personas.Huesped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Reservacion {
    public static final int HORAS_MINIMAS_CANCELACION = 48;
    public static final double CARGO_CANCELACION = 500.00;
    public static final LocalTime HORA_CHECK_IN = LocalTime.of(15, 0);

    protected String id;
    protected LocalDate fechaEntrada;
    protected LocalDate fechaSalida;
    protected int numHuespedes;
    protected EstadoReservacion estado;
    protected double cargoCancelacion;
    protected Huesped huesped;
    protected List<Habitacion> habitaciones;
    protected List<ConsumoServicio> consumos;
    protected Factura factura;

    public int calcularNoches() {
    }

    public int calcularCapacidadTotal() {
    }

    public boolean seTraslapaCon(LocalDate ini, LocalDate fin) {
    }

    public void modificarFechas(LocalDate ini, LocalDate fin) {
    }

    public void agregarConsumo(Servicio s, int cantidad) {
    }

    public void iniciar() {
    }

    public void finalizar() {
    }

    public void cancelar(LocalDateTime ahora) {
    }
}
