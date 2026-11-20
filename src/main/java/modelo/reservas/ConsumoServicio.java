package modelo.reservas;

import modelo.catalogo.Servicio;

import java.time.LocalDate;

/**
 * Servicio del catálogo consumido dentro de una reservación.
 * <p>
 * Forma parte de la reservación por composición: no existe fuera de ella (RN-23).
 * Conserva la referencia al servicio aunque este se dé de baja después (RN-38).
 * </p>
 */
public class ConsumoServicio {
    /** Cantidad consumida (platillos, sesiones o prendas, según el servicio). */
    protected int cantidad;
    /** Fecha en la que se registró el consumo. */
    protected LocalDate fecha;
    /** Servicio del catálogo que se consumió. */
    protected Servicio servicio;

    /**
     * Crea un consumo de servicio.
     *
     * @param servicio servicio consumido; no puede ser nulo
     * @param cantidad cantidad consumida; debe ser positiva
     * @param fecha    fecha del consumo; no puede ser nula
     * @throws IllegalArgumentException si algún dato es inválido (RN-53)
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
     * Calcula el subtotal del consumo con la regla de costo del servicio (RN-19 a RN-21).
     *
     * @return {@code servicio.calcularCosto(cantidad)}
     */
    public double calcularSubtotal() {
        return servicio.calcularCosto(cantidad);
    }

    /** @return cantidad consumida */
    public int getCantidad() {
        return cantidad;
    }

    /** @return fecha del consumo */
    public LocalDate getFecha() {
        return fecha;
    }

    /** @return servicio consumido */
    public Servicio getServicio() {
        return servicio;
    }
}
