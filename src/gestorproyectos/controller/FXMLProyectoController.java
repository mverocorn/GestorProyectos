package gestorproyectos.controller;

import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.dao.DocumentoDAO;
import gestorproyectos.modelo.dao.ExpedienteDAO;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.dao.ReporteDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.Documento;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.pojo.Reporte;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLProyectoController implements Initializable {

    @FXML
    private Label nombreProyecto;
    @FXML
    private TableView<Documento> tblDocumentos;
    @FXML
    private TableColumn<?, ?> colNombreDocumento;
    @FXML
    private TableView<Reporte> tblReportes;
    @FXML
    private TableColumn<?, ?> colNombreReporte;
    
    private int idProyecto;
    private int idAlumno;
    private int idExpediente;
    private ObservableList<Documento> documentos;
    private ObservableList<Reporte> reportes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Se podría hacer más inicialización si se requiere.
    }    

    @FXML
    private void clcikSubirArchivos(ActionEvent event) {
		try {
			Stage nuevoEscenario = new Stage();
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLEntregaDeArchivo.fxml"));
			Parent vista = loader.load();
			
			Scene nuevaEscena = new Scene(vista);
			nuevoEscenario.setScene(nuevaEscena);
			nuevoEscenario.setTitle("Entregar archivos");
			nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
			nuevoEscenario.showAndWait();
		} catch (IOException ex) {
			Logger.getLogger(FXMLProyectoController.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

    public void inicializarValores(int idProyecto, int idAlumno) {
        this.idProyecto = idProyecto;
        this.idAlumno = idAlumno;
        configurarTablaDocumentos();
        configurarTablaReportes();
        cargarDatos();
        cargarTablaDocumentos();
        cargarTablaReportes();
    }
    
    private void cargarDatos() {
        try {
            ProyectoSS proyectoss = ProyectoSSDAO.obtenerProyectoSSPorIdProyectoSS(idProyecto);
            if (proyectoss != null) {
                nombreProyecto.setText(proyectoss.getNombreProyecto());
            }

            idExpediente = ExpedienteDAO.obtenerIdExpedientePorAlumno(idAlumno);
        } catch (SQLException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", 
                    "No se pudo cargar la información del proyecto o expediente.");
        }
    }

    private void configurarTablaDocumentos() {
        colNombreDocumento.setCellValueFactory(new PropertyValueFactory("nombreDocumento"));
    }

    private void cargarTablaDocumentos() {
        documentos = FXCollections.observableArrayList();
        try {
            List<Documento> documentosBD = DocumentoDAO.obtenerDocumentos(idExpediente);
            if (documentosBD != null) {
                documentos.addAll(documentosBD);
                tblDocumentos.setItems(documentos);
            } else {
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", 
                        "No se pudo cargar la tabla de documentos.");
            }
        } catch (SQLException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", 
                    "El sistema presenta fallas al recuperar la información de documentos.");
        }
    }

    private void configurarTablaReportes() {
        colNombreReporte.setCellValueFactory(new PropertyValueFactory("nombreReporte"));
    }

    private void cargarTablaReportes() {
        reportes = FXCollections.observableArrayList();
        try {
            List<Reporte> reportesBD = ReporteDAO.obtenerReportes(idExpediente);
            if (reportesBD != null) {
                reportes.addAll(reportesBD);
                tblReportes.setItems(reportes);
            } else {
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", 
                        "No se pudo cargar la tabla de reportes.");
            }
        } catch (SQLException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", 
                    "El sistema presenta fallas al recuperar la información de reportes.");
        }
    }
}
