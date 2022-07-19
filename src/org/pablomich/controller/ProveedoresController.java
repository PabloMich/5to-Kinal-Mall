package org.pablomich.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.pablomich.bean.Proveedores;
import org.pablomich.db.Conexion;
import org.pablomich.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.pablomich.report.GenerarReporte;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 9/07/2021
 * @time 11:59:50 AM
 */
public class ProveedoresController implements Initializable{

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
    private TextField txtId;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtServicioPrestado;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TableView tblProveedores;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colServicioPrestado;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    
    Principal escenarioPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Iniciando...");
        cargarDatos();
    }
    
    private enum Operaciones {
        UEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    private ObservableList<Proveedores> listaProveedores;
    
    private Operaciones operacion = Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    
    private ObservableList<Proveedores> listaHorarios;
    
    
    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("saldoFavor"),
                        rs.getString("saldoContra")
                    )
                );
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;    
    }
    
    public void agregarProveedores() {
        ArrayList<TextField> proveedores = new ArrayList<>();
        proveedores.add(txtNit);
        proveedores.add(txtServicioPrestado);
        proveedores.add(txtTelefono);
        proveedores.add(txtDireccion);
        proveedores.add(txtSaldoFavor);
        proveedores.add(txtSaldoContra);
        
        if(validar(proveedores)) {
            Proveedores registro = new Proveedores();
            registro.setNit(txtNit.getText());
            registro.setSaldoContra(txtSaldoContra.getText());
            registro.setTelefono(txtTelefono.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setSaldoFavor(txtSaldoFavor.getText());
            registro.setSaldoContra(txtSaldoContra.getText());
            
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarProveedores(?,?,?,?,?,?)}");
            stmt.setString(1, registro.getNit());
            stmt.setString(2, registro.getServicioPrestado());
            stmt.setString(3, registro.getTelefono());
            stmt.setString(4, registro.getDireccion());
            stmt.setString(5, registro.getSaldoFavor());
            stmt.setString(6, registro.getSaldoContra());
            stmt.execute();
            cargarDatos();
            limpiarCampos();
            habilitarCampos();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        } else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Kinal Mall");
                alert.setHeaderText(null);
                alert.setContentText("Los campos se encuentran vacios");
                alert.show();
            } 
    }
    
    public boolean existeElementoSeleccionado(){
        if(tblProveedores.getSelectionModel().getSelectedItem() == null){
            return false;
        }else{
            return true;
        }
    }
    
    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));        
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("saldoContra"));   
    }
    
    public void limpiarCampos(){
        txtId.clear();
        txtNit.clear();
        txtServicioPrestado.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();    
    }
    private void habilitarCampos(){
        txtId.setEditable(false);
        txtNit.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        limpiarCampos();
        cargarDatos();
    }
    
    public void deshabilitarCampos(){
        txtId.setEditable(false);
        txtNit.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
    }
    
    @FXML
    public void seleccionarElemento(){
        txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
        txtNit.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit()));
        txtServicioPrestado.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado()));
        txtDireccion.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTelefono.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono()));
        txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
        txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
    }
    
    public boolean validar(ArrayList<TextField> listadoCampos){
        boolean respuesta = true;
        
        for(TextField campoTexto: listadoCampos){
            if(campoTexto.getText().trim().isEmpty()){
                return false;
            
            }
        }
        return respuesta;
    }
    
    private void editarProveedores(){
    
        Proveedores registro = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNit(txtNit.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setSaldoFavor(txtSaldoFavor.getText());
        registro.setSaldoContra(txtSaldoContra.getText());
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNit());
            stmt.setString(3, registro.getServicioPrestado());
            stmt.setString(4, registro.getDireccion());
            stmt.setString(5, registro.getTelefono());
            stmt.setString(6, registro.getSaldoFavor());
            stmt.setString(7, registro.getSaldoContra());
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    
    }
    
    private void eliminarProveedores(){
        Proveedores  proveedores = ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem());    
        System.out.println(proveedores);
        try{   
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarProveedores(?)}");
            stmt.setInt(1,proveedores.getId());                       
            stmt.execute();
            System.out.println(stmt.toString());
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un Proveedor");
            
            
            }catch(SQLException e){
            
            if(e.getErrorCode() == 1452){
               JOptionPane.showMessageDialog(null, "Debe Seleccionar un Proveedor");
            
            }
        
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un Proveedor");
        
        }
   
    }
    
    
    
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
    
        switch(operacion){
            case NINGUNO:
                habilitarCampos();
                           
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);               
                break;

            case GUARDAR:
                agregarProveedores();                
                deshabilitarCampos();  
                operacion = Operaciones.NINGUNO;
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                System.out.println("Operacion" + operacion);
                btnReporte.setDisable(false);
                txtId.setText(""); 
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        
        switch(operacion)
        {
            case NINGUNO:
                habilitarCampos();
                btnModificar.setText("Actualizar");
                btnEliminar.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.ACTUALIZAR;
                break;
                
            case ACTUALIZAR:
                btnModificar.setText("Modificar");
                btnEliminar.setText("Eliminar");
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;                
                
                 if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro que desea modificarlo con la actualización?");
                    
                    ButtonType btnConfirmacionSi = new ButtonType("Si");
                    ButtonType btnConfirmacionNo = new ButtonType("N0");
                    alert.getButtonTypes().clear();
                
                    alert.getButtonTypes().addAll(btnConfirmacionSi,btnConfirmacionNo);
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                        if (respuesta.get() == btnConfirmacionSi) {
                           
                        editarProveedores();               
                        limpiarCampos();
                        cargarDatos();   
                        break;                     
                    }
                                        
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }
                break;
                
            case GUARDAR:
                deshabilitarCampos(); 
                limpiarCampos();
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                break;
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        
        switch(operacion){
                case ACTUALIZAR:
                    btnNuevo.setDisable(false);                 
                    btnReporte.setDisable(false);
                    btnModificar.setText("Modificar");
                    btnEliminar.setText("Eliminar");
                    limpiarCampos();    
                    operacion = Operaciones.NINGUNO;
                    break;
                case NINGUNO:
                        if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro que desea continuar con la actualización?");
                    
                    ButtonType btnConfirmacionSi = new ButtonType("Si");
                    ButtonType btnConfirmacionNo = new ButtonType("N0");
                    alert.getButtonTypes().clear();
                
                    alert.getButtonTypes().addAll(btnConfirmacionSi,btnConfirmacionNo);
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                        if (respuesta.get() == btnConfirmacionSi) {
                           
                        eliminarProveedores();                                         
                        limpiarCampos();    
                        cargarDatos();    
                        break;                     
                    }
                                        
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar seleccione un registro");
                    alert.show();
                }
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
    return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    private void reporte(ActionEvent event) {
        Map parametros = new HashMap();
        
        GenerarReporte.getInstance().mostrarReporte("ReporteProveedores.jasper", parametros, "Reporte de Proveedores");
    }
}
