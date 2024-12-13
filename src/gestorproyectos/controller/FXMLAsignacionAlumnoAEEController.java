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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
	private TableColumn<Alumno, String> colNombreAlumno;
	@FXML
	private TableColumn<Alumno, String> colMatricula;

	private EE ee;
	private ObservableList<Alumno> alumnos;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		configurarTablaProyectoPP();
		configurarDobleClickEnTabla();
	}

	public void inicializarValores(EE ee) {
		this.ee = ee;
	}

	@FXML
	private void clickBuscar(ActionEvent event) {
		String filtro = txtFBuscarAlumnos.getText().trim();
		if (!filtro.isEmpty()) {
			try {
				cargarTablaProyectoPP(filtro);
			} catch (SQLException ex) {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error al buscar alumnos", "No se pudo realizar la búsqueda. Por favor, inténtelo de nuevo.");
			}
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Campo Vacío", "Por favor ingrese un criterio de búsqueda.");
		}
	}

	private void configurarTablaProyectoPP() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
		colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
	}

	private void configurarDobleClickEnTabla() {
		tblAlumnos.setRowFactory(tv -> {
			TableRow<Alumno> fila = new TableRow<>();
			fila.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !fila.isEmpty()) {
					Alumno alumnoSeleccionado = fila.getItem();
					int idAlumno = alumnoSeleccionado.getIdAlumno();
					int idEE = ee.getIdEE();
					String nombreAlumno = alumnoSeleccionado.getNombreAlumno() + " " + alumnoSeleccionado.getApellidoAlumno();

					if (manejarSeleccionDobleClick(nombreAlumno)) {
						try {
							InscripcionEEDAO.registrarInscripcionAlumnoEnEE(idAlumno, idEE);
							MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Alumno asignado", 
									"Se ha asignado al alumno a la experiencia educativa");
							cerrarPantalla();
						} catch (SQLException ex) {
							MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
									"Error",
									"Lo sentimos, no se ha podido realizar la asignación");
						}
					}
				}
			});
			return fila;
		});
	}

	private boolean manejarSeleccionDobleClick(String nombreAlumno) {
		return MisUtilidades.crearAlertaConfirmacion("¿Asignar Alumno", "Desea asignar al alumno: "
				+ nombreAlumno + " a la experiencia educativa?");
	}

	private void cargarTablaProyectoPP(String filtro) throws SQLException {
		alumnos = FXCollections.observableArrayList();
		List<Alumno> alumnosBD = AlumnoDAO.obtenerAlumnosNoInscritosEnEE(filtro);

		if (alumnosBD != null && !alumnosBD.isEmpty()) {
			alumnos.setAll(alumnosBD);
			tblAlumnos.setItems(alumnos);
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Sin Resultados", "No se encontraron alumnos con el criterio proporcionado.");
		}
	}
	
	private void cerrarPantalla() {
		Stage stage = (Stage) tblAlumnos.getScene().getWindow();
			stage.close();
	}
}
