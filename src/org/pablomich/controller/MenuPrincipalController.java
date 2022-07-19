package org.pablomich.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 5/05/2021
 * @time 11:43:32 AM
 */
public class MenuPrincipalController implements Initializable {
    
    private Principal escenarioPrincipal;
    @FXML
    private MenuItem menuAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*if(escenarioPrincipal.getRol() != 1) {
            menuAdministracion.setDisable(true);
        }*/
    }
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    void mostrarVistaAutor(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaAutor();
    }
    
    @FXML
    private void mostrarVistaCuentasPorPagar(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaCuentasPorPagar();
    }

    @FXML
    private void mostrarVistaAdministracion(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaAdministracion();
    }

    @FXML
    private void mostrarVistaClientes(ActionEvent event) {
        this.escenarioPrincipal.mostrarEscenaClientes();
    }

    @FXML
    private void mostrarVistaHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaHorarios();
    }

    @FXML
    private void mostrarVistaLocales(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaLocales();
    }

    @FXML
    private void mostrarVistaCargos(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaCargos();
    }

    @FXML
    private void mostrarVistaDepartamentos(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaDepartamentos();
    }

    @FXML
    private void mostrarVistaProveedores(ActionEvent event) {
        escenarioPrincipal.mostrarEscenaProveedores();
    }

    @FXML
    private void mostrarVistaLogin(MouseEvent event) {
        escenarioPrincipal.mostrarEscenaLogin();
    }
    
}
