package modelo.personas;

import modelo.comun.Desactivable;

/**
 * Clase base abstracta para todas las personas del hotel (huéspedes y empleados).
 * <p>
 * Implementa {@link Desactivable} para soportar la baja lógica (RN-34): una persona
 * nunca se elimina físicamente, solo se marca como inactiva para conservar el historial.
 *
 */
public abstract class Persona implements Desactivable {
    /**
     * Identificador único de la persona dentro del hotel (RN-51).
     */
    protected String id;
    /**
     * Nombre(s) de la persona.
     */
    protected String nombre;
    /**
     * Apellido(s) de la persona.
     */
    protected String apellido;
    /**
     * Teléfono de contacto.
     */
    protected String telefono;
    /**
     * Correo electrónico de contacto.
     */
    protected String correo;
    /**
     * Indica si la persona está activa; {@code false} significa que fue dada de baja.
     */
    protected boolean activo;

    /**
     * Crea una persona activa.
     *
     * @param id       identificador único; no puede estar vacío
     * @param nombre   nombre(s); no puede estar vacío
     * @param apellido apellido(s); no puede estar vacío
     * @param telefono teléfono de contacto; no puede estar vacío
     * @param correo   correo electrónico; no puede estar vacío
     * @throws IllegalArgumentException si algún texto es nulo o vacío (RN-53)
     */
    protected Persona(String id, String nombre, String apellido, String telefono, String correo) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
        this.correo = correo.trim();
        this.activo = true;
    }

    /**
     * Devuelve el nombre y el apellido separados por un espacio.
     *
     * @return nombre completo de la persona
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Indica si la persona sigue activa en el sistema.
     *
     * @return {@code true} si está activa, {@code false} si fue dada de baja
     */
    @Override
    public boolean isActivo() {
        return activo;
    }

    /**
     * Da de baja lógicamente a la persona ({@code activo = false}, RN-34).
     * Las validaciones que cruzan entidades (por ejemplo RN-55) las hace {@code Hotel}.
     */
    @Override
    public void darDeBaja() {
        this.activo = false;
    }

    /**
     * Devuelve una descripción legible de la persona; cada subclase la define.
     *
     * @return descripción de la persona
     */
    public abstract String descripcion();

    /**
     * @return identificador de la persona
     */
    public String getId() {
        return id;
    }

    /**
     * @return teléfono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @return correo electrónico de contacto
     */
    public String getCorreo() {
        return correo;
    }
}
