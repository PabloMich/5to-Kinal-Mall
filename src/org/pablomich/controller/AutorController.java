package org.pablomich.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 12/05/2021
 * @time 09:35:46 AM
 */
public class AutorController implements Initializable {
    
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    void atras(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
}

