package gestorproyectos.controller;

import gestorproyectos.modelo.dao.DocumentoDAO;
import gestorproyectos.modelo.dao.ExpedienteDAO;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.dao.ReporteDAO;
import gestorproyectos.modelo.pojo.Documento;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.Empresa;
import gestorproyectos.modelo.pojo.Expediente;
import gestorproyectos.modelo.pojo.InscripcionEE;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.pojo.Reporte;
import gestorproyectos.modelo.pojo.Responsable;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLExpedienteAlumnoController implements Initializable {

	@FXML
	private Label lblResultadoProyectp;
	@FXML
	private Label lblResultadoEmpresa;
	@FXML
	private Label lblResultadoResponsable;
	@FXML
	private Label lblResultadoPeriodo;
	@FXML
	private Label lblResultadoFecha;
	@FXML
	private Label lblResultadoDescripcion;
	@FXML
	private Label lblResultadoObjetivo;
	@FXML
	private TableView<Documento> tblDocumentos;
	@FXML
	private TableColumn colNombreDocumento;
	@FXML
	private TableColumn colValidar;
	@FXML
	private TableView<Reporte> tblReportes;
	@FXML
	private TableColumn colNombreReporte;
	@FXML
	private TableColumn colHoras;
        private int idInscripcionEE;
        private int idExpediente;
        private Empresa empresa;
        private Responsable responsable;
        private InscripcionEE inscripcionEE;
        private Expediente expediente;
        private ObservableList<Documento> documentos;
        private ObservableList<Reporte> reportes;
        private ProyectoSS proyectoSS;
        private ProyectoPP proyectoPP;
        private boolean tipoProyectoPP;
        private EE ee;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
            llenarTablaDocumentos();
            if(tipoProyectoPP){
            llenarInformacionPP();
            }else{
            llenarInformacionSS(); 
            }
	}
        
        public void inicializarValores (Empresa empresa, Responsable responsable,EE ee, InscripcionEE inscripcionEE, Expediente expediente, ProyectoPP proyectoPP){
            this.empresa=empresa;
            this.responsable=responsable;
            this.ee=ee;
            this.inscripcionEE=inscripcionEE;
            this.expediente=expediente;
            this.idInscripcionEE=inscripcionEE.getIdInscripcionEE();
            this.idExpediente=expediente.getIdExpediente();
            this.proyectoPP=proyectoPP;
            tipoProyectoPP=true;
        }
        
        public void inicializarValores (Empresa empresa, Responsable responsable,EE ee, InscripcionEE inscripcionEE, Expediente expediente, ProyectoSS proyectoSS){
            this.empresa=empresa;
            this.responsable=responsable;
            this.ee=ee;
            this.inscripcionEE=inscripcionEE;
            this.expediente=expediente;
            this.idInscripcionEE=inscripcionEE.getIdInscripcionEE();
            this.idExpediente=expediente.getIdExpediente();
            this.proyectoSS=proyectoSS;
            tipoProyectoPP=false;
        }        

	@FXML
	private void clickDarDeBaja(ActionEvent event) {
            boolean confirmacionBaja = MisUtilidades.crearAlertaConfirmacion("Confirmar dada de baja", "¿Seguro que desea dar de baja al alumno de este proyeecto?, esta accion es irreversible");
            if(confirmacionBaja){
                eliminarExpediente();
                eliminarInscripcionEE();
            }
	}
        
    private void eliminarExpediente() {
        try{
        Expediente expediente = ExpedienteDAO.obtenerExpediente(idInscripcionEE);
        HashMap<String,Object> respuestaDocumento = DocumentoDAO.DarDeBaja(expediente.getIdExpediente());
        HashMap<String,Object> respuestaReporte = ReporteDAO.DarDeBaja(expediente.getIdExpediente());
        HashMap<String,Object> respuestaExpediente = ExpedienteDAO.DarDeBaja(expediente.getIdExpediente());
            if(!((boolean)respuestaExpediente.get("error")) ){
                MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Expediente eliminado", ""+respuestaExpediente.get("mensaje") );
            }else{
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error al eliminar el expediente", ""+respuestaExpediente.get("mensaje"));
            }
        }catch(SQLException ex){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
        }
    }

    private void eliminarInscripcionEE() {
        try{
        HashMap<String,Object> respuesta = InscripcionEEDAO.DarDeBaja(idInscripcionEE);
            if(!((boolean)respuesta.get("error")) ){
                MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Alumno dado de baja", ""+respuesta.get("mensaje"));
                //cerrar ventana
            }else{
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error al dar de baja", ""+respuesta.get("mensaje") );
            }
        }catch(SQLException es){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
        }
    }        
	
    @FXML
    private void clicValidar() {
        boolean confirmarValidar = MisUtilidades.crearAlertaConfirmacion("Confirmacion", "Seguro que desea validar el documento, esta accion no se puede deshacer");
        if(confirmarValidar){
            Documento documento = tblDocumentos.getSelectionModel().getSelectedItem();
            try{
            if(documento!=null){
                HashMap<String,Object> respuesta = DocumentoDAO.validarDocumento(documento.getIdDocumento());
                    if(!((boolean)respuesta.get("error")) ){
                        // recargar datos de la tabla
                        MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Documento validado", "El documento fue validado");
                    }else{
                        MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error al validar el documento, favor de intentar mas tarde", ""+respuesta.get("error"));
                    }
            }else{
                MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING,"Selecciona un documento", "Debes seleccionar un documento para poder validarlo");
            }
            }catch(SQLException es){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
        }
        }
    }
    
    private void guardarArchivoPDF(byte[] contenidoPDF ) {
    DirectoryChooser directorio = new DirectoryChooser();
    directorio.setTitle("Seleccionar directorio para guardar el PDF");

    File directorioDestino = directorio.showDialog(tblDocumentos.getScene().getWindow());
    
    if (directorioDestino != null) {
        try {

            String nombreArchivo =  tblDocumentos.getSelectionModel().getSelectedItem().getNombreDocumento()+".pdf"; 
            File archivoPDF = new File(directorioDestino, nombreArchivo);
            

            try (OutputStream salida = new FileOutputStream(archivoPDF)) {
                salida.write(contenidoPDF);
                MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Archivo descargado correctamente", "El documento fue descargado correctamente");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("No se seleccionó ningún directorio para guardar el PDF.");
    }
}    
        
    private void clicDescargarDocumento() {
        byte[] archivoBytes = (byte[]) tblDocumentos.getSelectionModel().getSelectedItem().getDocumento();
        if (archivoBytes != null && archivoBytes.length > 0) {
            guardarArchivoPDF(archivoBytes);
        } else {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING,"Error al cargar el archivo", "No se encontro el archivo que deseas descargar");
        }
    }    

    @FXML
    private void clicDescargar(ActionEvent event) {
        clicDescargarDocumento();
    }

    private void llenarTablaDocumentos() {
        configurarTablaDocumentos();
        cargarInformacionTablaDocumentos();
    }
    
     private void configurarTablaDocumentos() {
        colNombreDocumento.setCellValueFactory(new PropertyValueFactory("nombreDocumento"));
        colValidar.setCellValueFactory(new PropertyValueFactory("validado"));
    }

    private void cargarInformacionTablaDocumentos() {
        documentos = FXCollections.observableArrayList();
        try{
        List<Documento> documentosBD = DocumentoDAO.obtenerDocumentos(idExpediente);
        if(!documentosBD.isEmpty()){
            documentos.addAll(documentosBD);
            tblDocumentos.setItems(documentos);
        }else{
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos no se puede cargar en este "
                    + "momento la informacion de los clientes");
        }
        }catch(SQLException e){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos, no se puede cargar la informacion en este momento ");
        }
    }   
    
    private void llenarTablaReportes() {
        configurarTablaReportes();
        cargarInformacionTablaReportes();
    }
    
     private void configurarTablaReportes() {
        colNombreReporte.setCellValueFactory(new PropertyValueFactory("nombreReporte"));
        colHoras.setCellValueFactory(new PropertyValueFactory("horasReportadas"));
    }

    private void cargarInformacionTablaReportes() {
        reportes = FXCollections.observableArrayList();
        try{
        HashMap<String,Object> respuesta = ReporteDAO.obtenerReportes(idExpediente);
        boolean isError = (boolean) respuesta.get("error");
        if(!isError){
            ArrayList<Reporte> reportesBD = (ArrayList<Reporte>) respuesta.get("reportes");
            reportes.addAll(reportesBD);
            tblReportes.setItems(reportes);
        }else{
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos no se puede cargar en este "
                    + "momento la informacion de los clientes");
        }
        }catch(SQLException e){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos, no se puede cargar la informacion en este momento ");
        }
    }     

    private void llenarInformacionPP() {
        lblResultadoProyectp.setText(proyectoPP.getNombreProyecto());
        lblResultadoEmpresa.setText(empresa.getNombreEmpresa());
        lblResultadoResponsable.setText(responsable.getNombreResponsable() + responsable.getApellidoResponsable());
        lblResultadoPeriodo.setText(ee.getPeriodo());
        lblResultadoFecha.setText(proyectoPP.getFechaProyecto());
        lblResultadoDescripcion.setText(proyectoPP.getDescripcionProyecto());
        lblResultadoObjetivo.setText(proyectoPP.getObjetivoProyecto());
    }
    
    private void llenarInformacionSS() {
        lblResultadoProyectp.setText(proyectoSS.getNombreProyecto());
        lblResultadoEmpresa.setText(empresa.getNombreEmpresa());
        lblResultadoResponsable.setText(responsable.getNombreResponsable() + responsable.getApellidoResponsable());
        lblResultadoPeriodo.setText(ee.getPeriodo());
        lblResultadoFecha.setText(proyectoSS.getFechaProyecto());
        lblResultadoDescripcion.setText(proyectoSS.getDescripcionProyecto());
        lblResultadoObjetivo.setText(proyectoSS.getObjetivoProyecto());
    }    
}
