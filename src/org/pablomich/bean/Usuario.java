package org.pablomich.bean;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 11/08/2021
 * @time 07:24:20 PM
 */
public class Usuario {
    private String user;
    private String pass;
    private String nombre;
    private int rol;

    public Usuario() {
    }

    public Usuario(String user, String pass, String nombre, int rol) {
        this.user = user;
        this.pass = pass;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    
}
