/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pablomich.controller;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 7/07/2021
 * @time 09:34:07 PM
 */
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
import org.pablomich.bean.Locales;
import org.pablomich.db.Conexion;
import org.pablomich.system.Principal;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 5/05/2021
 * @time 11:27:49 AM
 */
public class CuentasPorCobrarController implements Initializable {

    private Principal escenarioPrincipal;

    private enum Operaciones {
        NUEVO, GUARDAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String PAQUETE_IMAGES = "/org/jorgeperez/resource/images/";
    
    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;
    private ObservableList<Administracion> listaAdministracion;
    
    private HBox hboxNuevo;
    private ImageView imgNuevo;
    @FXML
    private Button btnNuevo;
    private ImageView imgModificar;
    @FXML
    private Button btnModificar;
    private HBox hboxEliminar;
    private ImageView imgEliminar;
    @FXML
    private Button btnEliminar;
    private HBox hboxReporte;
    private ImageView imgReporte;
    @FXML
    private Button btnReporte;
    @FXML
    private TableView tblCuentasPorCobrar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colFactura;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colMes;
    @FXML
    private TableColumn colValor;
    @FXML
    private TableColumn colEstado;
    @FXML
    private TableColumn colCodigoAdministracion;
    @FXML
    private TableColumn colCodigoCliente;
    @FXML
    private TableColumn colCodigoLocal;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFactura;

    @FXML
    private TextField txtValor;
    @FXML
    private TextField txtEstado;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbCliente;
    @FXML
    private ComboBox cmbLocal;
    
    @FXML
    private Spinner<Integer> spnAnio;

    private SpinnerValueFactory<Integer> valueFactoryAnio;

    @FXML
    private Spinner<Integer> spnMes;

