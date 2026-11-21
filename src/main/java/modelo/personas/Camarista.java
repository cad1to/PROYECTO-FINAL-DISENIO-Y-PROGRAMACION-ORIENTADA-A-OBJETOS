package modelo.personas;

import excepciones.HotelException;
import modelo.alojamiento.EstadoHabitacion;
import modelo.alojamiento.Habitacion;

import java.time.LocalDate;

/**
 * Camarista del hotel: registra la limpieza de las habitaciones (RN-48).
 * Cobra solo su salario base (RN-29, D-08).
 */
public class Camarista extends Empleado {

    /**
     * Crea una camarista activa.
     *
     * @param id           identificador único del empleado
     * @param nombre       nombre(s)
     * @param apellido     apellido(s)
     * @param telefono     teléfono de contacto
     * @param correo       correo electrónico
     * @param salarioBase  salario base; debe ser positivo
     * @param fechaIngreso fecha de ingreso; no puede ser nula
     * @throws IllegalArgumentException si algún dato es inválido (RN-53)
     */
    public Camarista(String id, String nombre, String apellido, String telefono, String correo,
                     double salarioBase, LocalDate fechaIngreso) {
        super(id, nombre, apellido, telefono, correo, salarioBase, fechaIngreso);
    }

    /**
     * Registra la limpieza de una habitación: pasa de LIMPIEZA a DISPONIBLE (RN-48).
     *
     * @param h habitación limpiada
     * @throws HotelException si la camarista está inactiva, la habitación es nula
     *                        o la habitación no está en LIMPIEZA
     */
    public void registrarLimpieza(Habitacion h) throws HotelException {
        if (!activo) {
            throw new HotelException("La camarista " + id + " está dada de baja.");
        }
        if (h == null) {
            throw new HotelException("La habitación no puede ser nula.");
        }
        if (h.getEstado() != EstadoHabitacion.LIMPIEZA) {
            throw new HotelException("La habitación no está en LIMPIEZA; estado actual: "
                    + h.getEstado() + ".");
        }
        h.cambiarEstado(EstadoHabitacion.DISPONIBLE);
    }

    /** @return "Camarista" */
    @Override
    public String getPuesto() {
        return "Camarista";
    }

    //creo que no se hizo el push anterior
}
