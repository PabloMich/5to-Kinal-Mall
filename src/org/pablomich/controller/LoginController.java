package org.pablomich.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.pablomich.system.Principal;
import java.sql.*;
import org.pablomich.db.Conexion;
import java.util.Base64;

/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 4/08/2021
 * @time 10:36:29 
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField pfPass;
    
    private Principal escenarioPrincipal;

    private String passDB;
    
    private String userDB;
    
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
   private void validar(ActionEvent event) {
       
       if ( !(txtUser.getText().equals("") || pfPass.getText().equals("")) ) {
       String user = txtUser.getText().trim();
       String pass = pfPass.getText().trim();
       
           System.out.println("user" + user);
           System.out.println("pass" + pass);
       
       getPassword(user);
       String pass64 = new String(Base64.getDecoder().decode(this.passDB));
       
           System.out.println("user.equals(this.user):" + user.equals(this.userDB));
           System.out.println("pass.equals(this.pass):" + pass.equals(this.passDB));
       
       if ((user.equals(this.userDB)) && (pass.equals(pass64))) {
           System.out.println("Bienvenido");
           escenarioPrincipal.mostrarMenuPrincipal();
       } else {
           System.out.println("Datos incorrectos");
       }
   }
   }
   
   private void getPassword(String user) {
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       
       try {
           pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarUsuario(?)}");
           pstmt.setString(1, user);
           System.out.println(pstmt.toString());
           rs = pstmt.executeQuery();
           
           while(rs.next()) {
               this.userDB = rs.getString("user");
               passDB = rs.getString("pass");
               System.out.println("pass db: " + passDB);
               escenarioPrincipal.setRol(rs.getInt("rol"));
               System.out.println("rol db: " + escenarioPrincipal.getRol());
           }
           
       } catch (SQLException e) {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {
               if (pstmt != null) {
                   pstmt.close();
               }
               if (rs != null) {
                   rs.close();
               }
           } catch (Exception e) {
               
           }
       }
   }

}
