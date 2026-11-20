package modelo.personas;

import excepciones.HotelException;

import java.time.LocalDate;

/**
 * Mesero del hotel. Cobra su salario base más las propinas acumuladas en el periodo
 * (RN-31); al cerrar el periodo sus propinas vuelven a cero (RN-50).
 */
public class Mesero extends Empleado {
    /** Propinas acumuladas en el periodo actual (RN-49). */
    protected double propinasAcumuladas;

    /**
     * Crea un mesero activo sin propinas acumuladas.
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
    public Mesero(String id, String nombre, String apellido, String telefono, String correo,
                  double salarioBase, LocalDate fechaIngreso) {
        super(id, nombre, apellido, telefono, correo, salarioBase, fechaIngreso);
        this.propinasAcumuladas = 0.0;
    }

    /**
     * Suma una propina a las propinas acumuladas del periodo (RN-49).
     *
     * @param monto monto de la propina; debe ser positivo
     * @throws HotelException si el mesero está inactivo o el monto no es positivo
     */
    public void registrarPropina(double monto) throws HotelException {
        if (!activo) {
            throw new HotelException("El mesero " + id + " está dado de baja.");
        }
        if (monto <= 0) {
            throw new HotelException("El monto de la propina debe ser positivo.");
        }
        propinasAcumuladas += monto;
    }

    /** @return "Mesero" */
    @Override
    public String getPuesto() {
        return "Mesero";
    }

    /**
     * Calcula el sueldo del mesero: salario base más propinas acumuladas (RN-31).
     *
     * @return sueldo del periodo
     */
    @Override
    public double calcularSueldo() {
        return super.calcularSueldo() + propinasAcumuladas;
    }

    /**
     * Cierra el periodo reiniciando las propinas acumuladas a cero (RN-50).
     */
    @Override
    public void cerrarPeriodo() {
        propinasAcumuladas = 0.0;
    }

    /** @return propinas acumuladas en el periodo actual */
    public double getPropinasAcumuladas() {
        return propinasAcumuladas;
    }
}
