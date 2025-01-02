package gestorproyectos.controller;

import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.dao.EEDAO;
import gestorproyectos.modelo.dao.EmpresaDAO;
import gestorproyectos.modelo.dao.ExpedienteDAO;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.dao.ResponsableDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.Empresa;
import gestorproyectos.modelo.pojo.Expediente;
import gestorproyectos.modelo.pojo.InscripcionEE;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.pojo.Responsable;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class FXMLDetallesAlumnoController implements Initializable {

    @FXML
    private Label lblResultadoAlumno;
    @FXML
    private Label lblResultadoMatricula;
    @FXML
    private Label lblResultadoCorreo;
    @FXML
    private Label lblResultadoTelefono;
    @FXML
    private Label lblResultadoPromedio;
    @FXML
    private Label lblResultadoProyectoEnCurso;
    @FXML
    private TableView<InscripcionEE> tblProyectos;

    @FXML
    private TableColumn<?, ?> colNombreProyecto;
    @FXML
    private TableColumn<?, ?> colExperienciaEducativa;
    @FXML
    private TableColumn<?, ?> colEstado;

    private Alumno alumno;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablaProyectos();  // Asegúrate de configurar la tabla en el método initialize
    }

    public void inicializarValores(Alumno alumnoSeleccionado) {
        this.alumno = alumnoSeleccionado; // Asignar el alumno recibido
        cargarInfo(); // Llamar al método para mostrar la información
        cargarTablaProyectoDeAlumno(); // Cargar los proyectos del alumno
    }

    private void cargarInfo() {
        // Verificar si 'alumno' no es null
        if (alumno != null) {
            // Obtener los detalles completos del alumno desde la base de datos
            alumno = AlumnoDAO.obtenerAlumnoPorId(alumno.getIdAlumno());
            
            // Mostrar la información básica del alumno
            lblResultadoAlumno.setText(alumno.getNombreAlumno() + " "
                + alumno.getApellidoAlumno());
            lblResultadoMatricula.setText(alumno.getMatricula() != null ? alumno.getMatricula() : "No disponible");
            lblResultadoCorreo.setText(alumno.getCorreo() != null ? alumno.getCorreo() : "No disponible");
            lblResultadoTelefono.setText(alumno.getTelefonoAlumno() != null ? alumno.getTelefonoAlumno() : "No disponible");
            
            // Mostrar el promedio con un formato adecuado
            Float promedio = alumno.getPromedio();
            lblResultadoPromedio.setText(promedio != null ? String.format("%.2f", promedio) : "Promedio no disponible");
            
            // Obtener y mostrar los proyectos activos (deberás agregar esa parte)
        } else {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "El alumno no está disponible.");
        }
    }

    private void configurarTablaProyectos() {
        colNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
        colExperienciaEducativa.setCellValueFactory(new PropertyValueFactory<>("nombreEE"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoInscripcion"));
    }

    private void cargarTablaProyectoDeAlumno() {
        ObservableList<InscripcionEE> inscripciones = FXCollections.observableArrayList(); // Lista ObservableList de InscripcionEE
        try {
            // Recupera las inscripciones del alumno
            List<InscripcionEE> inscripcionesBD = InscripcionEEDAO.obtenerDetallesInscripcionesPorAlumno(alumno.getIdAlumno());

            if (inscripcionesBD != null && !inscripcionesBD.isEmpty()) {
                inscripciones.addAll(inscripcionesBD); // Agrega las inscripciones obtenidas a la lista observable
                tblProyectos.setItems(inscripciones); // Asigna la lista observable a la tabla
            } else {
                tblProyectos.setItems(FXCollections.observableArrayList()); // Si no hay inscripciones, asigna una lista vacía
            }
        } catch (SQLException ex) {
            // Manejar la excepción y mostrar un mensaje de error si es necesario
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
                "Lo sentimos, el sistema presenta fallas para recuperar la información, vuelva a intentar");
        }
    }     
    
	private void abrirVentanaExpediente(EE ee) {
                try {
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLExpedienteAlumno.fxml"));
			Parent vista = loader.load();
			
                        InscripcionEE inscripcionEE = InscripcionEEDAO.obtenerInscripcionEE(alumno.getIdAlumno(),ee.getIdEE());
                        Expediente expediente = ExpedienteDAO.obtenerExpediente(inscripcionEE.getIdInscripcionEE());
                        if(ee.getNombreEE().equals("Servicio Social")){
                            ProyectoSS proyecto = ProyectoSSDAO.obtenerProyectoSSPorIdProyectoSS(inscripcionEE.getIdProyectoSS());
                            Responsable responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyecto.getIdResponsable());
                            Empresa empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
                            FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
                            controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyecto,false);
                        }else{
                            ProyectoPP proyecto = ProyectoPPDAO.obtenerProyectoPPPorIdProyectoPP(inscripcionEE.getIdProyectoPP());
                            Responsable responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyecto.getIdResponsable());
                            Empresa empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
                            FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
                            controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyecto,false);
                        }
                        

			Stage stage = new Stage();
			stage.setScene(new Scene(vista));
			stage.setTitle("Expediente");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de expediente.");
		} catch (SQLException ex){
                        ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de expediente 1.");
                }
	}    
        
        private EE obtenerEEporInscripcionEE (InscripcionEE inscripcionEE) throws SQLException{
            EE ee = EEDAO.obtenerEEPorInscripcion(inscripcionEE.getIdInscripcionEE());
            return ee;
            
        }
}
