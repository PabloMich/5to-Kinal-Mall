package org.pablomich.system;
import org.pablomich.controller.AutorController;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.pablomich.controller.AdministracionController;
import org.pablomich.controller.CargosController;
import org.pablomich.controller.ClientesController;
import org.pablomich.controller.CuentasPorPagarController;
import org.pablomich.controller.HorariosController;
import org.pablomich.controller.MenuPrincipalController;
import org.pablomich.controller.LocalesController;
import org.pablomich.controller.DepartamentosController;
import org.pablomich.controller.ProveedoresController;
import org.pablomich.controller.LoginController;
import java.util.Base64;
import org.pablomich.bean.Usuario;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 5/05/2021
 * @time 11:27:49 AM
 */
    
public class Principal extends Application {

    private final String PAQUETE_VIEW = "/org/pablomich/view/";
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private int rol;
    //private Usuario usuario;
    
    public static void main(String[] args) {
        launch(args);
    
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    
    @Override
    public void start(Stage stage) throws Exception {
        //usuario = new Usuario();
        this.escenarioPrincipal = stage;
        mostrarMenuPrincipal();
                
        /*String password = "admin";
        String password64 = Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println("Codificado: " + password64);
        
        mostrarEscenaLogin();*/
    }
     
    public void mostrarMenuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = null;
            menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 890, 500);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Se produjo un error al cargar la vista del menú principal");
            ex.printStackTrace();
        }
    }
    
    public void mostrarEscenaAutor() {
        try {
            AutorController autorController = (AutorController) cambiarEscena("AutorView.fxml", 890, 500);
            autorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al cargar la vista del autor");
            e.printStackTrace();
        }

    }
    
    public void mostrarEscenaAdministracion() {
        try {
            AdministracionController adminController;
            adminController = (AdministracionController) cambiarEscena("AdministracionView.fxml", 890, 500);
            adminController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al cargar la vista de Administración");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaClientes() {
        try {
            ClientesController clientesController;
            clientesController = (ClientesController) cambiarEscena("ClientesView.fxml", 1280, 500);
            clientesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Clientes");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaCuentasPorCobrar() {
         try {
            //CuentasPorCobrarController cpCobrarController;
            //cpCobrarController = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml", 1152, 648);
            //cpCobrarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Cuentas por Cobrar");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaCuentasPorPagar() {
        try {
            CuentasPorPagarController cuentasPorPagarController;
            cuentasPorPagarController = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 1000, 500);
            cuentasPorPagarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Cuentas por Pagar");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaHorarios() {
        try {
            HorariosController horariosController;
            horariosController = (HorariosController) cambiarEscena("HorariosView.fxml", 890, 500);
            horariosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Horarios");
            e.printStackTrace();
        }
    }
     
    public void mostrarEscenaLocales() {
        try {
            LocalesController localesController;
            localesController = (LocalesController) cambiarEscena("LocalesView.fxml", 1000, 500);
            localesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Locales");
            e.printStackTrace();
        }
    }
     
    public void mostrarEscenaCargos() {
        try {
            CargosController cargosController;
            cargosController = (CargosController) cambiarEscena("CargosView.fxml", 890, 500);
            cargosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Locales");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaDepartamentos() {
        try {
            DepartamentosController departamentosController;
            departamentosController = (DepartamentosController) cambiarEscena("DepartamentosView.fxml", 890, 500);
            departamentosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Departamentos");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaProveedores() {
        try {
            ProveedoresController proveedoresController;
            proveedoresController = (ProveedoresController) cambiarEscena("ProveedoresView.fxml", 1000, 500);
            proveedoresController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al cargar la vista de Proveedores");
            e.printStackTrace();
        }
    }
    
    public void mostrarEscenaLogin() {
        try {
            LoginController loginController;
            loginController = (LoginController) cambiarEscena("LoginView.fxml", 600, 450);
            loginController.setEscenarioPrincipal(this);
        }catch (Exception e) {
            System.out.println("\nSe produjo un error al cargar el Login");
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String viewFxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;    
        
        FXMLLoader cargadorFXML = new FXMLLoader();   
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + viewFxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + viewFxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
                
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        
        escenarioPrincipal.show();
        
        resultado = (Initializable) cargadorFXML.getController(); 
        return resultado;
    }
}
