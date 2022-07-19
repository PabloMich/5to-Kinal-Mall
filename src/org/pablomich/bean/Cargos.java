package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 15/07/2021
 * @time 07:44:14 PM
 */
public class Cargos {
    private int id;
    private String nombre;

    public Cargos() {
    }

    public Cargos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "Cargos{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
    
}
