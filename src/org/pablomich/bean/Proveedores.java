package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 9/07/2021
 * @time 09:04:32 PM
 */
public class Proveedores {
    private int id;
    private String nit;
    private String servicioPrestado;
    private String telefono;
    private String direccion;
    private String saldoFavor;
    private String saldoContra;

    public Proveedores() {
    }

    public Proveedores(int id, String nit, String servicioPrestado, String telefono, String direccion, String saldoFavor, String saldoContra) {
        this.id = id;
        this.nit = nit;
        this.servicioPrestado = servicioPrestado;
        this.telefono = telefono;
        this.direccion = direccion;
        this.saldoFavor = saldoFavor;
        this.saldoContra = saldoContra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getServicioPrestado() {
        return servicioPrestado;
    }

    public void setServicioPrestado(String servicioPrestado) {
        this.servicioPrestado = servicioPrestado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(String saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public String getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(String saldoContra) {
        this.saldoContra = saldoContra;
    }

    @Override
    public String toString() {
        return "Proveedores{" + "id=" + id + ", nit=" + nit + ", servicioPrestado=" + servicioPrestado + ", telefono=" + telefono + ", direccion=" + direccion + ", saldoFavor=" + saldoFavor + ", saldoContra=" + saldoContra + '}';
    }
    
    
}