    private SpinnerValueFactory<Integer> valueFactoryMes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 6);
        spnAnio.setValueFactory(valueFactoryAnio);
        spnMes.setValueFactory(valueFactoryMes);
        cargarDatos();
    }
    
    public boolean existeElementoSeleccionado() {
        return tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null;
    }
    
    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {
        ArrayList<CuentasPorCobrar> listado = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorCobrar}");
            System.out.println(pstmt);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                listado.add(new CuentasPorCobrar(
                        rs.getInt("id"), 
                        rs.getString("numeroFactura"), 
                        rs.getInt("anio"), 
                        rs.getInt("mes"), 
                        rs.getBigDecimal("valorNetoPago"), 
                        rs.getString("estadoPago"), 
                        rs.getInt("codigoAdministracion"), 
                        rs.getInt("codigoCliente"), 
                        rs.getInt("codigoLocal")
                    )
                );
            }
            
            listaCuentasPorCobrar = FXCollections.observableArrayList(listado);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Cuentas por cobrar en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();            
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return listaCuentasPorCobrar;
    }
    
    
    
    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {    
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listado.add(new Administracion(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                    )
                );
            }
            
            listaAdministracion = FXCollections.observableArrayList(listado);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla AdministraciÃ³n en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return listaAdministracion;
    }


    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
             pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            rs = pstmt.executeQuery();
            
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
            
            listaLocales = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Locales en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return listaLocales;
    }


    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes}");
            rs = pstmt.executeQuery();
            
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
            
            listaClientes = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Clientes en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();            
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaClientes;
    }
    
    public void cargarDatos() {
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValor.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstado.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoAdministracion"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoCliente"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoLocal"));
        cmbAdministracion.setItems(getAdministracion());
        cmbCliente.setItems(getClientes());
        cmbLocal.setItems(getLocales());
    }

    public Locales buscarLocales(int id) {
        Locales local = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarLocales(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                local = new Locales();
                local.setId(rs.getInt("id"));
                local.setSaldoFavor(rs.getBigDecimal("saldoFavor"));
                local.setSaldoContra(rs.getBigDecimal("saldoContra"));
                local.setMesesPendientes(rs.getInt("mesesPendientes"));
                local.setDisponibilidad(rs.getBoolean("disponibilidad"));
                local.setValorLocal(rs.getBigDecimal("valorLocal"));
                local.setValorAdministracion(rs.getBigDecimal("valorAdministracion"));
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar un Local con el id: " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return local;
    }
    
    
    public Clientes buscarClientes(int id) {
        Clientes cliente = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                cliente = new Clientes(
                        rs.getInt("id"), 
                        rs.getString("nombres"), 
                        rs.getString("apellidos"), 
                        rs.getString("telefono"), 
                        rs.getString("direccion"), 
                        rs.getString("email"), 
                        rs.getInt("codigoTipoCliente")
                );
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar un Cliente con el id: " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        
        return cliente;
    }
            
            
    public Administracion buscarAdministracion(int id) {
        Administracion administracion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                administracion = new Administracion(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                );
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar una AdministraciÃ³n con el id: " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();            
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }            
        }

        return administracion;
    }
    

    public void eliminarCuentasPorCobrar() {
        if (existeElementoSeleccionado()) {
            CuentasPorCobrar cuentaCobrar = (CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem();
            
            System.out.println(cuentaCobrar);
            
            PreparedStatement pstmt = null;
            
            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
                pstmt.setInt(1, cuentaCobrar.getId());
                
                System.out.println(pstmt);
                
                pstmt.execute();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Registro eliminado exitosamente");
                alert.show();
                
            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el registro con el id " + cuentaCobrar.getId());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    public void editarCuentasPorCobrar() {
               
         
          
        
        
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setId(Integer.parseInt(txtId.getText()));
        cuentaCobrar.setNumeroFactura(txtFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValor.getText()));
        cuentaCobrar.setEstadoPago(txtEstado.getText());
        cuentaCobrar.setCodigoAdministracion(((Administracion)cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            
            pstmt.setInt(1, cuentaCobrar.getId());
            
            pstmt.setString(2, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(3, cuentaCobrar.getAnio());
            pstmt.setInt(4, cuentaCobrar.getMes());
            pstmt.setBigDecimal(5, cuentaCobrar.getValorNetoPago());
            pstmt.setString(6, cuentaCobrar.getEstadoPago());
            pstmt.setInt(7, cuentaCobrar.getCodigoAdministracion());
            pstmt.setInt(8, cuentaCobrar.getCodigoCliente());
            pstmt.setInt(9, cuentaCobrar.getCodigoLocal());
            
            System.out.println(pstmt);
            
            pstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar editar una Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
    }
    
    public void agregarCuentasPorCobrar() {
        
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setNumeroFactura(txtFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValor.getText()));
        cuentaCobrar.setEstadoPago(txtEstado.getText());
        cuentaCobrar.setCodigoAdministracion(  ((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente( ((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal( ((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId() );
        
        System.out.println(cuentaCobrar);
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(2, cuentaCobrar.getAnio());
            pstmt.setInt(3, cuentaCobrar.getMes());
            pstmt.setBigDecimal(4, cuentaCobrar.getValorNetoPago());
            pstmt.setString(5, cuentaCobrar.getEstadoPago());
            pstmt.setInt(6, cuentaCobrar.getCodigoAdministracion());
            pstmt.setInt(7, cuentaCobrar.getCodigoCliente());
            pstmt.setInt(8, cuentaCobrar.getCodigoLocal());
            
            System.out.println(pstmt.toString());
            
            pstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar agregar un nueva Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean validar(ArrayList<TextField> listaTextField,ArrayList<ComboBox> listaComboBo){
        boolean respuesta = true;
        
        for(TextField campoTexto: listaTextField){
            if(campoTexto.getText().trim().isEmpty()){
                return false;
            
            }
        }
        
	return respuesta;
    }
        
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtFactura.setText( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura() );
            spnAnio.getValueFactory().setValue( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio() );
            spnMes.getValueFactory().setValue( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            txtValor.setText(String.valueOf( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago() ));
            txtEstado.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            cmbLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
        }
        
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void activarControles() {
        txtId.setEditable(false);

        txtFactura.setEditable(true);
        txtValor.setEditable(true);
        txtEstado.setEditable(true);

        spnAnio.setDisable(false);
        spnMes.setDisable(false);
        cmbAdministracion.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);

        txtFactura.setEditable(false);
        txtValor.setEditable(false);
        txtEstado.setEditable(false);

        spnAnio.setDisable(true);
        spnMes.setDisable(true);
        cmbAdministracion.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();

        txtFactura.clear();
        txtValor.clear();
        txtEstado.clear();

        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);

        cmbAdministracion.valueProperty().set(null);
        cmbCliente.valueProperty().set(null);
        cmbLocal.valueProperty().set(null);
    }

    @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO: // Nuevo
                activarControles();
                limpiarControles();

                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));

                btnModificar.setText("Cancelar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));

                hboxEliminar.setDisable(true);
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                
                hboxReporte.setDisable(true);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                
                operacion = Operaciones.GUARDAR;
                break;

            case GUARDAR:

                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtFactura);
                listaTextField.add(txtValor);
                listaTextField.add(txtEstado);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                
                //if (escenarioPrincipal.validar(listaTextField, listaComboBox))
                if (validar(listaTextField, listaComboBox)) {

                    agregarCuentasPorCobrar();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                    
                    
                    btnModificar.setText("Modificar");
                    imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                    
                    hboxEliminar.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEliminar.setOpacity(1);
                    
                    hboxReporte.setDisable(false);
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

                if (existeElementoSeleccionado()) {
                    activarControles();
                    
                    btnModificar.setText("Actualizar");
                    //imgModificar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                    
                    
                    btnEliminar.setText("Cancelar");
                    //imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                    
                    //hboxNuevo.setDisable(true);
                    //btnNuevo.setDisable(true);
                    //imgNuevo.setOpacity(0.15);
                    
                    //hboxReporte.setDisable(true);
                    //btnReporte.setDisable(true);
                    //imgReporte.setOpacity(0.15);
                    
                    
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

                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtId);
                listaTextField.add(txtFactura);
                listaTextField.add(txtValor);
                listaTextField.add(txtEstado);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);

                /*if (escenarioPrincipal.validar(listaTextField, listaComboBox)) {
                    
                    editarCuentasPorCobrar();
                    limpiarControles();
                    cargarDatos();
                    
                    btnModificar.setText("Modificar");
                    //imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                    
                    btnEliminar.setText("Eliminar");
                    //imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                    
                    //hboxNuevo.setDisable(false);
                    btnNuevo.setDisable(false);
                    //imgNuevo.setOpacity(1);
                    
                    //hboxReporte.setDisable(false);
                    btnReporte.setDisable(false);
                    //imgReporte.setOpacity(1);

                    operacion = Operaciones.NINGUNO;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor, llena todos los campos");
                    alert.show();
                }*/

                break;

            case GUARDAR: // CancelaciÃ³n de una inserciÃ³n
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                
                hboxEliminar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                
                hboxReporte.setDisable(false);
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
                
                hboxNuevo.setDisable(false);
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                
                hboxReporte.setDisable(false);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                
                btnModificar.setText("Modificar");
                imgModificar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));
                
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                
                operacion = Operaciones.NINGUNO;
                break;
                
            case NINGUNO: // Eliminar

                if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia!");
                    alert.setHeaderText(null);
                    alert.setContentText("Está seguro que desea continuar con la eliminación?");
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCuentasPorCobrar();
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


}
