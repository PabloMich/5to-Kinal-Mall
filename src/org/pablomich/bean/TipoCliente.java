package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 9/06/2021
 * @time 10:32:21
 */
public class TipoCliente {
    private int id;
    private String descripcion;

    public TipoCliente() {
    }

    public TipoCliente(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoCliente{" + "id=" + id + ", descripcion=" + descripcion + '}';
    }
    
}
