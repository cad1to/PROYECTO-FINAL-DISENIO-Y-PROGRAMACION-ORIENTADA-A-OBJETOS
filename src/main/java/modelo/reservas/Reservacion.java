package modelo.reservas;

import excepciones.PagoPendienteException;
import excepciones.ReservacionInvalidaException;
import modelo.alojamiento.EstadoHabitacion;
import modelo.alojamiento.Habitacion;
import modelo.catalogo.Servicio;
import modelo.facturacion.Factura;
import modelo.personas.Huesped;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reservación de una o varias habitaciones para un huésped en un rango de fechas.
 * <p>
 * Valida sus propias transiciones de estado (RN-39):
 * {@code ACTIVA → EN_CURSO → FINALIZADA} o {@code ACTIVA → CANCELADA}.
 * Las validaciones que cruzan entidades (disponibilidad y traslapes con otras
 * reservaciones, RN-02, RN-40) las hace {@code Hotel}.
 * </p>
 */
public class Reservacion {
    /** Horas mínimas de anticipación para cancelar sin cargo (RN-09). */
    public static final int HORAS_MINIMAS_CANCELACION = 48;
    /** Cargo que se registra por una cancelación tardía (RN-09). */
    public static final double CARGO_CANCELACION = 500.00;
    /** Hora de check-in usada para medir la anticipación de una cancelación (RN-09). */
    public static final LocalTime HORA_CHECK_IN = LocalTime.of(15, 0);

    /** Identificador único generado por el sistema (RN-52). */
    protected String id;
    /** Fecha de entrada, común a todas las habitaciones (RN-04). */
    protected LocalDate fechaEntrada;
    /** Fecha de salida, común a todas las habitaciones (RN-04). */
    protected LocalDate fechaSalida;
    /** Número de huéspedes que se hospedan. */
    protected int numHuespedes;
    /** Estado actual de la reservación. */
    protected EstadoReservacion estado;
    /** Cargo registrado por cancelación tardía; 0 si no aplica (RN-09). */
    protected double cargoCancelacion;
    /** Huésped que realiza la reservación. */
    protected Huesped huesped;
    /** Habitaciones reservadas (al menos una). */
    protected List<Habitacion> habitaciones;
    /** Consumos de servicios registrados durante la estancia (composición, RN-23). */
    protected List<ConsumoServicio> consumos;
    /** Factura de la reservación; {@code null} mientras no se genere (RN-46). */
    protected Factura factura;

