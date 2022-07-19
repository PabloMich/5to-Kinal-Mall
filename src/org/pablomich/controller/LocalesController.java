package org.pablomich.controller;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import org.pablomich.bean.Administracion;
import org.pablomich.bean.Clientes;
import org.pablomich.bean.Locales;
import org.pablomich.bean.TipoCliente;
import org.pablomich.db.Conexion;
import org.pablomich.report.GenerarReporte;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 7/07/2021
 * @time 09:52:31 PM
 */
public class LocalesController implements Initializable{

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
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TextField txtValorAdministracion;
    @FXML
    private CheckBox chkDisponibilidad;
    @FXML
    private TableView tblLocal;
    
    Principal escenarioPrincipal;
    @FXML
    private TextField txtCantidadDisponible;



    private void mostrarTexto(ActionEvent event) {
            Locales local = new Locales();
          if(chkDisponibilidad.isSelected() == chkDisponibilidad.isSelected()){
            chkDisponibilidad.setSelected(true);
            chkDisponibilidad.setText("Disponible");
            colDisponibilidad.setText("Disponible");
            chkDisponibilidad.getAccessibleText();
            local.setDisponibilidad(true);
           
          }
          
    
    }



    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    
    private final String PAQUETE_IMAGES = "/org/pablomich/resource/images/";

    private ObservableList<Clientes> listaClientes;
    private ObservableList<TipoCliente> listaTipoCliente;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;
    
     
    public boolean validar(ArrayList<TextField> listadoCampos){
        boolean respuesta = true;
        
        for(TextField campoTexto: listadoCampos){
            if(campoTexto.getText().trim().isEmpty()){
                return false;
            
            }
        }
        return respuesta;
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

    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicialdo...");
        cargarDatos();
    }
    
      public ObservableList<Locales> getLocales() {
          System.out.println("Consultando DB.Locales");
        ArrayList<Locales> lista = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            ResultSet rs = pstmt.executeQuery();
            
            int contador = 0;
            
            while (rs.next()) {
                Locales registro = new Locales();
                registro.setId(rs.getInt("id"));
                registro.setSaldoFavor(rs.getBigDecimal("saldoFavor"));
                registro.setSaldoContra(rs.getBigDecimal("saldoContra"));
                registro.setMesesPendientes(rs.getInt("mesesPendientes"));
                registro.setDisponibilidad(rs.getBoolean("disponibilidad"));
                registro.setValorLocal(rs.getBigDecimal("valorLocal"));
                registro.setValorAdministracion(rs.getBigDecimal("valorAdministracion"));
                lista.add(registro);
                
                listaLocales = FXCollections.observableArrayList(lista);
                
                if (rs.getBoolean("disponibilidad")) {
                    contador ++;
                }
            }
            
            txtCantidadDisponible.setText(String.valueOf(contador));
            
            rs.close();
            pstmt.close();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }
      
      
      
        public void agregarLocal(){
        ArrayList<TextField> listaCampos = new ArrayList<>();
        //listaCampos.add(txtId);
        listaCampos.add(txtSaldoFavor);
        listaCampos.add(txtSaldoContra);
        listaCampos.add(txtMesesPendientes);
        //listaCampos.add(txtTelefono);
        listaCampos.add(txtValorLocal);
        listaCampos.add(txtSaldoLiquido);
      
        listaCampos.add(txtValorAdministracion);
        
        //if(validar(listaCampos)){
        //Lo que hacemo saqui es llamar a las variables y convertirlas 
        //A al valor de la vista, O Textfield.  
        Locales local = new Locales();  
        System.out.println(txtSaldoFavor);
            System.out.println(chkDisponibilidad);
        //Recordar siempre, siempre se pueden convertir dos veces sss
        // supongamo de Big decimal a double para que despues se pueda usar como texto.
        local.setSaldoFavor(BigDecimal.valueOf(Double.valueOf(txtSaldoFavor.getText())));
        local.setSaldoContra(BigDecimal.valueOf(Double.valueOf(txtSaldoContra.getText())));
        local.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        local.setDisponibilidad(Boolean.getBoolean(String.valueOf(chkDisponibilidad.isSelected())));  
        local.setValorLocal(BigDecimal.valueOf(Double.valueOf(txtValorLocal.getText())));        
        local.setValorAdministracion(BigDecimal.valueOf(Double.valueOf(txtValorAdministracion.getText())));    
       
      
        /*
        if(cmbTipoCliente.getSelectionModel().getSelectedItem() != null){
            clientes.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());        
            System.out.println(clientes);        
        
        
        }*/
        //Aqui es donde llamamos a las variables nuestra clase x para que funcione
        //Tambien preparamos la conexion para que todo sea exitoso :)
        try{
        PreparedStatement psmt = Conexion.getInstance().getConexion().prepareCall("{CALL  sp_AgregarLocales(?,?,?,?,?,?)}");
        psmt.setBigDecimal(1, local.getSaldoFavor());
        psmt.setBigDecimal(2, local.getSaldoContra());
        psmt.setInt(3, local.getMesesPendientes());
        psmt.setBoolean(4, local.isDisponibilidad());
        psmt.setBigDecimal(5, local.getValorLocal());
        psmt.setBigDecimal(6, local.getValorAdministracion());
        
        
        psmt.execute();
        listaLocales.add(local);
        
        }catch(SQLException e){
            if(e.getErrorCode() == 1452){
                JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
            }
            
        }
                                
         
        catch(Exception e ){
        e.printStackTrace();
             }
           // }else{
                //JOptionPane.showMessageDialog(null, "Alguno de los campos se encuetra vacio");
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Kinall Mall");
                alert.setHeaderText(null);
                alert.setContentText("Antes de continual un tipo de cliente");
                alert.show();
            
        
            }   
         
         
       
