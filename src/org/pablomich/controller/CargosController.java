package org.pablomich.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
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
import org.pablomich.db.Conexion;
import org.pablomich.report.GenerarReporte;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 15/07/2021
 * @time 07:44:02 PM
 */
public class CargosController implements Initializable {
    
    Principal escenarioPrincipal;
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
    private TableView tblCargos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombreCargo;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombreCargo;


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
    
    private CargosController.Operaciones operacion = CargosController.Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    
    private ObservableList<Cargos> listaCargos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public ObservableList<Cargos> getCargos() {
        System.out.println("Consultando DB.Cargos...");
        ArrayList<Cargos> lista = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}");
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Cargos cargos = new Cargos(
                        rs.getInt("id"),
                        rs.getString("nombre"));
                lista.add(cargos);
            }
            
            System.out.println("Mostrando Resultados...");
            for (Cargos cargos : lista) {
                System.out.println(cargos);
            }
            
            listaCargos = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.out.println("\nSe produjo un error al intentar consultar la lista de Cargos");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                
            }
        }
        return listaCargos;
        
    }
    
    public void agregarCargos() {                               
        ArrayList<TextField> listaCampos = new ArrayList<>();
        listaCampos.add(txtNombreCargo);
        
        if(validar(listaCampos)){
            Cargos cargos = new Cargos();
            cargos.setNombre(txtNombreCargo.getText());
            
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCargos(?)}");
            stmt.setString(1, cargos.getNombre());
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
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtNombreCargo.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombre()));
        }
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("id"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombre"));       
    }
        public boolean existeElementoSeleccionado(){
        if(tblCargos.getSelectionModel().getSelectedItem() == null){
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
        txtNombreCargo.clear();
    }
    
    public void activarControles() {
        txtId.setEditable(false);
        txtNombreCargo.setEditable(false);
    }
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtNombreCargo.setEditable(true);
    }
    
    private void editarCargos() {
        Cargos cargos = (Cargos) tblCargos.getSelectionModel().getSelectedItem();
        cargos.setId(Integer.parseInt(txtId.getText()));
        cargos.setNombre(txtNombreCargo.getText());
        
        try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCargos(?, ?)}");
            stmt.setInt(1, cargos.getId());
            stmt.setString(2, cargos.getNombre());
            stmt.execute();
            System.out.println(stmt.toString());
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarCargos() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCargos(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean existeElementosSeleccionados() {
        return tblCargos.getSelectionModel().getSelectedItem() !=null;
    }
    
    @FXML
    private void nuevo(ActionEvent event) {
    }
    
    @FXML
    private void eliminar(ActionEvent event) {
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
                    
                    
                    operacion = CargosController.Operaciones.ACTUALIZAR;
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

                    operacion = CargosController.Operaciones.NINGUNO;
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
                
                operacion = CargosController.Operaciones.NINGUNO;
                break;
        }
    }

    

    @FXML
    private void reporte(ActionEvent event) {
    Map parametros = new HashMap();
        
        GenerarReporte.getInstance().mostrarReporte("ReporteCargos.jasper", parametros, "Reporte de Cargos");
    }
    
}
