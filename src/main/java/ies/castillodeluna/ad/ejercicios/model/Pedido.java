package ies.castillodeluna.ad.ejercicios.model;

import java.util.Date;
import ies.castillodeluna.ad.ejercicios.Entity;

public class Pedido implements Entity {

    private int id;
    private Date fecha;
    private double importe;
    private int idCliente; // FK

    public Pedido() {
    }

    public Pedido(int id, Date fecha, double importe, int idCliente) {
        this.id = id;
        this.fecha = fecha;
        this.importe = importe;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

}
