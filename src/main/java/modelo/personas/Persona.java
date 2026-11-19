package modelo.personas;

import modelo.comun.Desactivable;

public abstract class Persona implements Desactivable {
    protected String id;
    protected String nombre;
    protected String apellido;
    protected String telefono;
    protected String correo;
    protected boolean activo;

    public String getNombreCompleto() {
    }

    @Override
    public boolean isActivo() {
    }

    @Override
    public void darDeBaja() {
    }

    public abstract String descripcion();
}
