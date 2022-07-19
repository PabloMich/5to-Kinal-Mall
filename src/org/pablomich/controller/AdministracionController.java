package org.pablomich.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.pablomich.system.Principal;
import org.pablomich.db.Conexion;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.pablomich.bean.Administracion;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.pablomich.report.GenerarReporte;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 13/05/2021
 * @time 10:29:08
 */


public class AdministracionController implements Initializable {

    private ObservableList<Administracion> listaAdministracion;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    
    private enum Operaciones{
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    
    }
    
    private Operaciones operacion = Operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    @FXML
    private ImageView regresar;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableView tblAdministracion;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    void regresar(MouseEvent event) {
       escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    public ObservableList<Administracion>  getAdministracion(){
        
        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        
            try {
                PreparedStatement stmt;
                
                //CallableStatement 
                stmt = (PreparedStatement) Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion}");
                ResultSet resultado = stmt.executeQuery();
                
                while(resultado.next()){
                    System.out.println(resultado.getInt(1));
                    System.out.println(resultado.getString(2));
                    System.out.println(resultado.getString(3));
                    System.out.println("");
                    listado.add(new Administracion(
                            resultado.getInt("id"),
                            resultado.getString("direccion"),
                            resultado.getString("telefono")
                    
                            )      
                    
                        );
                }
                
                listado.forEach(elemento ->{
                    System.out.println();
                });
                
                resultado.close();
                stmt.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
    
            listaAdministracion = FXCollections.observableArrayList(listado);
            return  listaAdministracion;
    }
    
    
    public void cargarDatos(){
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String>("telefono"));
        
    }
    
        public boolean existeElementoSeleccionado(){
        if(tblAdministracion.getSelectionModel().getSelectedItem() == null){
            return false;
        }else{
            return true;
        
        }    
    }
    
    @FXML
    public void seleccionarElemento(){
        txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
        txtDireccion.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTelefono.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono()));
    }

    
    
    private void agregarAdministracion(){
        ArrayList<TextField> listaCampos = new ArrayList<>();
        //listaCampos.add(txtId);
        listaCampos.add(txtDireccion);
        listaCampos.add(txtTelefono);
         
        
        if(validar(listaCampos)){
            Administracion registro = new Administracion();
            registro.setDireccion(txtDireccion.getText());
            registro.setTelefono(txtTelefono.getText());
        
        
        
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarAdministracion(?,?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            stmt.execute();
            cargarDatos();
            limpiarCampos();
            habilitarCampos();
            //deshabilitarCampos();
                        
        }catch(SQLException e){
            e.printStackTrace();
         }
        
        }else{
                //JOptionPane.showMessageDialog(null, "Alguno de los campos se encuetra vacio");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Kinal Mall");
                alert.setHeaderText(null);
                alert.setContentText("Los campos se encuentran vacios");
                alert.show();
            
        
            }   
    
        
        
    }
        
    private void habilitarCampos(){
        txtId.setEditable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        limpiarCampos();
        cargarDatos();
    }
      
    
   
    private void editarAdministracion(){
    
        Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarAdministracion(?, ?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDireccion());
            stmt.setString(3, registro.getTelefono());
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    
    }
    
    //Deja los campos vacios
    public void limpiarCampos(){
        txtId.clear();
        txtDireccion.clear();
        txtTelefono.clear();
    
    }
    
    
    
    
  

 
        
    //Desahbilita los campos de texto en donde agregamos los datos
    public void deshabilitarCampos(){
        txtId.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    
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
   
    
    
    
    private void eliminarAdministracion(){
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarAdministracion(?)}");
            stmt.setInt(1,Integer.parseInt(txtId.getText()));                       
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e){
            e.printStackTrace();
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
                           
                        editarAdministracion();               
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
                
            
            case GUARDAR: // Cancela incersion
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
               
                
                //verificarDatosLlenos();
                break;
                
                
                
                
                
            case GUARDAR:
                agregarAdministracion();                
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
    private void eliminar(ActionEvent event) {
        System.out.println("Operacion" + operacion);
        
        switch(operacion){
                case ACTUALIZAR: //Cancelar
                    btnNuevo.setDisable(false);                 
                    btnReporte.setDisable(false);
                    btnModificar.setText("Modificar");
                    btnEliminar.setText("Eliminar");
                    limpiarCampos();    
                    operacion = Operaciones.NINGUNO;
                    break;
                case NINGUNO: //ELIMINAR
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
                           
                        eliminarAdministracion();                                         
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
    private void reporte(ActionEvent event) {
        
        Map parametros = new HashMap();
        
        GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", parametros, "Reporte de Administración");
    }
    
}












