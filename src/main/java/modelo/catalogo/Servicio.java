package modelo.catalogo;

import modelo.comun.Desactivable;
import modelo.facturacion.Facturable;

public abstract class Servicio implements Facturable, Desactivable {
    protected String codigo;
    protected String nombre;
    protected double precioBase;
    protected CategoriaServicio categoria;
    protected boolean activo;

    @Override
    public abstract double calcularCosto(int cantidad);

    @Override
    public String getConcepto() {
    }

    @Override
    public boolean isActivo() {
    }

    @Override
    public void darDeBaja() {
    }
}
