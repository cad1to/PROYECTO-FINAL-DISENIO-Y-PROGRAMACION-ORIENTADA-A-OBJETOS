package modelo.personas;

import java.time.LocalDate;

/**
 * Clase base abstracta para los empleados del hotel.
 * <p>
 * Segundo nivel de la jerarquía {@code Persona → Empleado → (Recepcionista, Camarista,
 * Gerente, Mesero)}. Define el cálculo de sueldo base que heredan los roles (RN-28, RN-29)
 * y que extienden el gerente y el mesero (RN-30, RN-31).
 * </p>
 */
public abstract class Empleado extends Persona {
    /** Salario base del periodo; es el único dato actualizable del empleado (RN-54). */
    protected double salarioBase;
    /** Fecha en la que el empleado ingresó al hotel. */
    protected LocalDate fechaIngreso;

    /**
     * Crea un empleado activo.
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
    protected Empleado(String id, String nombre, String apellido, String telefono, String correo,
                       double salarioBase, LocalDate fechaIngreso) {
        super(id, nombre, apellido, telefono, correo);
        if (salarioBase <= 0) {
            throw new IllegalArgumentException("El salario base debe ser positivo.");
        }
        this.salarioBase = salarioBase;
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * Devuelve el nombre del puesto del empleado; cada rol lo define.
     *
     * @return nombre del puesto
     */
    public abstract String getPuesto();

    /**
     * Calcula el sueldo del periodo. Por defecto es el salario base (RN-28, RN-29);
     * las subclases pueden extenderlo con {@code super.calcularSueldo()}.
     * No modifica al empleado (RN-50).
     *
     * @return sueldo del periodo
     */
    public double calcularSueldo() {
        return salarioBase;
    }

    /**
     * Cierra el periodo de nómina del empleado (RN-50). Por defecto no cambia nada;
     * los roles con acumulados del periodo (como el mesero) lo sobrescriben.
     */
    public void cerrarPeriodo() {
        // Los roles sin acumulados del periodo no cambian al cerrar.
    }

    /**
     * @return descripción con puesto, id, nombre completo, sueldo y estado del empleado
     */
    @Override
    public String descripcion() {
        return String.format("%s | ID: %s | %s | Ingreso: %s | Sueldo: $%.2f%s",
                getPuesto(), id, getNombreCompleto(), fechaIngreso, calcularSueldo(),
                activo ? "" : " | (BAJA)");
    }

    /** @return salario base del empleado */
    public double getSalarioBase() {
        return salarioBase;
    }

    /**
     * Actualiza el salario base (RN-54).
     *
     * @param salarioBase nuevo salario base; debe ser positivo
     * @throws IllegalArgumentException si el salario no es positivo (RN-53)
     */
    public void setSalarioBase(double salarioBase) {
        if (salarioBase <= 0) {
            throw new IllegalArgumentException("El salario base debe ser positivo.");
        }
        this.salarioBase = salarioBase;
    }

    /** @return fecha de ingreso del empleado */
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
}
