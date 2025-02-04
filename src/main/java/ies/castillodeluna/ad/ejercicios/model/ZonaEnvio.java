package ies.castillodeluna.ad.ejercicios.model;

import ies.castillodeluna.ad.ejercicios.Entity;

public class ZonaEnvio implements Entity {

    private int id;
    private String nombre;
    private double tarifa;

    public ZonaEnvio() {
    }

    public ZonaEnvio(int id, String nombre, double tarifa) {
        this.id = id;
        this.nombre = nombre;
        this.tarifa = tarifa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    
    
}
