package gestorproyectos.controller;

import gestorproyectos.modelo.dao.DocumentoDAO;
import gestorproyectos.modelo.dao.ReporteDAO;
import gestorproyectos.modelo.pojo.Documento;
import gestorproyectos.modelo.pojo.Reporte;
import gestorproyectos.utilidades.MisUtilidades;
import gestorproyectos.utilidades.Validador;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLEntregaDeArchivoController implements Initializable {

	@FXML
	private TextField txtFNombreEntrega;
	@FXML
	private ComboBox<String> cBoxTipoArchivo;
	@FXML
	private TextField txtFCantidadHoras;
        private File archivo;
        private int idExpediente;
        @FXML
        private Label lbConfirmacionArchivo;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
            llenarCombo();
	}	

        public void cargarExpediente(int idExpediente){
            this.idExpediente = idExpediente;
        }
        
	@FXML
	private void clickSubirArchivo(ActionEvent event) {
            FileChooser dialogoSeleccion = new FileChooser();
            dialogoSeleccion.setTitle("Seleccionar archivo");
            String etiquetaTipoDato = "Archivo (*.pdf)";
            FileChooser.ExtensionFilter filtroArchivo = new FileChooser.ExtensionFilter(etiquetaTipoDato, "*.pdf");
            dialogoSeleccion.getExtensionFilters().add(filtroArchivo);
            Stage escenarioActual = (Stage) txtFNombreEntrega.getScene().getWindow();
            archivo = dialogoSeleccion.showOpenDialog(escenarioActual);
            if(archivo != null){
                mostrarMensajeConfirmacion(archivo.getName());
            }
	}

	@FXML
	private void clickEntregar(ActionEvent event) {
        boolean datosValidos = datosValidos();
        if (datosValidos){
            try{
                if(cBoxTipoArchivo.getSelectionModel().getSelectedItem()=="Documento"){
                    Documento documento = obtenerDocumento();
                    HashMap<String,Object> respuesta = DocumentoDAO.subirDocumento(documento);
                        if(!(boolean)respuesta.get("error")){
                            MisUtilidades.crearAlertaSimple( Alert.AlertType.ERROR,"Documento guardado", ""+respuesta.get("mensaje"));
                            cerrarVentana();
                        }else{
                            MisUtilidades.crearAlertaSimple( Alert.AlertType.ERROR,"Error", ""+respuesta.get("mensaje"));
                        } 
                }else{
                    Reporte reporte = obtenerReporte();
                    HashMap<String,Object> respuesta = ReporteDAO.subirReporte(reporte);
                        if(!(boolean)respuesta.get("error")){
                            MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Reporte guardado", ""+respuesta.get("mensaje"));
                            cerrarVentana();
                        }else{
                            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", ""+respuesta.get("mensaje"));
                        } 
                }
            }catch (SQLException ex) {
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo subir el archivo.");
            }  
        }else{
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos, los datos estan incompletos");
        }
	}

    
    private void mostrarMensajeConfirmacion(String nombreArchivo) {
        lbConfirmacionArchivo.setVisible(true);
        lbConfirmacionArchivo.setText(nombreArchivo);
    }
    
    private Reporte obtenerReporte() {
        Reporte reporte = new Reporte();
        reporte.setReporte(obtenerArchivo());
        reporte.setNombreReporte(txtFNombreEntrega.getText());
        reporte.setHorasReportadas(Integer.parseInt(txtFCantidadHoras.getText()));
        reporte.setValidado("Entregado");
        reporte.setNoReporte(obtenerNumeroReporte()+1);
        reporte.setIdExpediente(idExpediente);
        
        return reporte;
    }

    private Documento obtenerDocumento() {
        LocalDate fechaActual = LocalDate.now();
        
        Documento documento = new Documento();
        documento.setNombreDocumento(txtFNombreEntrega.getText());
        documento.setDocumento(obtenerArchivo());
        documento.setFechaEntrega(fechaActual.toString());
        documento.setValidado("Entregado");
        documento.setIdExpediente(idExpediente);
        
        return documento;
    }

    private byte[] obtenerArchivo() {
        try{
            if(archivo!=null){
                byte[] archivoBytes = Files.readAllBytes(archivo.toPath());
                return archivoBytes;
            }else{
                MisUtilidades.crearAlertaSimple( Alert.AlertType.ERROR,"Error", "Lo sentimos, el archivo seleccionado no puede ser guardado");
                return null;
            }            
        }catch (IOException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos, el archivo seleccionado no puede ser guardad");
            return null;
        }
    }

    private int obtenerNumeroReporte() {
        try{
        HashMap<String,Object> respuesta = (HashMap<String,Object>) ReporteDAO.obtenerReportes(idExpediente);
        boolean isError = (boolean) respuesta.get("error");
        if(!isError){
            ArrayList<Reporte> reportesBD = (ArrayList<Reporte>) respuesta.get("reportes");
            return reportesBD.size();
        }else{
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error","" + respuesta.get("mensaje"));
            return -1;
        }
        }catch (SQLException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo subir el archivo.");
            return -1;
        } 
    }

    private void llenarCombo() {
        ObservableList<String> opcionesArchivo = FXCollections.observableArrayList("Documento","Reporte");
        cBoxTipoArchivo.setItems(opcionesArchivo);
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtFCantidadHoras.getScene().getWindow();
        stage.close();
    }
    
    private boolean datosValidos() {
        try{
        Validador.validarTexto(txtFNombreEntrega.getText(), "Nombre de la entrega", 50);
        Validador.validarHoras(Integer.parseInt(txtFCantidadHoras.getText()));
        if (archivo==null || cBoxTipoArchivo.getSelectionModel().getSelectedItem().isEmpty()){
            return false;
        }
        return true;
        }catch(IllegalArgumentException ex){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", ex.getMessage());
            return false;
        }
        
    }    
}
