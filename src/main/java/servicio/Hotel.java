package servicio;

import excepciones.BajaNoPermitidaException;
import excepciones.HotelException;
import excepciones.PagoPendienteException;
import excepciones.RegistroDuplicadoException;
import excepciones.RegistroNoEncontradoException;
import excepciones.ReservacionInvalidaException;
import modelo.alojamiento.*;
import modelo.catalogo.Servicio;
import modelo.facturacion.Factura;
import modelo.personas.Camarista;
import modelo.personas.Empleado;
import modelo.personas.Huesped;
import modelo.personas.Mesero;
import modelo.personas.Recepcionista;
import modelo.personas.TipoHuesped;
import modelo.reservas.ConsumoServicio;
import modelo.reservas.EstadoReservacion;
import modelo.reservas.Reservacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    /*
      ================== Encapsulamiento ===============
      ¿Porque no private?

      Debi al acutal proyecto no tiene ningun sentido tenerlo como protected debido a que es la
      unica clase dentro de nuestro package Servicio. Pero pensando algo en el futuro, la posinilidad
      de servicios independientes donde requieran los mismos empleados o los mismos huesped, como un
      Administrador de Rentas de Casas, no sea necesario pasarlos por getters y sea de forma directa.
     */
    protected String nombre;
    protected String direccion;
    protected List<Habitacion> habitaciones;
    protected List<Empleado> empleados;
    protected List<Huesped> huespedes;
    protected List<Servicio> servicios;
    protected List<Reservacion> reservaciones;

    public Hotel(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.habitaciones = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.huespedes = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.reservaciones = new ArrayList<>();
    }

    /**
     * Registra un huésped nuevo en el hotel.
     * <p>
     * El id del huésped debe ser único dentro del hotel; por ello se compara
     * contra todos los huéspedes registrados, incluso los dados de baja,
     * porque la baja es lógica y el id sigue ocupado.
     *
     * @param h huésped que se va a registrar
     * @throws RegistroDuplicadoException si ya existe un huésped con el mismo id
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void registrarHuesped(Huesped h) throws HotelException {
        for (Huesped existente : huespedes) {
            if (existente.getId().equals(h.getId())) {
                throw new RegistroDuplicadoException("Ya existe un huésped con el id " + h.getId());
            }
        }
        huespedes.add(h);
    }

    /**
     * Registra un nuevo empleado en la lista del Hotel (RF-02).
     * <p>
     * El id debe ser único entre todos los empleados (RN-51); se debe comparar debido a que
     * las bajas en el hotel son lógicas (RN-34), por ello el id sigue ocupado.
     *
     * @param e empleado nuevo a registrar
     * @throws RegistroDuplicadoException si ya existe un empleado con el mismo id
     * @throws HotelException excepción base para todas las reglas del hotel
     */
    public void registrarEmpleado(Empleado e) throws HotelException {
        for (Empleado existente : empleados) {
            if (existente.getId().equals(e.getId())) {
                throw new RegistroDuplicadoException("Ya existe un empleado con el id "+e.getId());
            }
        }
        empleados.add(e);
    }

    /**
     * Registra una nueva habitación en el hotel (RF-03).
     * <p>
     * El número debe ser único dentro del hotel (RN-11); se compara contra todas las
     * habitaciones, incluso las dadas de baja, porque la baja es lógica y el número sigue
     * ocupado (RN-34).
     *
     * @param h habitación nueva en el hotel
     * @throws RegistroDuplicadoException si ya existe una habitación con el mismo número
     * @throws HotelException excepción base para todas las reglas del hotel
     */
    public void registrarHabitacion(Habitacion h) throws HotelException {
        for (Habitacion existente : habitaciones) {
            if (existente.getNumero() == h.getNumero()) {
                throw new RegistroDuplicadoException("Ya existe la habitación con el número: " + h.getNumero());
            }
        }
        habitaciones.add(h);
    }

    /**
     * Registra un nuevo servicio en el catálogo del hotel (RF-04).
     * <p>
     * El código debe ser único dentro del hotel (RN-33); se compara contra todos
     * los servicios, incluso los dados de baja, porque la baja es lógica y el
     * código sigue ocupado (RN-34). El parámetro puede ser cualquier subclase de
     * {@code Servicio} (restaurante, spa o lavandería).
     *
     * @param s servicio nuevo a registrar
     * @throws RegistroDuplicadoException si ya existe un servicio con el mismo código
     * @throws HotelException             excepción base de las reglas del hotel
     */
    public void registrarServicio(Servicio s) throws HotelException {
        for (Servicio existente : servicios) {
            if (s.getCodigo().equals(existente.getCodigo())) {
                throw new RegistroDuplicadoException("Ya existe un servicio con el código "+s.getCodigo());
            }
        }
        servicios.add(s);
    }

     /*
        Agregación de métodos de búsqueda

        Para evitar repetir ciertas líneas de búsqueda, decidí implementar métodos que se dedicaran a ello
        para evitar repetir código y reciclar funciones.

        Búsqueda de huéspedes, empleados, habitaciones, servicios y reservaciones
      */

    /**
     * Busca un huésped activo por su id.
     * <p>
     * Un huésped dado de baja se trata como no encontrado (RN-56). Es un método
     * auxiliar interno que usan las operaciones de {@code Hotel} que necesitan
     * un huésped registrado y activo (RN-01).
     *
     * @param id id del huésped que se busca
     * @return el huésped activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Huesped buscarHuesped(String id) throws RegistroNoEncontradoException {
        for (Huesped existente : huespedes) {
            if (existente.getId().equals(id)){
                if (existente.isActivo()){
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El huésped "+id+" no fue encontrado");
    }

    /**
     * Busca un empleado activo por su id.
     * <p>
     * Un empleado dado de baja se trata como no encontrado (RN-56).
     *
     * @param id id del empleado que se busca
     * @return el empleado activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Empleado buscarEmpleado(String id) throws RegistroNoEncontradoException {
        for (Empleado existente : empleados) {
            if (existente.getId().equals(id)) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El empleado " + id + " no fue encontrado");
    }

    /**
     * Busca una habitación activa por su número.
     * <p>
     * Una habitación dada de baja se trata como no encontrada (RN-56).
     *
     * @param numero número de la habitación que se busca
     * @return la habitación activa con ese número
     * @throws RegistroNoEncontradoException si no existe o está dada de baja
     */
    private Habitacion buscarHabitacion(int numero) throws RegistroNoEncontradoException {
        for (Habitacion existente : habitaciones) {
            if (existente.getNumero() == numero) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("La habitación " + numero + " no fue encontrada");
    }

    /**
     * Busca un servicio activo por su código.
     * <p>
     * Un servicio dado de baja se trata como no encontrado (RN-56).
     *
     * @param codigo código del servicio que se busca
     * @return el servicio activo con ese código
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     */
    private Servicio buscarServicio(String codigo) throws RegistroNoEncontradoException {
        for (Servicio existente : servicios) {
            if (existente.getCodigo().equals(codigo)) {
                if (existente.isActivo()) {
                    return existente;
                }
                break;
            }
        }
        throw new RegistroNoEncontradoException("El servicio " + codigo + " no fue encontrado");
    }

    /**
     * Busca una reservación por su id.
     * <p>
     * Las reservaciones no tienen baja lógica: se cancelan y se conservan con su
     * estado (RN-37), por eso aquí no se revisa si están activas.
     *
     * @param id id de la reservación que se busca
     * @return la reservación con ese id
     * @throws RegistroNoEncontradoException si no existe ninguna con ese id
     */
    private Reservacion buscarReservacion(String id) throws RegistroNoEncontradoException {
        for (Reservacion existente : reservaciones) {
            if (existente.getId().equals(id)) {
                return existente;
            }
        }
        throw new RegistroNoEncontradoException("La reservación " + id + " no fue encontrada");
    }


    /**
     * Crea una reservación ACTIVA para un huésped (RF-05).
     * <p>
     * El huésped debe estar registrado y activo (RN-01). Todas las habitaciones solicitadas
     * deben estar disponibles en el rango (RN-40); si alguna no lo está se rechaza toda la
     * reservación y no queda ninguna parcial (RN-03). La excepción indica cuáles habitaciones
     * no están disponibles para que la interfaz pueda informarlo. Las fechas (RN-06) y la
     * capacidad (RN-05) las valida {@code Reservacion}. Se permiten fechas anteriores a hoy
     * (RN-41). El id lo genera el sistema.
     *
     * @param idHuesped     id del huésped que reserva
     * @param numerosHab    números de las habitaciones que se reservan; al menos una
     * @param ini           fecha de entrada
     * @param fin           fecha de salida
     * @param numHuespedes  número de huéspedes; no puede superar la capacidad total
     * @return la reservación creada y registrada en el hotel
     * @throws RegistroNoEncontradoException si el huésped o alguna habitación no existe o está
     *                                       dado de baja
     * @throws ReservacionInvalidaException  si no se indica ninguna habitación, alguna no está
     *                                       disponible, las fechas son inválidas o se excede
     *                                       la capacidad
     * @throws HotelException excepción base de las reglas del hotel
     */
    public Reservacion crearReservacion(String idHuesped, List<Integer> numerosHab, LocalDate ini, LocalDate fin,
                                        int numHuespedes) throws HotelException {
        Huesped huesped = buscarHuesped(idHuesped);
        if (numerosHab == null || numerosHab.isEmpty()) {
            throw new ReservacionInvalidaException("Debe seleccionar al menos una habitación.");
        }

        List<Habitacion> habitacionesReserva = new ArrayList<>();
        List<Integer> noDisponibles = new ArrayList<>();
        for (Integer numero : numerosHab) {
            Habitacion habitacion = buscarHabitacion(numero);
            if (!estaDisponible(habitacion, ini, fin, null)) {
                noDisponibles.add(numero);
            } else if (!habitacionesReserva.contains(habitacion)) {
                habitacionesReserva.add(habitacion);
            }
        }
        if (!noDisponibles.isEmpty()) {
            throw new ReservacionInvalidaException("Las habitaciones " + noDisponibles
                    + " no están disponibles del " + ini + " al " + fin + ".");
        }

        String idReservacion = String.valueOf(reservaciones.size() + 1);
        Reservacion reservacion = new Reservacion(idReservacion, huesped, habitacionesReserva, ini, fin, numHuespedes);
        reservaciones.add(reservacion);
        return reservacion;
    }

    /**
     * Lista los huéspedes activos del hotel (RF-06).
     * <p>
     * Los huéspedes dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de huéspedes activos; vacía si no hay ninguno
     */
    public List<Huesped> listarHuespedes() {
        List<Huesped> huespedesActivos = new ArrayList<>();
        for (Huesped existente : huespedes) {
            if (existente.isActivo()) {
                huespedesActivos.add(existente);
            }
        }
        return huespedesActivos;
    }

    /**
     * Lista los empleados activos del hotel (RF-06).
     * <p>
     * Los empleados dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de empleados activos; vacía si no hay ninguno
     */
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleadosActivos = new ArrayList<>();
        for (Empleado existente : empleados) {
            if (existente.isActivo()) {
                empleadosActivos.add(existente);
            }
        }
        return empleadosActivos;
    }

    /**
     * Lista las habitaciones activas del hotel (RF-06).
     * <p>
     * Las habitaciones dadas de baja no aparecen en la consulta (RN-35), aunque se
     * conservan en el hotel (RN-34). Devuelve una lista nueva, por lo que modificarla
     * no altera la colección interna (RN-39).
     *
     * @return lista de habitaciones activas; vacía si no hay ninguna
     */
    public List<Habitacion> listarHabitaciones() {
        List<Habitacion> habitacionesActivas = new ArrayList<>();
        for (Habitacion existente : habitaciones) {
            if (existente.isActivo()) {
                habitacionesActivas.add(existente);
            }
        }
        return habitacionesActivas;
    }

    /**
     * Lista los servicios activos del catálogo del hotel (RF-06).
     * <p>
     * Los servicios dados de baja no aparecen en la consulta (RN-35), aunque se
     * conservan para el historial de las facturas (RN-34, RN-38). Devuelve una lista
     * nueva, por lo que modificarla no altera la colección interna (RN-39).
     *
     * @return lista de servicios activos; vacía si no hay ninguno
     */
    public List<Servicio> listarServicios() {
        List<Servicio> serviciosActivos = new ArrayList<>();
        for (Servicio existente : servicios) {
            if (existente.isActivo()) {
                serviciosActivos.add(existente);
            }
        }
        return serviciosActivos;
    }

    /**
     * Lista todas las reservaciones del hotel con su estado (RF-06).
     * <p>
     * A diferencia de los demás listados, no se filtran: se incluyen también las
     * FINALIZADAS y CANCELADAS (RN-35). Devuelve una copia de la colección, por lo
     * que modificarla no altera la lista interna (RN-39).
     *
     * @return lista con todas las reservaciones; vacía si no hay ninguna
     */
    public List<Reservacion> listarReservaciones() {
        return new ArrayList<>(reservaciones);
    }

    /**
     * Busca las habitaciones disponibles en un rango de fechas (RF-06).
     * <p>
     * Una habitación está disponible si está activa, no está en MANTENIMIENTO (RN-14) y no
     * se traslapa con ninguna reservación ACTIVA o EN_CURSO (RN-02, RN-40); la salida de una
     * reservación y la entrada de otra pueden caer el mismo día. Su estado actual (OCUPADA,
     * LIMPIEZA) no impide reservar fechas futuras. Devuelve una lista nueva, por lo que
     * modificarla no altera la colección interna (RN-39).
     *
     * @param ini fecha de entrada del rango
     * @param fin fecha de salida del rango
     * @return lista de habitaciones disponibles; vacía si no hay ninguna
     */
    public List<Habitacion> buscarDisponibles(LocalDate ini, LocalDate fin) {
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion habitacion : listarHabitaciones()) {
            // null: no se ignora ninguna reservación; todas cuentan para el traslape
            if (estaDisponible(habitacion, ini, fin, null)) {
                disponibles.add(habitacion);
            }
        }
        return disponibles;
    }

    /**
     * Actualiza el tipo de un huésped (RF-07).
     * <p>
     * Del huésped solo puede actualizarse su tipo, REGULAR o VIP (RN-36, RN-54).
     * Un huésped dado de baja se trata como no encontrado (RN-56).
     *
     * @param id id del huésped que se actualiza
     * @param t  nuevo tipo del huésped; no puede ser nulo
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void actualizarTipoHuesped(String id, TipoHuesped t) throws HotelException {
        Huesped huesped = buscarHuesped(id);
        huesped.setTipo(t);
    }

    /**
     * Actualiza el salario base de un empleado (RF-07).
     * <p>
     * Del empleado solo puede actualizarse su salario base, que debe ser positivo
     * (RN-53, RN-54). Un empleado dado de baja se trata como no encontrado (RN-56).
     *
     * @param id      id del empleado que se actualiza
     * @param salario nuevo salario base; debe ser positivo
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void actualizarSalarioEmpleado(String id, double salario) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        empleado.setSalarioBase(salario);
    }

    /**
     * Actualiza el estado de una habitación (RF-07).
     * <p>
     * El estado OCUPADA solo lo asigna el check-in y solo lo libera el check-out (RN-42):
     * no se puede asignar OCUPADA manualmente ni cambiar el estado de una habitación que
     * ya está OCUPADA. Una habitación dada de baja se trata como no encontrada (RN-56).
     *
     * @param num número de la habitación que se actualiza
     * @param e   nuevo estado de la habitación
     * @throws RegistroNoEncontradoException si no existe o está dada de baja
     * @throws HotelException si se intenta asignar OCUPADA o cambiar el estado de una habitación OCUPADA (RN-42)
     */
    public void actualizarEstadoHabitacion(int num, EstadoHabitacion e) throws HotelException {
        Habitacion habitacion = buscarHabitacion(num);
        if (e == EstadoHabitacion.OCUPADA) {
            throw new HotelException("El estado OCUPADA solo lo asigna el check-in.");
        }
        if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
            throw new HotelException("La habitación " + num + " está OCUPADA; solo el check-out la libera.");
        }
        habitacion.cambiarEstado(e);
    }

    /**
     * Actualiza la tarifa base de una habitación (RF-07).
     * <p>
     * La tarifa debe ser positiva (RN-53, RN-54). El cambio no modifica las facturas ya
     * generadas, porque cada detalle conserva su concepto, cantidad y monto (RN-45).
     * Una habitación dada de baja se trata como no encontrada (RN-56).
     *
     * @param num    número de la habitación que se actualiza
     * @param tarifa nueva tarifa base por noche; debe ser positiva
     * @throws RegistroNoEncontradoException si no existe o está dada de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void actualizarTarifaHabitacion(int num, double tarifa) throws HotelException {
        Habitacion habitacion = buscarHabitacion(num);
        habitacion.setTarifaBase(tarifa);
    }

    /**
     * Actualiza el precio base de un servicio del catálogo (RF-07).
     * <p>
     * El precio debe ser positivo (RN-53, RN-54). El cambio no modifica las facturas ya
     * generadas, porque cada detalle conserva su concepto, cantidad y monto (RN-45).
     * Un servicio dado de baja se trata como no encontrado (RN-56).
     *
     * @param codigo código del servicio que se actualiza
     * @param precio nuevo precio base; debe ser positivo
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void actualizarPrecioServicio(String codigo, double precio) throws HotelException {
        Servicio servicio = buscarServicio(codigo);
        servicio.setPrecioBase(precio);
    }

    /**
     * Indica si una habitación está disponible para un rango de fechas (RN-40).
     * <p>
     * Está disponible si está activa, no está en MANTENIMIENTO (RN-14) y ninguna otra
     * reservación ACTIVA o EN_CURSO que la incluya se traslapa con el rango (RN-02). Su
     * estado actual (OCUPADA, LIMPIEZA) no impide reservar fechas futuras.
     *
     * @param habitacion habitación que se revisa
     * @param ini fecha de entrada del rango
     * @param fin fecha de salida del rango
     * @param ignorada reservación que no cuenta para el traslape (la que se está
     *                 modificando), o {@code null} para considerarlas todas
     * @return {@code true} si la habitación puede reservarse en el rango
     */
    private boolean estaDisponible(Habitacion habitacion, LocalDate ini, LocalDate fin, Reservacion ignorada) {
        if (!habitacion.isActivo() || habitacion.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
            return false;
        }
        for (Reservacion existente : reservaciones) {
            if (existente != ignorada
                    && existente.getHabitaciones().contains(habitacion)
                    && existente.seTraslapaCon(ini, fin)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Modifica las fechas de una reservación ACTIVA (RF-07).
     * <p>
     * Solo se pueden modificar las fechas de una reservación ACTIVA y la salida debe ser
     * posterior a la entrada (RN-07, RN-06); ambas cosas las valida {@code Reservacion}.
     * Hotel vuelve a validar que todas sus habitaciones estén disponibles en el nuevo rango
     * (RN-40), sin contar la propia reservación en el traslape (RN-02). Se permiten fechas
     * anteriores a hoy (RN-41). Si alguna habitación no está disponible se rechaza todo el
     * cambio y la reservación conserva sus fechas originales.
     *
     * @param idRes id de la reservación que se modifica
     * @param ini   nueva fecha de entrada
     * @param fin   nueva fecha de salida
     * @throws RegistroNoEncontradoException si no existe una reservación con ese id
     * @throws ReservacionInvalidaException  si la reservación no está ACTIVA, las fechas son
     *                                       inválidas o alguna habitación no está disponible
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void modificarFechasReservacion(String idRes, LocalDate ini, LocalDate fin) throws HotelException {
        Reservacion reservacion = buscarReservacion(idRes);
        LocalDate entradaAnterior = reservacion.getFechaEntrada();
        LocalDate salidaAnterior = reservacion.getFechaSalida();
        reservacion.modificarFechas(ini, fin);
        for (Habitacion habitacion : reservacion.getHabitaciones()) {
            if (!estaDisponible(habitacion, ini, fin, reservacion)) {
                reservacion.modificarFechas(entradaAnterior, salidaAnterior);
                throw new ReservacionInvalidaException("La habitación " + habitacion.getNumero()
                        + " no está disponible del " + ini + " al " + fin + ".");
            }
        }
    }

    /**
     * Da de baja lógica a un huésped y cancela sus reservaciones ACTIVA (RF-08, RF-09).
     * <p>
     * El huésped no se elimina: se marca como inactivo para conservar el historial
     * (RN-34). Si tiene una reservación EN_CURSO la baja se rechaza (RN-55). Si no, se
     * cancelan todas sus reservaciones ACTIVA con las reglas de RN-08 a RN-10, usando el
     * mismo momento para todas. La validación se hace antes de cancelar: si se rechaza la
     * baja, no se cancela nada. Como un huésped dado de baja se trata como no encontrado
     * (RN-56), darlo de baja dos veces lanza la misma excepción que un id inexistente.
     *
     * @param id id del huésped que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws BajaNoPermitidaException si tiene una reservación EN_CURSO
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaHuesped(String id) throws HotelException {
        Huesped huesped = buscarHuesped(id);
        List<Reservacion> reservacionesActivas = new ArrayList<>();
        for (Reservacion existente : reservaciones) {
            if (existente.getHuesped().equals(huesped)) {
                if (existente.getEstado() ==  EstadoReservacion.EN_CURSO){
                    throw new BajaNoPermitidaException("El huesped "+id+" tiene una reservacion en curso.");
                }
                if (existente.getEstado() ==  EstadoReservacion.ACTIVA) {
                    reservacionesActivas.add(existente);
                }
            }
        }
        LocalDateTime ahora = LocalDateTime.now();
        for (Reservacion existente : reservacionesActivas) {
            cancelarReservacion(existente.getId(), ahora);
        }
        huesped.darDeBaja();
    }

    /**
     * Da de baja lógica a un empleado (RF-08).
     * <p>
     * El empleado no se elimina: se marca como inactivo para conservar el historial
     * (RN-34). Como un empleado dado de baja se trata como no encontrado (RN-56),
     * darlo de baja dos veces lanza la misma excepción que un id inexistente.
     *
     * @param id id del empleado que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaEmpleado(String id) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        empleado.darDeBaja();
    }

    /**
     * Da de baja lógica a una habitación (RF-08, RF-09).
     * <p>
     * La habitación no se elimina: se marca como inactiva para conservar el historial
     * (RN-34). Se rechaza la baja si la habitación pertenece a alguna reservación ACTIVA
     * o EN_CURSO (RN-15); las FINALIZADAS y CANCELADAS no la bloquean. La validación se
     * hace antes de la baja, por lo que si se rechaza la habitación no cambia. Como una
     * habitación dada de baja se trata como no encontrada (RN-56), darla de baja dos
     * veces lanza la misma excepción que un número inexistente.
     *
     * @param num número de la habitación que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dada de baja
     * @throws BajaNoPermitidaException si tiene reservaciones ACTIVA o EN_CURSO
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaHabitacion(int num) throws HotelException {
        Habitacion habitacion = buscarHabitacion(num);
        for (Reservacion reservacionExistente : reservaciones) {
            EstadoReservacion estadoReservacion = reservacionExistente.getEstado();
            if (estadoReservacion == EstadoReservacion.ACTIVA || estadoReservacion == EstadoReservacion.EN_CURSO) {
                for (Habitacion habitacionExistente : reservacionExistente.getHabitaciones()) {
                    if (habitacionExistente == habitacion) {
                        throw new BajaNoPermitidaException("La habitación " + num
                                + " tiene reservaciones ACTIVA o EN_CURSO.");
                    }
                }
            }
        }
        habitacion.darDeBaja();
    }

    /**
     * Da de baja lógica a un servicio del catálogo (RF-08).
     * <p>
     * El servicio no se elimina: se marca como inactivo, deja de ofrecerse en el
     * catálogo pero sus consumos anteriores se conservan en las facturas (RN-34, RN-38).
     * Como un servicio dado de baja se trata como no encontrado (RN-56), darlo de baja
     * dos veces lanza la misma excepción que un código inexistente.
     *
     * @param codigo código del servicio que se da de baja
     * @throws RegistroNoEncontradoException si no existe o ya está dado de baja
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void darDeBajaServicio(String codigo) throws HotelException {
        Servicio servicio = buscarServicio(codigo);
        servicio.darDeBaja();
    }

    /**
     * Cancela una reservación ACTIVA (RF-10).
     * <p>
     * La cancelación aplica a la reservación completa y no la elimina: pasa a CANCELADA y
     * se conserva en el hotel (RN-10, RN-37). Solo se puede cancelar mientras esté ACTIVA
     * (RN-08). Si faltan menos de {@code HORAS_MINIMAS_CANCELACION} horas para la entrada,
     * la reservación registra {@code CARGO_CANCELACION} una sola vez, sin generar factura
     * (RN-09). Hotel solo localiza la reservación; la validación del estado y el cargo los
     * resuelve {@code Reservacion.cancelar} (RN-39).
     *
     * @param idRes id de la reservación que se cancela
     * @param ahora momento en que se solicita la cancelación; con él se mide la anticipación
     * @throws RegistroNoEncontradoException si no existe una reservación con ese id
     * @throws ReservacionInvalidaException si la reservación no está ACTIVA
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void cancelarReservacion(String idRes, LocalDateTime ahora) throws HotelException {
        Reservacion reservacion = buscarReservacion(idRes);
        reservacion.cancelar(ahora);
    }

    /**
     * Busca un recepcionista activo por su id.
     * <p>
     * Es un método auxiliar interno: el id debe corresponder a un empleado activo (RN-56)
     * cuyo rol sea recepcionista, porque solo él realiza el check-in y el check-out (RN-44).
     *
     * @param id id del recepcionista que se busca
     * @return el recepcionista activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     * @throws HotelException si el empleado existe pero no es recepcionista
     */
    private Recepcionista buscarRecepcionista(String id) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        if (!(empleado instanceof Recepcionista recepcionista)) {
            throw new HotelException("El empleado " + id + " no es recepcionista.");
        }
        return recepcionista;
    }

    /**
     * Registra el check-in de una reservación (RF-11).
     * <p>
     * Lo realiza un recepcionista activo (RN-44). Solo aplica a una reservación ACTIVA cuyas
     * habitaciones estén todas DISPONIBLES: la reservación pasa a EN_CURSO y sus habitaciones
     * a OCUPADA (RN-13); si alguna no está DISPONIBLE se rechaza todo el check-in (RN-43). No
     * se valida que la fecha actual coincida con la de entrada (RN-43). Hotel solo localiza al
     * recepcionista y la reservación; el estado lo valida {@code Reservacion} (RN-39).
     *
     * @param idRecepcionista id del recepcionista que realiza el check-in
     * @param idRes id de la reservación a la que se hace check-in
     * @throws RegistroNoEncontradoException si el recepcionista no existe o está dado de baja,
     *                                       o no existe la reservación
     * @throws ReservacionInvalidaException si la reservación no está ACTIVA o alguna habitación
     *                                      no está DISPONIBLE
     * @throws HotelException si el empleado no es recepcionista
     */
    public void realizarCheckIn(String idRecepcionista, String idRes) throws HotelException {
        Recepcionista recepcionista = buscarRecepcionista(idRecepcionista);
        Reservacion reservacion = buscarReservacion(idRes);
        recepcionista.hacerCheckIn(reservacion);
    }

    /**
     * Registra el check-out de una reservación (RF-11).
     * <p>
     * Lo realiza un recepcionista activo (RN-44). Solo aplica a una reservación EN_CURSO cuya
     * factura ya fue generada y está pagada (RN-27): la reservación pasa a FINALIZADA y sus
     * habitaciones a LIMPIEZA (RN-13). Hotel solo localiza al recepcionista y la reservación;
     * el estado y el pago los valida {@code Reservacion} (RN-39).
     *
     * @param idRecepcionista id del recepcionista que realiza el check-out
     * @param idRes id de la reservación a la que se hace check-out
     * @throws RegistroNoEncontradoException si el recepcionista no existe o está dado de baja,
     *                                       o no existe la reservación
     * @throws ReservacionInvalidaException si la reservación no está EN_CURSO
     * @throws PagoPendienteException si la factura no se ha generado o no está pagada
     * @throws HotelException si el empleado no es recepcionista
     */
    public void realizarCheckOut(String idRecepcionista, String idRes) throws HotelException {
        Recepcionista recepcionista = buscarRecepcionista(idRecepcionista);
        Reservacion reservacion = buscarReservacion(idRes);
        recepcionista.hacerCheckOut(reservacion);
    }

    /**
     * Registra el consumo de un servicio en una reservación (RF-12).
     * <p>
     * El consumo solo se acepta en una reservación EN_CURSO, sobre un servicio activo del
     * catálogo y antes de que se genere la factura (RN-22, RN-46); esas validaciones las
     * hace {@code Reservacion}. Hotel solo localiza la reservación y el servicio (RN-39). No
     * se guarda ningún monto: el costo se calcula con {@code calcularCosto(cantidad)} del
     * servicio al generar la factura (RN-19 a RN-21, RN-24).
     *
     * @param idRes          id de la reservación que consume
     * @param codigoServicio código del servicio consumido
     * @param cantidad       cantidad consumida (platillos, sesiones o prendas); debe ser positiva
     * @throws RegistroNoEncontradoException si no existe la reservación o el servicio, o este
     *                                       está dado de baja
     * @throws ReservacionInvalidaException  si la reservación no está EN_CURSO o ya tiene factura
     * @throws IllegalArgumentException      si la cantidad no es positiva
     * @throws HotelException excepción base de las reglas del hotel
     */
    public void registrarConsumo(String idRes, String codigoServicio, int cantidad) throws HotelException {
        Reservacion reserva = buscarReservacion(idRes);
        Servicio servicio = buscarServicio(codigoServicio);
        reserva.agregarConsumo(servicio, cantidad);
    }

    /**
     * Genera la factura de una reservación EN_CURSO (RF-13).
     * <p>
     * La factura se genera una sola vez (RN-46). Incluye un detalle por cada habitación
     * (por las noches de la estancia) y uno por cada consumo registrado (RN-24). El
     * descuento depende del tipo de huésped (RN-26) y el folio es único porque se
     * deriva del id de la reservación (RN-52).
     *
     * @param idRes id de la reservación que se factura
     * @return la factura generada y asociada a la reservación
     * @throws RegistroNoEncontradoException si no existe la reservación
     * @throws ReservacionInvalidaException si no está EN_CURSO o ya tiene factura
     * @throws HotelException excepción base de las reglas del hotel
     */
    public Factura generarFactura(String idRes) throws HotelException {
        Reservacion reservacion = buscarReservacion(idRes);
        Factura factura = new Factura("F-" + reservacion.getId(), LocalDate.now(),
                reservacion.getHuesped().calcularDescuento());

        int noches = reservacion.calcularNoches();
        for (Habitacion habitacion : reservacion.getHabitaciones()) {
            factura.agregarDetalle(habitacion, noches);
        }
        for (ConsumoServicio consumo : reservacion.getConsumos()) {
            factura.agregarDetalle(consumo.getServicio(), consumo.getCantidad());
        }

        reservacion.setFactura(factura);
        return factura;
    }

    /**
     * Registra el pago de la factura de una reservación (RF-13).
     * <p>
     * La factura debe existir y no estar pagada (RN-46); esto último lo valida
     * {@code Factura}.
     *
     * @param idRes id de la reservación cuya factura se paga
     * @throws RegistroNoEncontradoException si no existe la reservación
     * @throws PagoPendienteException si la reservación aún no tiene factura
     * @throws HotelException si la factura ya estaba pagada
     */
    public void pagarFactura(String idRes) throws HotelException {
        Reservacion reservacion = buscarReservacion(idRes);
        Factura factura = reservacion.getFactura();
        if (factura == null) {
            throw new PagoPendienteException("La reservación " + idRes + " no tiene factura generada.");
        }
        factura.pagar();
    }

    /**
     * Registra la limpieza de una habitación (RF-14).
     * <p>
     * Lo realiza una camarista activa (RN-48). La habitación debe estar en LIMPIEZA y pasa
     * a DISPONIBLE. Hotel solo localiza a la camarista y la habitación; el estado lo valida
     * {@code Camarista}.
     *
     * @param idCamarista   id de la camarista que realiza la limpieza
     * @param numHabitacion número de la habitación limpiada
     * @throws RegistroNoEncontradoException si la camarista o la habitación no existen o están
     *                                       dadas de baja
     * @throws HotelException si el empleado no es camarista o la habitación no está en LIMPIEZA
     */
    public void registrarLimpieza(String idCamarista, int numHabitacion) throws HotelException {
        Camarista camarista = buscarCamarista(idCamarista);
        Habitacion habitacion = buscarHabitacion(numHabitacion);
        camarista.registrarLimpieza(habitacion);
    }

    /**
     * Registra una propina para un mesero (RF-15).
     * <p>
     * La propina se suma a las acumuladas del periodo (RN-49). El monto debe ser positivo;
     * lo valida {@code Mesero}.
     *
     * @param idMesero id del mesero que recibe la propina
     * @param monto    monto de la propina; debe ser positivo
     * @throws RegistroNoEncontradoException si el mesero no existe o está dado de baja
     * @throws HotelException si el empleado no es mesero o el monto no es positivo
     */
    public void registrarPropina(String idMesero, double monto) throws HotelException {
        Mesero mesero = buscarMesero(idMesero);
        mesero.registrarPropina(monto);
    }

    /**
     * Busca una camarista activa por su id (RN-48, RN-56).
     *
     * @param id id de la camarista que se busca
     * @return la camarista activa con ese id
     * @throws RegistroNoEncontradoException si no existe o está dada de baja
     * @throws HotelException si el empleado existe pero no es camarista
     */
    private Camarista buscarCamarista(String id) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        if (!(empleado instanceof Camarista camarista)) {
            throw new HotelException("El empleado " + id + " no es camarista.");
        }
        return camarista;
    }

    /**
     * Busca un mesero activo por su id (RN-49, RN-56).
     *
     * @param id id del mesero que se busca
     * @return el mesero activo con ese id
     * @throws RegistroNoEncontradoException si no existe o está dado de baja
     * @throws HotelException si el empleado existe pero no es mesero
     */
    private Mesero buscarMesero(String id) throws HotelException {
        Empleado empleado = buscarEmpleado(id);
        if (!(empleado instanceof Mesero mesero)) {
            throw new HotelException("El empleado " + id + " no es mesero.");
        }
        return mesero;
    }

    /**
     * Calcula la nómina total del periodo actual (RF-16).
     * <p>
     * Suma el sueldo de todos los empleados activos; cada rol calcula el suyo con
     * polimorfismo (salario base, más bono el gerente, más propinas el mesero). Los
     * empleados dados de baja no cuentan (RN-35). Solo consulta: no modifica a ningún
     * empleado (RN-50).
     *
     * @return total de la nómina en pesos; 0 si no hay empleados activos
     */
    public double calcularNomina() {
        double total = 0;
        for (Empleado empleado : listarEmpleados()) {
            total += empleado.calcularSueldo();
        }
        return total;
    }

    /**
     * Cierra el periodo de nómina (RF-16).
     * <p>
     * Calcula primero la nómina del periodo y después reinicia los acumulados de cada
     * empleado activo, por ejemplo las propinas de los meseros (RN-50). El orden importa:
     * si se reiniciara antes de calcular, las propinas no se pagarían.
     *
     * @return total de la nómina pagada en el periodo que se cierra
     */
    public double cerrarPeriodoNomina() {
        double total = calcularNomina();
        for (Empleado empleado : listarEmpleados()) {
            empleado.cerrarPeriodo();
        }
        return total;
    }
}