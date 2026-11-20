package modelo.alojamiento;

import modelo.comun.Desactivable;
import modelo.facturacion.Facturable;

public abstract class Habitacion implements Facturable, Desactivable {
    protected int numero;
    protected int piso;
    protected int capacidad;
    protected double tarifaBase;
    protected EstadoHabitacion estado;
    protected boolean activo;

    public abstract double calcularTarifa(int noches);

    public double calcularCosto(int noches) {
    }

    public String getConcepto() {
    }

    public void cambiarEstado(EstadoHabitacion e) {
    }

    /** @return estado actual de la habitación */
    public EstadoHabitacion getEstado() {
        return estado;
    }

    @Override
    public boolean isActivo() {
    }

    @Override
    public void darDeBaja() {
    }
}
