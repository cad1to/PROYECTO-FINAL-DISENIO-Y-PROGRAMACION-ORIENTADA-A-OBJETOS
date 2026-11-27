package modelo.reservas;

import modelo.catalogo.Servicio;

import java.time.LocalDate;

/**
 * Consumo de un servicio registrado durante la estancia de una reservación.
 */
public class ConsumoServicio {
    protected int cantidad;
    protected LocalDate fecha;
    protected Servicio servicio;

    /**
     * Crea un consumo.
     *
     * @param servicio servicio consumido; no puede ser nulo
     * @param cantidad cantidad consumida (platillos, sesiones o prendas); debe ser positiva
     * @param fecha    fecha del consumo; no puede ser nula
     * @throws IllegalArgumentException si algún dato es nulo o la cantidad no es positiva
     */
    public ConsumoServicio(Servicio servicio, int cantidad, LocalDate fecha) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del consumo no puede ser nula.");
        }
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    /**
     * Calcula el costo de este consumo con las reglas de su servicio.
     *
     * @return subtotal del consumo en pesos
     */
    public double calcularSubtotal() {
        return servicio.calcularCosto(cantidad);
    }

    /** @return servicio consumido */
    public Servicio getServicio() {
        return servicio;
    }

    /** @return cantidad consumida */
    public int getCantidad() {
        return cantidad;
    }

    /** @return fecha del consumo */
    public LocalDate getFecha() {
        return fecha;
    }
}
