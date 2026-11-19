package modelo.personas;

import java.time.LocalDate;

public abstract class Empleado extends Persona {
    protected double salarioBase;
    protected LocalDate fechaIngreso;

    public abstract String getPuesto();

    public double calcularSueldo() {
    }

    public void cerrarPeriodo() {
    }

    @Override
    public String descripcion() {
    }
}
