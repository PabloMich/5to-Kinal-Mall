package org.pablomich.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.pablomich.bean.Administracion;
import org.pablomich.bean.Clientes;
import org.pablomich.bean.CuentasPorCobrar;
import org.pablomich.bean.CuentasPorPagar;
import org.pablomich.bean.Locales;
import org.pablomich.bean.Proveedores;
import org.pablomich.db.Conexion;
import org.pablomich.system.Principal;
/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 9/07/2021
 * @time 08:42:03 PM
 */
public class CuentasPorPagarController implements Initializable{

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private ImageView imgModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblCuentasPorPagar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNumeroFactura;
    @FXML
    private TableColumn colFechaLimitePago;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colCodigoAdministracion;
    @FXML
    private TableColumn colCodigoProveedor;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFactura;
    @FXML
    private TextField txtFechaPago;
    @FXML
    private TextField txtEstadoPago;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbProveedor;
    
    private Principal escenarioPrincipal;
    
    private enum Operaciones {
        NUEVO, GUARDAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    private Operaciones operacion = Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<Administracion> listaAdministracion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //cargarDatos();
    }
    
    public void activarControles() {
        txtId.setDisable(true);
        txtId.setEditable(false);
        
        txtFactura.setDisable(true);
        txtFactura.setEditable(false);
        
        txtEstadoPago.setDisable(true);
        txtEstadoPago.setEditable(false);
        
        
    }
    
    @FXML
    private void nuevo(ActionEvent event) {
    }

    @FXML
    private void modificar(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
    }

    @FXML
    private void reporte(ActionEvent event) {
    }

       @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

}
