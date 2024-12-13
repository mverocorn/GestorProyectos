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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLAsignacionAlumnoAEEController implements Initializable {

	@FXML
	private TextField txtFBuscarAlumnos;
	@FXML
	private TableView<Alumno> tblAlumnos;
	@FXML
	private TableColumn<?, ?> colNombreAlumno;
	@FXML
	private TableColumn<?, ?> colMatricula;
	
	private EE ee;
	private ObservableList<Alumno> alumnos;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	public void inicializarValores(EE ee) {
		this.ee = ee;
	}

	@FXML
	private void clickBuscar(ActionEvent event) {
	}
	
	private void configurarTablaProyectoPP() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
	}

	private void cargarTablaProyectoPP() throws SQLException {
		alumnos = FXCollections.observableArrayList();
		List<Alumno> AlumnosBD = AlumnoDAO.obtenerAlumnosNoInscritosEnEE();
		if (AlumnosBD != null) {
			alumnos.addAll(AlumnosBD);
			tblAlumnos.setItems(alumnos);
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
					"Error", "Lo sentimos, no se puede cargar en "
							+ "este momento la tabla de alumnos");
		}
	}
	
}
