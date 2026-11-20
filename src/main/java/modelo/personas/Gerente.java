package modelo.personas;

import java.time.LocalDate;

/**
 * Gerente del hotel. Cobra su salario base más un bono mensual (RN-30).
 */
public class Gerente extends Empleado {
    /** Bono que se suma al salario base cada periodo. */
    protected double bonoMensual;

    /**
     * Crea un gerente activo.
     *
     * @param id           identificador único del empleado
     * @param nombre       nombre(s)
     * @param apellido     apellido(s)
     * @param telefono     teléfono de contacto
     * @param correo       correo electrónico
     * @param salarioBase  salario base; debe ser positivo
     * @param fechaIngreso fecha de ingreso; no puede ser nula
     * @param bonoMensual  bono mensual; no puede ser negativo
     * @throws IllegalArgumentException si algún dato es inválido (RN-53)
     */
    public Gerente(String id, String nombre, String apellido, String telefono, String correo,
                   double salarioBase, LocalDate fechaIngreso, double bonoMensual) {
        super(id, nombre, apellido, telefono, correo, salarioBase, fechaIngreso);
        if (bonoMensual < 0) {
            throw new IllegalArgumentException("El bono mensual no puede ser negativo.");
        }
        this.bonoMensual = bonoMensual;
    }

    /** @return "Gerente" */
    @Override
    public String getPuesto() {
        return "Gerente";
    }

    /**
     * Calcula el sueldo del gerente: salario base más bono mensual (RN-30).
     *
     * @return sueldo del periodo
     */
    @Override
    public double calcularSueldo() {
        return super.calcularSueldo() + bonoMensual;
    }

    /** @return bono mensual del gerente */
    public double getBonoMensual() {
        return bonoMensual;
    }
}
