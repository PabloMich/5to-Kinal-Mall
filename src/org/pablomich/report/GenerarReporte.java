package org.pablomich.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.pablomich.db.Conexion;


/**
 * 
 * @author Pablo Emmanuel Mich Mux // Codigo Técnico: IN5BM
 * @date 15/07/2021
 * @time 10:44:07 AM
 */
public class GenerarReporte {
    
    private static GenerarReporte instancia;
    
    public GenerarReporte() {    
    }
    
    public static GenerarReporte getInstance() {
        if (instancia == null) {
            instancia = new GenerarReporte();
        }
        return instancia;
    }
    
    public void mostrarReporte(String nombreReporte, Map parametros, String titulo) {
        try {
            System.out.println("buscando logo...");
            parametros.put("LOGO_HEADER", getClass().getResourceAsStream("org/pablomich/resource/imageslogo.jpg"));
            parametros.put("LOGO_FOOTER", getClass().getResourceAsStream("org/pablomich/resource/images/logo2.jpg"));

            InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reporte);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, 
                    parametros, 
                    Conexion.getInstance().getConexion()
            );
            
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(titulo);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
