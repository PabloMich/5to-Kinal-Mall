package org.pablomich.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.pablomich.bean.Clientes;
import org.pablomich.bean.TipoCliente;
import org.pablomich.system.Principal;
import org.pablomich.db.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.pablomich.bean.Administracion;
import org.pablomich.bean.CuentasPorCobrar;
import org.pablomich.bean.Locales;
import org.pablomich.report.GenerarReporte;



/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 5/05/2021
 * @time 11:27:49 AM
 */

public class ClientesController implements Initializable {

    // 1280x700
    private Principal escenarioPrincipal;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colCodigoTipoCliente;
    
    @FXML
    private ComboBox cmbTipoCliente;

    
    //@FXML
    //private ComboBox cmbLocal;
    
    //@FXML
    //private ComboBox cmbAdministracion;
    

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Clientes> listaClientes;
    private ObservableList<TipoCliente> listaTipoCliente;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize");
        cargarDatos();
        
    }
    
    
    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("Id"), rs.getString("descripcion")));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
        
    }
    
    
    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        try {
            //CallableStatement stmt;
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );
            }

            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
    }
    
    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            ResultSet rs = pstmt.executeQuery();
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
            }
            rs.close();
            pstmt.close();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }
  
    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("codigoTipoCliente")
                    )
                );
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
        
       
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
    
    private void eliminarClientes(){
        Clientes  cliente = ((Clientes)tblClientes.getSelectionModel().getSelectedItem());    
        
        System.out.println(cliente);
        
        try{
                 
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
            stmt.setInt(1,cliente.getId());                       
            stmt.execute();
            System.out.println(stmt.toString());
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
            
            
            }catch(SQLException e){
            
            if(e.getErrorCode() == 1452){
               JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
            
            }
            
        
        
        
        
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
        
        }
    
    
    
    }
    /*
    public void editarClientes(){
        ArrayList<TextField> listaCampos = new ArrayList<>();
        listaCampos.add(txtId);
        listaCampos.add(txtNombres);
        listaCampos.add(txtApellidos);
        listaCampos.add(txtTelefono);
        listaCampos.add(txtDireccion);
        listaCampos.add(txtEmail);        
            
            if(validar(listaCampos)){
               Clientes clientes = new Clientes();
               clientes.setId(Integer.parseInt(txtId.getText()));
               clientes.setNombres((txtNombres.getText()));
               clientes.setApellidos((txtApellidos.getText()));
               clientes.setTelefono((txtTelefono.getText()));
               clientes.setDireccion((txtDireccion.getText()));
               clientes.setEmail((txtEmail.getText()));
               clientes.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
            
            
            }
            
    
    }
     */
    public void editarClientes() {
          ArrayList<TextField> listaCampos = new ArrayList<>();
          listaCampos.add(txtId);
          listaCampos.add(txtNombres);
          listaCampos.add(txtApellidos);
          listaCampos.add(txtTelefono);
          listaCampos.add(txtDireccion);
          listaCampos.add(txtEmail); 
        
        if(validar(listaCampos)){
               Clientes clientes = new Clientes();
               clientes.setId(Integer.parseInt(txtId.getText()));
               clientes.setNombres((txtNombres.getText()));
               clientes.setApellidos((txtApellidos.getText()));
               clientes.setTelefono((txtTelefono.getText()));
               clientes.setDireccion((txtDireccion.getText()));
               clientes.setEmail((txtEmail.getText()));
               clientes.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
                 
               if(cmbTipoCliente.getSelectionModel().getSelectedItem() != null){
             clientes.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());              
             System.out.println(clientes);        
        
        }
           
        try{
        PreparedStatement psmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?,?,?,?,?,?)}");
        psmt.setString(1, clientes.getNombres());
        psmt.setString(2, clientes.getApellidos());
        psmt.setString(3, clientes.getTelefono());
        psmt.setString(4, clientes.getDireccion());
        psmt.setString(5, clientes.getEmail());
        psmt.setInt(6, clientes.getCodigoTipoCliente());
        
        
        psmt.execute();
        listaClientes.add(clientes);
        }catch(SQLException e){
            
            if(e.getErrorCode() == 1452){
               //JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
            }
            
        }
                                
         
        catch(Exception e ){
        e.printStackTrace();
             }
            }else{
                //JOptionPane.showMessageDialog(null, "Alguno de los campos se encuetra vacio");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Kinall Mall");
                alert.setHeaderText(null);
                alert.setContentText("Debe de rellenar los datos");
                alert.show();
                      
        }       
    }
    
     
     
     
     public void agregarCliente(){
        ArrayList<TextField> listaCampos = new ArrayList<>();
        //listaCampos.add(txtId);
        listaCampos.add(txtNombres);
        listaCampos.add(txtApellidos);
        //listaCampos.add(txtTelefono);
        listaCampos.add(txtDireccion);
        listaCampos.add(txtEmail);   
        
       if(validar(listaCampos)){
        Clientes clientes = new Clientes();
        clientes.setTelefono(txtNombres.getText());        
        clientes.setApellidos(txtApellidos.getText()); 
        clientes.setNombres(txtNombres.getText()); 
        clientes.setDireccion(txtDireccion.getText());
        clientes.setEmail(txtEmail.getText());
      
        
        if(cmbTipoCliente.getSelectionModel().getSelectedItem() != null){
             clientes.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());              
             System.out.println(clientes);        
        
        }
           
        try{
        PreparedStatement psmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?,?,?,?,?,?)}");
        psmt.setString(1, clientes.getNombres());
        psmt.setString(2, clientes.getApellidos());
        psmt.setString(3, clientes.getTelefono());
        psmt.setString(4, clientes.getDireccion());
        psmt.setString(5, clientes.getEmail());
        psmt.setInt(6, clientes.getCodigoTipoCliente());
        cargarDatos();
        
        psmt.execute();
        listaClientes.add(clientes);
        }catch(SQLException e){
            
            if(e.getErrorCode() == 1452){
               //JOptionPane.showMessageDialog(null, "Debe Seleccionar un tipo de cliente");
            }
            
        }
                                
         
        catch(Exception e ){
        e.printStackTrace();
             }
            }else{
                //JOptionPane.showMessageDialog(null, "Alguno de los campos se encuetra vacio");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Kinall Mall");
                alert.setHeaderText(null);
                alert.setContentText("Debe de rellenar los datos");
                alert.show();
            
        
            }   
         
         
       
     }

    public void cargarDatos() {
        
        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        
        colNombres.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));        
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoTipoCliente"));
        
       
        
        cmbTipoCliente.setItems(getTipoCliente());
    }
    
    public TipoCliente bucarTipoCliente(int id){
       TipoCliente registro = null;
       try {
           //Instanciamos al procedimiento para ser manipulado por los metodos necesarios para que funcione.
            PreparedStatement psmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarTipoCliente(?)}");
            psmt.setInt(1, id);
                ResultSet rs = psmt.executeQuery();
                while (rs.next()){
                    registro = new TipoCliente(rs.getInt("id"), rs.getString("descripcion"));                     
                }
                //rs.close();
                //psmt.close();
       
            } catch(Exception e){
                e.printStackTrace();
            
            }
            return registro;
       
            }
       
       
    public void activarControles(){
        txtId.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtEmail.setEditable(true); 
        cmbTipoCliente.setDisable(false);
        cargarDatos();
    
    }
    
    
    
    public void desactivarControles(){
        txtId.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtEmail.setEditable(false); 
        cmbTipoCliente.setDisable(true);
    }
    
    public void limpiarControles(){
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEmail.clear(); 
        cmbTipoCliente.getSelectionModel().clearSelection();
    
    }
    
    public boolean existeElementoSeleccionado(){
        if(tblClientes.getSelectionModel().getSelectedItem() == null){
            return false;
        }else{
            return true;
        
        }    
    }
        
    public void seleccionarElemento(){
        if (existeElementoSeleccionado()){        
        txtId.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getId()));
        txtNombres.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidos());
        txtTelefono.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefono());
        txtEmail.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getEmail());    
        cmbTipoCliente.getSelectionModel().select(bucarTipoCliente(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        
        }    
    
    }
     
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch(operacion){
            case NINGUNO:
                activarControles();
                //limpiarControles();
                operacion = Operaciones.GUARDAR;
                btnNuevo.setText("Guadar");
                btnModificar.setText("Cancelar");
                btnEliminar.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case GUARDAR:
                agregarCliente();
                cargarDatos();
                operacion = Operaciones.NINGUNO;
                btnNuevo.setText("Nuevo");
                btnEliminar.setDisable(false);
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);  
                break;         
        }
    
    }
        
    @FXML
    private void modificar(ActionEvent event) {
                 switch(operacion)
        {
            case NINGUNO:
                
                activarControles();              
                System.out.println("operacion" + operacion );                
                btnModificar.setText("Actualizar");
                btnEliminar.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnReporte.setDisable(true);
                 operacion = Operaciones.ACTUALIZAR;
                break;
                
            case ACTUALIZAR:
                System.out.println("operacion" + operacion );                
                btnModificar.setText("Modificar");
                btnEliminar.setText("Eliminar");
                btnNuevo.setDisable(false);
                btnReporte.setDisable(false);
                operacion = ClientesController.Operaciones.NINGUNO;                
                
                 if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("Está seguro que desea modificarlo con la eliminación?");
                    
                    ButtonType btnConfirmacionSi = new ButtonType("Si");
                    ButtonType btnConfirmacionNo = new ButtonType("N0");
                    alert.getButtonTypes().clear();
                
                    alert.getButtonTypes().addAll(btnConfirmacionSi,btnConfirmacionNo);
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                        if (respuesta.get() == btnConfirmacionSi) {
                           
                        editarClientes();               
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
                break;
                
            
            case GUARDAR: // Cancela incersion
                
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnModificar.setText("Modificar");
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = ClientesController.Operaciones.NINGUNO;
                break;
        }
                            
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch(operacion){
            case ACTUALIZAR:
            btnNuevo.setDisable(false);
            btnReporte.setDisable(false);
            btnModificar.setText("Modificar");
            btnEliminar.setText("Eliminar");
            limpiarControles();
            operacion = Operaciones.NINGUNO;
            break;
            case NINGUNO: //Eliminar
             
             if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("Â¿EstÃ¡ seguro que desea continuar con la eliminaciÃ³n?");
                    
                    ButtonType btnConfirmacionSi = new ButtonType("Si");
                    ButtonType btnConfirmacionNo = new ButtonType("N0");
                    alert.getButtonTypes().clear();
                
                    alert.getButtonTypes().addAll(btnConfirmacionSi,btnConfirmacionNo);
                    
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                    if (respuesta.get() == btnConfirmacionSi) {
                        eliminarClientes();
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
    void regresar(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    @FXML
    private void reporte(ActionEvent event) {
        Map parametros = new HashMap();
        
        GenerarReporte.getInstance().mostrarReporte("ReporteTipoCliente.jasper", parametros, "Reporte de Clientes");
    }

    

}