    //}
        
        
        
        public void editarLocales(){
        ArrayList<TextField> listaCampos = new ArrayList<>();
        listaCampos.add(txtId);
        listaCampos.add(txtSaldoFavor);
        listaCampos.add(txtSaldoContra);
        listaCampos.add(txtMesesPendientes);
        listaCampos.add(txtValorLocal);
        listaCampos.add(txtSaldoLiquido);        
            
            if(validar(listaCampos)){
                             
                Locales local = new Locales();   
                local.setId(Integer.parseInt(txtId.getText()));
                local.setSaldoFavor(BigDecimal.valueOf(txtSaldoFavor.getText().charAt(9)));
                local.setSaldoContra(BigDecimal.valueOf(txtSaldoContra.getText().charAt(9)));
                local.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
                local.setDisponibilidad(Boolean.getBoolean(chkDisponibilidad.getTypeSelector()));  
                local.setValorLocal(BigDecimal.valueOf(txtValorLocal.getText().charAt(9))); 
                local.setValorAdministracion(BigDecimal.valueOf(txtValorAdministracion.getText().length()));
            }
    
    }
        
            private void eliminarLocales(){
                  Locales  local = ((Locales)tblLocal.getSelectionModel().getSelectedItem());    
                
            try{
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1,local.getId());                       
            stmt.execute();
            System.out.println(stmt.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
        
        //Aqui es donde cargamos los datos para que todo aparezca en la tabla de 
        //la tabla que nosotros queramos.
            public void cargarDatos() {
        
        tblLocal.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, String>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, String>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, String>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, String>("valorLocal"));
        //colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoLocal"));
        //colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoAdministracion"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, String>("valorAdministracion"));
        
        //cmbLocal.setItems(getLocales());
        
        //cmbAdministracion.setItems(getAdministracion());
        
        //cmbTipoCliente.setItems(getTipoCliente());
    }
         public boolean existeElementoSeleccionado(){
        if(tblLocal.getSelectionModel().getSelectedItem() == null){
            return false;
        }else{
            return true;
        
        }    
    }
        
    public void seleccionarElemento(){
        if (existeElementoSeleccionado()){        
        txtId.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getId()));
        txtSaldoFavor.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getSaldoFavor()));
        txtValorLocal.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getValorLocal()));  
        txtSaldoContra.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getSaldoContra()));
        txtValorAdministracion.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getValorAdministracion()));
        txtMesesPendientes.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getMesesPendientes())); 
        chkDisponibilidad.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).isDisponibilidad())); 
        
        //cmbTipoCliente.getSelectionModel().select(bucarTipoCmdliente(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        
        }    
    
    }
     
            
            
        public void activarControles(){
        txtId.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtMesesPendientes.setEditable(true);
        txtValorLocal.setEditable(true);
        txtSaldoLiquido.setEditable(true);
        txtSaldoContra.setEditable(true); 
        txtValorAdministracion.setEditable(true); 
        chkDisponibilidad.setDisable(false);
    
    }
    
    
    public void desactivarControles(){
        txtId.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtMesesPendientes.setEditable(false);
        txtValorLocal.setEditable(false);
        txtSaldoLiquido.setEditable(false);
        txtSaldoContra.setEditable(false); 
        txtValorAdministracion.setEditable(true); 
        chkDisponibilidad.setDisable(true);
    }
    
    public void limpiarControles(){
        txtId.clear();
        txtSaldoFavor.clear();
        txtMesesPendientes.clear();
        txtValorLocal.clear();
        txtSaldoLiquido.clear();
        txtSaldoContra.clear(); 
        txtValorAdministracion.clear();
        //cmbTipoCliente.getSelectionModel().clearSelection();
    
    }
    
    
            
    
    
    
    
    @FXML
    private void nuevo(ActionEvent event) {
         switch(operacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                operacion = LocalesController.Operaciones.GUARDAR;
                btnNuevo.setText("Guardar");
                System.out.println("...");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case GUARDAR:
                
                agregarLocal();
                cargarDatos();
                operacion = LocalesController.Operaciones.NINGUNO;
                System.out.println("Hola mundo 34");
                btnNuevo.setText("Nuevo");
                btnEliminar.setDisable(false);
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);  
                break;         
        }   
    }

    @FXML
    private void modificar(ActionEvent event) {
    
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
                    operacion = LocalesController.Operaciones.NINGUNO;
                    break;
                case NINGUNO: //ELIMINAR                                        
                    eliminarLocales();                                         
                    
                    cargarDatos();    
                    break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        Map parametros = new HashMap();
        
        GenerarReporte.getInstance().mostrarReporte("ReporteLocales.jasper", parametros, "Reporte de Locales");
    
    }
}
