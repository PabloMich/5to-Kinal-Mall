package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 7/07/2021
 * @time 09:32:09 PM
 */
public class Departamentos {
    private int id;
    private String nombre;

    public Departamentos() {
    }

    public Departamentos(int id, String nombre) {
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
        return "Departamentos{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
    
}
