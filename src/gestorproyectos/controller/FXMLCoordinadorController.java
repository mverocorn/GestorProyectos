package gestorproyectos.controller;

import gestorproyectos.interfaces.IObservador;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLCoordinadorController implements Initializable, IObservador {

	@FXML
	private TableView<Alumno> tblAlumno;
	@FXML
	private TableColumn<?, ?> colNombreAlumno;
	@FXML
	private Tab tabAlumnos;
	@FXML
	private TextField txtFBuscarAlumno;
	@FXML
	private TableColumn<?, ?> colEEAlumno;
	@FXML
	private TableColumn<?, ?> colProyectoAlumno;
	@FXML
	private ComboBox<String> cBoxFiltroAlumno;
	@FXML
	private Tab tabProyectos;
	@FXML
	private Tab tabListado;
	@FXML
	private TextField txtFBuscarProyecto;
	@FXML
	private TableView<ProyectoSS> tblProyecto;
	@FXML
	private TableColumn<?, ?> colNombreProyecto;
	@FXML
	private TableColumn<ProyectoSS, String> colEEProyecto;
	@FXML
	private ComboBox<?> cBoxFiltroProyecto;
	@FXML
	private Tab tabAsignacion;
	@FXML
	private TableView<?> tblAlumnoAsignacion;
	@FXML
	private TableColumn<?, ?> colNombreAlumnoAsignacion;
	@FXML
	private TableColumn<?, ?> colPromedioAsignacion;
	@FXML
	private TableColumn<?, ?> colEEAsignacion;
	@FXML
	private TableColumn<?, ?> colBloqueAsignacion;
	@FXML
	private ComboBox<?> cBoxFiltroAsignarProyecto;
	@FXML
	private TableView<?> tblPriorizacion;
	@FXML
	private TableColumn<?, ?> colNombreProyectoPriorizacion;
	@FXML
	private TableColumn<?, ?> colCupoPriorizacion;
	@FXML
	private TableColumn<?, ?> colOrdenPriorizacion;
	@FXML
	private Tab tabEE;
	@FXML
	private TableView<?> tblEE;
	@FXML
	private TableColumn<?, ?> colNombreEE;
	@FXML
	private TableColumn<?, ?> colPeriodoEE;
	@FXML
	private TableColumn<?, ?> colBloqueEE;
	@FXML
	private TextField txtFBuscarEE;
	@FXML
	private ComboBox<?> cBoxFiltroEE;

	private ObservableList<Alumno> alumnos;
	private ObservableList<ProyectoSS> proyectosSS;
	private ObservableList<ProyectoPP> proyectosPP;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cBoxFiltroAlumno.getItems().addAll("Servicio Social", "Práctica Profesional");
		configurarTablaProyectoSS();
		cargarTablaProyectoSS();
	}

	private void configurarTablaAlumno() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colEEAlumno.setCellValueFactory(new PropertyValueFactory("nombreEE"));
		colProyectoAlumno.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));

	}

	private void cargarTablaAlumno() {
		String nombreBusqueda = txtFBuscarAlumno.getText().trim();
		String tipoProyecto = (String) cBoxFiltroAlumno.getValue(); // Obtienes el valor seleccionado en el ComboBox

		alumnos = FXCollections.observableArrayList();

		try {
			List<Alumno> alumnosBD = AlumnoDAO.obtenerAlumnoYProyecto(nombreBusqueda, tipoProyecto);
			for (Alumno alumno : alumnos) {
				System.out.println("Alumno: " + alumno.getNombreAlumno()
						+ ", EE: " + alumno.getNombreEE()
						+ ", Proyecto: " + alumno.getNombreProyecto());
			}

			if (alumnosBD != null && !alumnosBD.isEmpty()) {
				alumnos.addAll(alumnosBD);
				tblAlumno.setItems(alumnos);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,
						"Sin resultados", "No se encontraron alumnos con ese criterio de búsqueda.");
				tblAlumno.setItems(null);
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información. Inténtelo de nuevo más tarde.");
			ex.printStackTrace();
		}
	}

	@FXML
	private void clickCerrarSesion(ActionEvent event) {
	}

	@FXML
	private void clickRegistrarAlumno(ActionEvent event) {
		irFormularioAlumno();
	}

	@FXML
	private void clickBuscarAlumno(ActionEvent event) {
		configurarTablaAlumno();
		cargarTablaAlumno();
	}

	private void configurarTablaProyectoSS() {
		colNombreProyecto.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));
		colEEProyecto.setCellValueFactory(celda -> new SimpleStringProperty("Servicio Social"));
	}

	private void cargarTablaProyectoSS() {
		proyectosSS = FXCollections.observableArrayList();
		try {
			List<ProyectoSS> proyectoSSBD = ProyectoSSDAO.obtenerProyectosSS();
			if (proyectoSSBD != null) {
				proyectosSS.addAll(proyectoSSBD);
				tblProyecto.setItems(proyectosSS);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de proyectosSS");
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
		}
	}

	private void configurarTablaProyectoPP() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colEEAlumno.setCellValueFactory(new PropertyValueFactory("ee"));
		colProyectoAlumno.setCellValueFactory(new PropertyValueFactory(""));

	}

	private void cargarTablaProyectoPP() {
		alumnos = FXCollections.observableArrayList();
		try {
			List<Alumno> alumnosBD = AlumnoDAO.obtenerAlumnos();
			if (alumnosBD != null) {
				alumnos.addAll(alumnosBD);
				tblAlumno.setItems(alumnos);
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
	private void clickBuscarProyecto(ActionEvent event) {
	}

	@FXML
	private void clickRegistrarProyecto(ActionEvent event) {
	}

	@FXML
	private void clickAsignar(ActionEvent event) {
	}

	@FXML
	private void clickBuscarEE(ActionEvent event) {
	}

	@FXML
	private void clickRegistrarEE(ActionEvent event) {
	}

	private void irFormularioAlumno() {
		try {
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLRegistroAlumno.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Registro de clientes");
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void notificarAlumnoGuardado(String nombreAlumno, String operacion) {

	}

}
