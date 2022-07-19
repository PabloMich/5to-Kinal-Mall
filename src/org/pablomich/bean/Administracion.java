package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 27/05/2021
 * @time 11:40:58
 */
public class Administracion {

    //Atributos
    private int id;
    private String direccion;
    private String telefono;
    
    //Constructor nulo
    public Administracion() {
        
    }
    
    //Constructor con parametros
    public Administracion(int id, String direccion, String telefono) {
        this.id = id;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    //Metodos Getter and Setters
    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "\nAdministracion{" + "id=" + id + ", direccion=" + direccion + ", telefono=" + telefono + '}';
    }
    
    
}