    /**
     * Crea una reservación en estado ACTIVA, sin consumos, sin factura y sin cargo.
     * La disponibilidad de las habitaciones (RN-01, RN-02, RN-40) la valida {@code Hotel}
     * antes de crearla.
     *
     * @param id           identificador generado por el sistema; no puede estar vacío
     * @param huesped      huésped que reserva; no puede ser nulo
     * @param habitaciones habitaciones reservadas; al menos una y sin elementos nulos
     * @param fechaEntrada fecha de entrada; no puede ser nula
     * @param fechaSalida  fecha de salida; no puede ser nula
     * @param numHuespedes número de huéspedes; debe ser positivo
     * @throws IllegalArgumentException     si algún dato es nulo, vacío o no positivo (RN-53)
     * @throws ReservacionInvalidaException si la salida no es posterior a la entrada (RN-06)
     *                                      o los huéspedes superan la capacidad total (RN-05)
     */
    public Reservacion(String id, Huesped huesped, List<Habitacion> habitaciones,
                       LocalDate fechaEntrada, LocalDate fechaSalida, int numHuespedes)
            throws ReservacionInvalidaException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la reservación no puede estar vacío.");
        }
        if (huesped == null) {
            throw new IllegalArgumentException("El huésped no puede ser nulo.");
        }
        if (habitaciones == null || habitaciones.isEmpty()) {
            throw new IllegalArgumentException("La reservación debe tener al menos una habitación.");
        }
        for (Habitacion h : habitaciones) {
            if (h == null) {
                throw new IllegalArgumentException("La lista de habitaciones no puede contener nulos.");
            }
        }
        if (fechaEntrada == null || fechaSalida == null) {
            throw new IllegalArgumentException("Las fechas de la reservación no pueden ser nulas.");
        }
        if (numHuespedes <= 0) {
            throw new IllegalArgumentException("El número de huéspedes debe ser positivo.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new ReservacionInvalidaException(
                    "La fecha de salida debe ser posterior a la fecha de entrada.");
        }
        int capacidadTotal = 0;
        for (Habitacion h : habitaciones) {
            capacidadTotal += h.getCapacidad();
        }
        if (numHuespedes > capacidadTotal) {
            throw new ReservacionInvalidaException("El número de huéspedes (" + numHuespedes
                    + ") supera la capacidad total de las habitaciones (" + capacidadTotal + ").");
        }
        this.id = id.trim();
        this.huesped = huesped;
        this.habitaciones = new ArrayList<>(habitaciones);
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numHuespedes = numHuespedes;
        this.estado = EstadoReservacion.ACTIVA;
        this.cargoCancelacion = 0.0;
        this.consumos = new ArrayList<>();
        this.factura = null;
    }

    /**
     * Calcula el número de noches entre la fecha de entrada y la de salida.
     *
     * @return número de noches (al menos 1, RN-06)
     */
    public int calcularNoches() {
        return (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    /**
     * Suma la capacidad de todas las habitaciones reservadas.
     *
     * @return capacidad total de la reservación
     */
    public int calcularCapacidadTotal() {
        int total = 0;
        for (Habitacion h : habitaciones) {
            total += h.getCapacidad();
        }
        return total;
    }

    /**
     * Indica si esta reservación ocupa fechas que se traslapan con el rango dado (RN-02).
     * Dos rangos se traslapan si {@code entrada1 < salida2} y {@code entrada2 < salida1},
     * así que la salida de una y la entrada de otra pueden caer el mismo día.
     * Solo cuentan las reservaciones ACTIVA y EN_CURSO; las FINALIZADAS y CANCELADAS
     * nunca se traslapan.
     *
     * @param ini fecha de entrada del otro rango
     * @param fin fecha de salida del otro rango
     * @return {@code true} si la reservación está ACTIVA o EN_CURSO y los rangos se traslapan
     * @throws IllegalArgumentException si alguna fecha es nula
     */
    public boolean seTraslapaCon(LocalDate ini, LocalDate fin) {
        if (ini == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (estado != EstadoReservacion.ACTIVA && estado != EstadoReservacion.EN_CURSO) {
            return false;
        }
        return fechaEntrada.isBefore(fin) && ini.isBefore(fechaSalida);
    }

    /**
     * Cambia las fechas de entrada y salida de la reservación (RN-07).
     * Solo valida el estado y las fechas; la disponibilidad y los traslapes con otras
     * reservaciones los valida {@code Hotel} antes de llamar a este método.
     *
     * @param ini nueva fecha de entrada
     * @param fin nueva fecha de salida
     * @throws IllegalArgumentException     si alguna fecha es nula
     * @throws ReservacionInvalidaException si la reservación no está ACTIVA o la salida
     *                                      no es posterior a la entrada (RN-06)
     */
    public void modificarFechas(LocalDate ini, LocalDate fin) throws ReservacionInvalidaException {
        if (ini == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (estado != EstadoReservacion.ACTIVA) {
            throw new ReservacionInvalidaException(
                    "Solo se pueden modificar las fechas de una reservación ACTIVA; estado actual: " + estado + ".");
        }
        if (!fin.isAfter(ini)) {
            throw new ReservacionInvalidaException(
                    "La fecha de salida debe ser posterior a la fecha de entrada.");
        }
        this.fechaEntrada = ini;
        this.fechaSalida = fin;
    }

    /**
     * Registra el consumo de un servicio con la fecha actual (RN-22, RN-46).
     *
     * @param s        servicio consumido; no puede ser nulo
     * @param cantidad cantidad consumida; debe ser positiva
     * @throws IllegalArgumentException     si el servicio es nulo o la cantidad no es positiva
     * @throws ReservacionInvalidaException si la reservación no está EN_CURSO, ya tiene
     *                                      factura generada o el servicio está dado de baja
     */
    public void agregarConsumo(Servicio s, int cantidad) throws ReservacionInvalidaException {
        if (s == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo.");
        }
        if (estado != EstadoReservacion.EN_CURSO) {
            throw new ReservacionInvalidaException(
                    "Solo se registran consumos en una reservación EN_CURSO; estado actual: " + estado + ".");
        }
        if (factura != null) {
            throw new ReservacionInvalidaException(
                    "La reservación ya tiene factura generada; no se aceptan más consumos.");
        }
        if (!s.isActivo()) {
            throw new ReservacionInvalidaException("El servicio está dado de baja.");
        }
        consumos.add(new ConsumoServicio(s, cantidad, LocalDate.now()));
    }

    /**
     * Inicia la estancia (check-in): la reservación pasa a EN_CURSO y todas sus
     * habitaciones a OCUPADA (RN-13). Si alguna habitación no está DISPONIBLE se rechaza
     * todo el check-in (RN-43).
     *
     * @throws ReservacionInvalidaException si la reservación no está ACTIVA o alguna
     *                                      habitación no está DISPONIBLE
     */
    public void iniciar() throws ReservacionInvalidaException {
        if (estado != EstadoReservacion.ACTIVA) {
            throw new ReservacionInvalidaException(
                    "Solo se puede hacer check-in a una reservación ACTIVA; estado actual: " + estado + ".");
        }
        for (Habitacion h : habitaciones) {
            if (h.getEstado() != EstadoHabitacion.DISPONIBLE) {
                throw new ReservacionInvalidaException("La habitación " + h.getNumero()
                        + " no está DISPONIBLE; estado actual: " + h.getEstado() + ".");
            }
        }
        for (Habitacion h : habitaciones) {
            h.cambiarEstado(EstadoHabitacion.OCUPADA);
        }
        estado = EstadoReservacion.EN_CURSO;
    }

    /**
     * Finaliza la estancia (check-out): la reservación pasa a FINALIZADA y sus
     * habitaciones a LIMPIEZA (RN-13). Exige la factura generada y pagada (RN-27).
     *
     * @throws ReservacionInvalidaException si la reservación no está EN_CURSO
     * @throws PagoPendienteException       si la factura no se ha generado o no está pagada
     */
    public void finalizar() throws ReservacionInvalidaException, PagoPendienteException {
        if (estado != EstadoReservacion.EN_CURSO) {
            throw new ReservacionInvalidaException(
                    "Solo se puede hacer check-out a una reservación EN_CURSO; estado actual: " + estado + ".");
        }
        if (factura == null) {
            throw new PagoPendienteException("La reservación " + id + " no tiene factura generada.");
        }
        if (!factura.isPagada()) {
            throw new PagoPendienteException("La factura de la reservación " + id + " no está pagada.");
        }
        for (Habitacion h : habitaciones) {
            h.cambiarEstado(EstadoHabitacion.LIMPIEZA);
        }
        estado = EstadoReservacion.FINALIZADA;
    }

    /**
     * Cancela la reservación completa (RN-08, RN-10). Si faltan menos de
     * {@link #HORAS_MINIMAS_CANCELACION} horas para la fecha de entrada a las
     * {@link #HORA_CHECK_IN}, registra {@link #CARGO_CANCELACION} una sola vez (RN-09).
     *
     * @param ahora momento en que se solicita la cancelación; no puede ser nulo
     * @throws IllegalArgumentException     si {@code ahora} es nulo
     * @throws ReservacionInvalidaException si la reservación no está ACTIVA
     */
    public void cancelar(LocalDateTime ahora) throws ReservacionInvalidaException {
        if (ahora == null) {
            throw new IllegalArgumentException("El momento de la cancelación no puede ser nulo.");
        }
        if (estado != EstadoReservacion.ACTIVA) {
            throw new ReservacionInvalidaException(
                    "Solo se puede cancelar una reservación ACTIVA; estado actual: " + estado + ".");
        }
        Duration anticipacion = Duration.between(ahora, fechaEntrada.atTime(HORA_CHECK_IN));
        if (anticipacion.compareTo(Duration.ofHours(HORAS_MINIMAS_CANCELACION)) < 0) {
            cargoCancelacion = CARGO_CANCELACION;
        }
        estado = EstadoReservacion.CANCELADA;
    }

    /** @return identificador de la reservación */
    public String getId() {
        return id;
    }

    /** @return fecha de entrada */
    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    /** @return fecha de salida */
    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    /** @return número de huéspedes */
    public int getNumHuespedes() {
        return numHuespedes;
    }

    /** @return estado actual de la reservación */
    public EstadoReservacion getEstado() {
        return estado;
    }

    /** @return cargo por cancelación registrado (0 si no aplica) */
    public double getCargoCancelacion() {
        return cargoCancelacion;
    }

    /** @return huésped que realizó la reservación */
    public Huesped getHuesped() {
        return huesped;
    }

    /** @return vista de solo lectura de las habitaciones reservadas */
    public List<Habitacion> getHabitaciones() {
        return Collections.unmodifiableList(habitaciones);
    }

    /** @return vista de solo lectura de los consumos registrados */
    public List<ConsumoServicio> getConsumos() {
        return Collections.unmodifiableList(consumos);
    }

    /** @return factura de la reservación, o {@code null} si no se ha generado */
    public Factura getFactura() {
        return factura;
    }

    /**
     * Asocia la factura generada a la reservación. La factura se genera una sola vez y
     * solo sobre una reservación EN_CURSO (RN-46).
     *
     * @param factura factura generada; no puede ser nula
     * @throws IllegalArgumentException     si la factura es nula
     * @throws ReservacionInvalidaException si la reservación no está EN_CURSO o ya tiene factura
     */
    public void setFactura(Factura factura) throws ReservacionInvalidaException {
        if (factura == null) {
            throw new IllegalArgumentException("La factura no puede ser nula.");
        }
        if (estado != EstadoReservacion.EN_CURSO) {
            throw new ReservacionInvalidaException(
                    "Solo se factura una reservación EN_CURSO; estado actual: " + estado + ".");
        }
        if (this.factura != null) {
            throw new ReservacionInvalidaException("La reservación " + id + " ya tiene factura generada.");
        }
        this.factura = factura;
    }
}
