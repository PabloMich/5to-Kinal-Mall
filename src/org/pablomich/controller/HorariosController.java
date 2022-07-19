package org.pablomich.controller;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 8/07/2021
 * @time 11:49:36 PM
 */
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.pablomich.bean.Horarios;
import org.pablomich.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import org.pablomich.db.Conexion;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.pablomich.report.GenerarReporte;

/**
 * FXML Controller class
 *
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 */
public class HorariosController implements Initializable {

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView tblHorarios;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colHorarioEntrada;
    @FXML
    private TableColumn colHorarioSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;
    @FXML
    private JFXTimePicker tpHorarioEntrada;
    @FXML
    private JFXTimePicker tpHorarioSalida;
    @FXML
    private CheckBox chkLunes;
    @FXML
    private CheckBox chkMartes;
    @FXML
    private CheckBox chkJueves;
    @FXML
    private CheckBox chkMiercoles;
    @FXML
    private CheckBox chkViernes;
    @FXML
    private TextField txtId;
    
    Principal escenarioPrincipal;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgModificar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    
    
    private enum Operaciones {
        UEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    private Operaciones operacion = Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";
    
    private ObservableList<Horarios> listaHorarios;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Iniciando...");
        cargarDatos();
        tpHorarioEntrada.setEditable(false);
        tpHorarioSalida.setEditable(false);
    }    
    
    public ObservableList<Horarios> getHorarios() {
        System.out.println("Consultando DB.Horarios...");
        ArrayList<Horarios> lista = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Horarios horario = new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("Martes"),
                        rs.getBoolean("Miercoles"),
                        rs.getBoolean("Jueves"),
                        rs.getBoolean("Viernes"));
                lista.add(horario);
            }
            
            System.out.println("Mostrando Resultados...");
            for (Horarios horarios : lista) {
                System.out.println(horarios);
            }
            
            listaHorarios = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la lista de Horarios");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                
            }
        }
        return listaHorarios;
    }
    
    public void agregarHorarios() {
        Horarios horario = new Horarios();
        horario.setHorarioEntrada(Time.valueOf(tpHorarioEntrada.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpHorarioSalida.getValue()));
        horario.setLunes(chkLunes.isSelected());
        horario.setMartes(chkMartes.isSelected());
        horario.setMiercoles(chkMiercoles.isSelected());
        horario.setJueves(chkJueves.isSelected());
        horario.setViernes(chkViernes.isSelected());
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarHorarios(?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setTime(1, horario.getHorarioEntrada());
            pstmt.setTime(2, horario.getHorarioSalida());
            pstmt.setBoolean(3, horario.isLunes());
            pstmt.setBoolean(4, horario.isMartes());
            pstmt.setBoolean(5, horario.isMiercoles());
            pstmt.setBoolean(6, horario.isJueves());
            pstmt.setBoolean(7, horario.isViernes());
            
            System.out.println(pstmt.toString());
            
            pstmt.execute();
        } catch (Exception e) {
            System.err.println("\nSe produjo un error al intentar agregar un nuevo Horario");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementosSeleccionados()) {
            tpHorarioEntrada.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHorarioSalida.setValue( ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());
            chkLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            chkMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            chkMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            chkJueves.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            chkViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }
    }
    
    public void cargarDatos () {
        System.out.println("Cargando Datos...");
        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
        
        
    }
    
    public boolean existeElementosSeleccionados() {
        return tblHorarios.getSelectionModel().getSelectedItem() !=null;
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        tpHorarioEntrada.setDisable(false);
        tpHorarioSalida.setDisable(false);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);    
    }
    
    public void limpiarControles() {
        txtId.clear();
        tpHorarioEntrada.getEditor().clear();
        tpHorarioSalida.getEditor().clear();
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJueves.setSelected(false);
        chkViernes.setSelected(false);
    }
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtId.setDisable(true);
        tpHorarioEntrada.setDisable(true);
        tpHorarioSalida.setDisable(true);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true); 
    }
    
   @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO: // Nuevo
                activarControles();
                limpiarControles();

                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));

                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                
                operacion = Operaciones.GUARDAR;
                break;

            case GUARDAR:
                
                //if (escenarioPrincipal.validar(listaTextField, listaComboBox))
                //if (validar(listaTextField, listaComboBox)) {
                    if((tpHorarioEntrada.getValue() != null) && (tpHorarioSalida.getValue() != null)) {
                    agregarHorarios();
                    cargarDatos();
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
                                       
                    operacion = Operaciones.NINGUNO; 
                    
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Antes de continuar, llene todos los campos");
                        alert.show();
                    }

                break;
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
                    
                    
                    operacion = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar, selecciona un registro");
                    alert.show();
                }
                break;
                
            case ACTUALIZAR:
/*
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtId);
                listaTextField.add(txtFactura);
                listaTextField.add(txtValor);
                listaTextField.add(txtEstado);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
*/
                //if (escenarioPrincipal.validar(listaTextField, listaComboBox)) {
                    
                    //editarHorarios();
                    limpiarControles();
                    //cargarDatos();
                    
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                    
                    btnEliminar.setText("Eliminar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                    
                    btnNuevo.setDisable(false);
                    imgNuevo.setOpacity(1);
                    
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);

                    operacion = Operaciones.NINGUNO;
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
                
                operacion = Operaciones.NINGUNO;
                break;
        }
        
    }

    @FXML
    private void eliminar(ActionEvent event) {
        
        switch (operacion) {
            case ACTUALIZAR: // Cancelar
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                
                operacion = Operaciones.NINGUNO;
                break;
                
            case NINGUNO: // Eliminar

                if (existeElementosSeleccionados()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("Esta seguro que desea continuar con la eliminacion?");
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                    if (respuesta.get() == ButtonType.OK) {
                        //eliminarHorarios();
                        limpiarControles();
                        //cargarDatos();
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
    
    public boolean validarFecha(String date) {
        String patron = "[0-9]{4}[-/.](0[0-9]|1[012])[-/.](0[0-9]|1[0-9]|2[0-9]|3[01])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(date);
        System.out.println(matcher.matches());
        return matcher.matches();
    }

    public boolean validarTiempo(String time) {
        String patron = "([01][0-9]|[2][0123]):([0-5][0-9]):([0-5][0-9])";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(time);
        System.out.println(matcher.matches());
        return matcher.matches();
    }
    
    private void mostrarVistaEmpleados() {
        
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
        GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", parametros, "Reporte de Horarios");
    }
    
}
