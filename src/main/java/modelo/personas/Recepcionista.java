package modelo.personas;

import excepciones.HotelException;
import modelo.reservas.Reservacion;

import java.time.LocalDate;

/**
 * Recepcionista del hotel: realiza el check-in y el check-out de las reservaciones
 * (RN-13, RN-44). Cobra solo su salario base (RN-28).
 */
public class Recepcionista extends Empleado {
    /** Turno en el que trabaja el recepcionista (por ejemplo, "Matutino"). */
    protected String turno;

    /**
     * Crea un recepcionista activo.
     *
     * @param id           identificador único del empleado
     * @param nombre       nombre(s)
     * @param apellido     apellido(s)
     * @param telefono     teléfono de contacto
     * @param correo       correo electrónico
     * @param salarioBase  salario base; debe ser positivo
     * @param fechaIngreso fecha de ingreso; no puede ser nula
     * @param turno        turno de trabajo; no puede estar vacío
     * @throws IllegalArgumentException si algún dato es inválido (RN-53)
     */
    public Recepcionista(String id, String nombre, String apellido, String telefono, String correo,
                         double salarioBase, LocalDate fechaIngreso, String turno) {
        super(id, nombre, apellido, telefono, correo, salarioBase, fechaIngreso);
        if (turno == null || turno.isBlank()) {
            throw new IllegalArgumentException("El turno no puede estar vacío.");
        }
        this.turno = turno.trim();
    }

    /**
     * Realiza el check-in de una reservación (RN-13, RN-43, RN-44).
     * Verifica que el recepcionista esté activo y delega en la reservación la validación
     * de su estado y el de sus habitaciones.
     *
     * @param r reservación a la que se hace check-in
     * @throws HotelException si el recepcionista está inactivo o la reservación es nula;
     *                        o la subclase que lance la reservación al validar su estado
     */
    public void hacerCheckIn(Reservacion r) throws HotelException {
        if (!activo) {
            throw new HotelException("El recepcionista " + id + " está dado de baja.");
        }
        if (r == null) {
            throw new HotelException("La reservación no puede ser nula.");
        }
        r.iniciar();
    }

    /**
     * Realiza el check-out de una reservación (RN-13, RN-27, RN-44).
     * Verifica que el recepcionista esté activo y delega en la reservación la validación
     * de su estado y de la factura pagada.
     *
     * @param r reservación a la que se hace check-out
     * @throws HotelException si el recepcionista está inactivo o la reservación es nula;
     *                        o la subclase que lance la reservación al validar su estado
     *                        o el pago
     */
    public void hacerCheckOut(Reservacion r) throws HotelException {
        if (!activo) {
            throw new HotelException("El recepcionista " + id + " está dado de baja.");
        }
        if (r == null) {
            throw new HotelException("La reservación no puede ser nula.");
        }
        r.finalizar();
    }

    /** @return "Recepcionista" */
    @Override
    public String getPuesto() {
        return "Recepcionista";
    }

    /** @return turno de trabajo */
    public String getTurno() {
        return turno;
    }
}
