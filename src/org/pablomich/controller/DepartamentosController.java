package org.pablomich.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.pablomich.bean.Cargos;
import org.pablomich.bean.Departamentos;
import org.pablomich.db.Conexion;
import org.pablomich.report.GenerarReporte;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 7/07/2021
 * @time 09:45:35 AM
 */
public class DepartamentosController implements Initializable{

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
    private TextField txtNombreDepartamento;
    @FXML
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombreDepartamento;
    Principal escenarioPrincipal;


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
    private enum Operaciones {
        UEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    private DepartamentosController.Operaciones operacion = DepartamentosController.Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    
    private ObservableList<Departamentos> listaDepartamentos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public ObservableList<Departamentos> getDepartamentos() {
        System.out.println("Consultando DB.Departamentos...");
        ArrayList<Departamentos> lista = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos()}");
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Departamentos departamentos = new Departamentos(
                        rs.getInt("id"),
                        rs.getString("nombre"));
                lista.add(departamentos);
            }
            
            System.out.println("Mostrando Resultados...");
            for (Departamentos departamentos : lista) {
                System.out.println(departamentos);
            }
            
            listaDepartamentos = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.out.println("\nSe produjo un error al intentar consultar la lista de Departamentos");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                
            }
        }
        return listaDepartamentos;
        
    }
    
    public void cargarDatos(){
        tblDepartamentos.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("id"));
        colNombreDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));       
    }
        public boolean existeElementoSeleccionado(){
        if(tblDepartamentos.getSelectionModel().getSelectedItem() == null){
            return false;
        }else{
            return true;
        
        }    
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
    
    public void limpiarControles(){
        txtId.clear();
        txtNombreDepartamento.clear();
    }
    
    public void activarControles() {
        txtId.setEditable(false);
        txtNombreDepartamento.setEditable(false);
    }
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtNombreDepartamento.setEditable(true);
    }
    
    public void agregarDepartamentos() {
        ArrayList<TextField> listaCampos = new ArrayList<>();
        listaCampos.add(txtNombreDepartamento);
        
        if(validar(listaCampos)){
            Departamentos departamentos = new Departamentos();
            departamentos.setNombre(txtNombreDepartamento.getText());
            
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarDepartamentos(?)}");
            stmt.setString(1, departamentos.getNombre());
            stmt.execute();
            cargarDatos();
            limpiarControles();
            activarControles();
        }catch (SQLException e) {
            e.printStackTrace();
        }   
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kinal Mall");
            alert.setHeaderText(null);
            alert.setContentText("Los campos se encuentran vacios");
            alert.show();
        }
    }
    
    private void editarDepartamentos() {
        Departamentos departamentos = (Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem();
        departamentos.setId(Integer.parseInt(txtId.getText()));
        departamentos.setNombre(txtNombreDepartamento.getText());
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDepartamentos(?, ?)}");
            stmt.setInt(1, departamentos.getId());
            stmt.setString(2, departamentos.getNombre());
            stmt.execute();
            System.out.println(stmt.toString());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarCargos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDepartamentos(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean existeElementosSeleccionados() {
        return tblDepartamentos.getSelectionModel().getSelectedItem() !=null;
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtNombreDepartamento.setText(String.valueOf(((Departamentos) tblDepartamentos.getSelectionModel().getSelectedItem()).getNombre()));
        }
    }
        
    @FXML
    private void nuevo(ActionEvent event) {
        System.out.println("Operacion" + operacion);
    
        switch(operacion){
            case NINGUNO:
                activarControles();          
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);               
                break;
  
            case GUARDAR:
                agregarDepartamentos();                
                desactivarControles();  
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
        switch (operacion) {
            case NINGUNO:

                if (existeElementosSeleccionados()) {
                    activarControles();
                    
                    btnModificar.setText("Actualizar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                    
                    
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                    
                    btnNuevo.setDisable(true);
                    imgNuevo.setOpacity(0.15);
                    
                    btnReporte.setDisable(true);
                    imgReporte.setOpacity(0.15);
                    
                    
                    operacion = DepartamentosController.Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar, selecciona un registro");
                    alert.show();
                }
                break;
            case ACTUALIZAR:
                
                    limpiarControles();
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                    
                    btnEliminar.setText("Eliminar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                    
                    btnNuevo.setDisable(false);
                    imgNuevo.setOpacity(1);
                    
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);

                    operacion = DepartamentosController.Operaciones.NINGUNO;
                    /*
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llena todos los campos");
                    alert.show();
                }
*/
                break;

            case GUARDAR: // CancelaciÃ³n de una inserciÃ³n
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                
                operacion = DepartamentosController.Operaciones.NINGUNO;
                break;
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
                    limpiarControles();    
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
                           
                        eliminarCargos();                                         
                        limpiarControles();    
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
        
        GenerarReporte.getInstance().mostrarReporte("ReporteDepartamentos.jasper", parametros, "Reporte de Departamentos");
    }

}
