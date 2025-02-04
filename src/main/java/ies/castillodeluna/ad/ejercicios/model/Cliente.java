package ies.castillodeluna.ad.ejercicios.model;

import ies.castillodeluna.ad.ejercicios.Entity;

/**
 * Clase que representa a un Cliente en el sistema.
 * Implementa la interfaz Entity.
 */
public class Cliente implements Entity {
    
    private int id;               // Identificador único del cliente
    private String nombre;        // Nombre completo del cliente
    private String email;        // Correo electrónico del cliente
    private String telefono;      // Número de teléfono del cliente (ahora es String)
    private int id_zona;      // ID de la zona de envío (relación FK)

    /**
     * Constructor vacío de Cliente.
     * Se utiliza para crear un objeto vacío antes de rellenarlo con los datos necesarios.
     */
    public Cliente() {
    }

    /**
     * Constructor de Cliente con todos los datos.
     * @param id Identificador único del cliente
     * @param nombre Nombre completo del cliente
     * @param correo Correo electrónico del cliente
     * @param telefono Número de teléfono del cliente
     * @param idZonaEnvio ID de la zona de envío a la que pertenece el cliente
     */
    public Cliente(int id, String nombre, String email, String telefono, int id_zona) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.id_zona = id_zona;
    }

    /**
     * Obtiene el identificador único del cliente.
     * @return ID del cliente
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del cliente.
     * @param id ID del cliente
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre completo del cliente.
     * @return Nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo del cliente.
     * @param nombre Nombre del cliente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     * @return Correo electrónico del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     * @param correo Correo electrónico del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * @return Número de teléfono del cliente
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * @param telefono Número de teléfono del cliente
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el ID de la zona de envío del cliente.
     * @return ID de la zona de envío
     */
    public int getIdZonaEnvio() {
        return id_zona;
    }

    /**
     * Establece el ID de la zona de envío del cliente.
     * @param idZonaEnvio ID de la zona de envío
     */
    public void setIdZonaEnvio(int id_zona) {
        this.id_zona = id_zona;
    }

    /**
     * Representa a un cliente como una cadena de texto.
     * @return Información detallada del cliente
     */
    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nombre=" + nombre + ", Correo=" + email + ", Teléfono=" + telefono + ", Zona de Envío ID=" + id_zona + "]";
    }
}
