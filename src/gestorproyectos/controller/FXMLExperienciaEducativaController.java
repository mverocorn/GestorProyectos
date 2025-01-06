package gestorproyectos.controller;

import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
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

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLExperienciaEducativaController implements Initializable {

	@FXML
	private TableView<Alumno> tblAlumnosDeExperiencia;
	@FXML
	private TableColumn<?, ?> colNombreAlumno;
	@FXML
	private TableColumn<?, ?> colMatricula;
	@FXML
	private Label txtFResultadoNombre;
	@FXML
	private Label txtFResultadoNRC;
	@FXML
	private Label txtFResultadoSeccion;
	@FXML
	private Label txtFResultadoPeriodo;
	@FXML
	private Label txtFResultadoProfesor;

	private EE ee;

	private ObservableList<Alumno> alumnos;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	public void inicializarValores(EE ee) {
		this.ee = ee;

		System.out.println("Nombre Profesor: " + ee.getNombreProfesor()
				+ ", Apellido: " + ee.getApellidoProfesor());

		cargarInfo();
		configurarTablaProyectoPP();
		cargarTablaProyectoPP();
	}

	private void cargarInfo() {
		txtFResultadoNombre.setText(ee.getNombreEE());
		txtFResultadoNRC.setText(ee.getNrc() + "");
		txtFResultadoSeccion.setText(ee.getSeccion() + "");
		txtFResultadoPeriodo.setText(ee.getPeriodo());
		txtFResultadoProfesor.setText(ee.getNombreProfesor() + " "
				+ ee.getApellidoProfesor());
	}

	private void configurarTablaProyectoPP() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreCompleto"));
		colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));

		tblAlumnosDeExperiencia.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) { // Si se hace doble clic en un alumno
				Alumno seleccionado = tblAlumnosDeExperiencia.getSelectionModel().getSelectedItem();
				if (seleccionado != null) {
					abrirDetallesAlumno(seleccionado); // Abrir los detalles del alumno
				}
			}
		});
	}

	private void abrirDetallesAlumno(Alumno alumnoSeleccionado) {
		try {
			if (alumnoSeleccionado == null) {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un alumno.");
				return;
			}

			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLDetallesAlumno.fxml"));
			Parent vista = loader.load();

			FXMLDetallesAlumnoController controladorDetalle = loader.getController();
			controladorDetalle.inicializarValores(alumnoSeleccionado);

			Stage nuevoEscenario = new Stage();
			nuevoEscenario.setScene(new Scene(vista));
			nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
			nuevoEscenario.setTitle("Detalle del Alumno");
			nuevoEscenario.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"No se pudo cargar la ventana de detalles del alumno.");
		}
	}

	private void cargarTablaProyectoPP() {
		alumnos = FXCollections.observableArrayList();
		try {
			List<Alumno> AlumnosBD = InscripcionEEDAO.obtenerAlumnosAsignados(ee.getIdEE());
			if (AlumnosBD != null) {
				alumnos.addAll(AlumnosBD);
				tblAlumnosDeExperiencia.setItems(alumnos);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de alumnos");
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
		}
	}

	@FXML
	private void clickAsignarAlumno(ActionEvent event) {
		abrirAsignarAlumnoEE(ee);
	}

	private void abrirAsignarAlumnoEE(EE eeSeleccionada) {
		try {
			Stage escenarioActual = (Stage) txtFResultadoNRC.getScene().getWindow();

			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLAsignacionAlumnoAEE.fxml"));
			Parent vista = loader.load();
			FXMLAsignacionAlumnoAEEController controladorDetalle = loader.getController();
			controladorDetalle.inicializarValores(eeSeleccionada);

			Scene nuevaEscena = new Scene(vista);

			escenarioActual.setScene(nuevaEscena);
			escenarioActual.setTitle("Asignación de Alumno a EE");
			escenarioActual.show();
		} catch (IOException ex) {
			ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se pudo cargar la ventana de Asignación a EE");
		}
	}

}
