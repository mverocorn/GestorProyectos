package gestorproyectos.controller;

import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.utilidades.MisUtilidades;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
		cargarInfo();
		configurarTablaProyectoPP();
		cargarTablaProyectoPP();
	}

	private void cargarInfo() {
		txtFResultadoNombre.setText(ee.getNombreEE());
		txtFResultadoNRC.setText(ee.getNrc() + "");
		txtFResultadoSeccion.setText(ee.getSeccion() + "");
		txtFResultadoPeriodo.setText(ee.getPeriodo());
		txtFResultadoProfesor.setText(ee.getNombreProfesor() + " " + ee.getApellidoProfesor());
	}

	private void configurarTablaProyectoPP() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
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
	}

}
