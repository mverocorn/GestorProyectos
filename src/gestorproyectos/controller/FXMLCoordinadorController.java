package gestorproyectos.controller;

import gestorproyectos.interfaces.IObservador;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.dao.EEDAO;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.pojo.InscripcionEE;
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
	private ComboBox<String> cBoxFiltroProyecto;
	@FXML
	private Tab tabAsignacion;
	@FXML
	private TableView<InscripcionEE> tblAlumnoAsignacionSS;
	@FXML
	private TableColumn<?, ?> colNombreAlumnoAsignacionSS;
	@FXML
	private TableColumn<?, ?> colPromedioAsignacionSS;
	@FXML
	private TableColumn<?, ?> colEEAsignacionSS;
	@FXML
	private TableColumn<?, ?> colSeccionAsignacionSS;
	@FXML
	private TableView<InscripcionEE> tblPriorizacionSS;
	@FXML
	private TableColumn<?, ?> colNombreProyectoPriorizacionSS;
	@FXML
	private TableColumn<?, ?> colCupoPriorizacionSS;
	@FXML
	private TableColumn<?, ?> colOrdenPriorizacionSS;
	@FXML
	private TableView<InscripcionEE> tblAlumnoAsignacionPP;
	@FXML
	private TableColumn<?, ?> colNombreAlumnoAsignacionPP;
	@FXML
	private TableColumn<?, ?> colPromedioAsignacionPP;
	@FXML
	private TableColumn<?, ?> colEEAsignacionPP;
	@FXML
	private TableColumn<?, ?> colSeccionAsignacionPP;
	@FXML
	private TableView<?> tblPriorizacionPP;
	@FXML
	private TableColumn<?, ?> colNombreProyectoPriorizacionPP;
	@FXML
	private TableColumn<?, ?> colCupoPriorizacionPP;
	@FXML
	private TableColumn<?, ?> colOrdenPriorizacionPP;
	@FXML
	private ComboBox<String> cBoxFiltroAsignarProyecto;
	@FXML
	private Tab tabEE;
	@FXML
	private TableView<EE> tblEE;
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
	@FXML
	private TableView<ProyectoSS> tblProyectoSS;
	@FXML
	private TableColumn<?, ?> colNombreProyectoSS;
	@FXML
	private TableColumn<ProyectoSS, String> colEEProyectoSS;
	@FXML
	private TableView<ProyectoPP> tblProyectoPP;
	@FXML
	private TableColumn<?, ?> colNombreProyectoPP;
	@FXML
	private TableColumn<ProyectoPP, String> colEEProyectoPP;
	private TableColumn<?, ?> colSeccionAsignacion;

	private ObservableList<Alumno> alumnos;
	private ObservableList<ProyectoSS> proyectosSS;
	private ObservableList<ProyectoPP> proyectosPP;
	private ObservableList<EE> experiencias;
	private ObservableList<InscripcionEE> inscripcionesSS;
	private ObservableList<InscripcionEE> inscripcionesPP;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cBoxFiltroAlumno.getItems().addAll("Servicio Social", "Práctica Profesional", "Todos");
		cBoxFiltroAlumno.setValue("Todos");
		cBoxFiltroAlumno.setOnAction(event -> {
			configurarTablaAlumno();
			cargarTablaAlumno();
		});

		cBoxFiltroProyecto.getItems().addAll("Práctica Profesional", "Servicio Social");
		cBoxFiltroProyecto.setOnAction(event -> {
			filtrarTipoProyectos();
		});

		cBoxFiltroAsignarProyecto.getItems().addAll("Práctica Profesional", "Servicio Social");
		cBoxFiltroAsignarProyecto.setOnAction(event -> {
			filtrarTablasAsignacion();
		});
		configurarTablaEE();
		cargarTablaEE();
	}

	private void configurarTablaAlumno() {
		colNombreAlumno.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colEEAlumno.setCellValueFactory(new PropertyValueFactory("nombreEE"));
		colProyectoAlumno.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));

	}

	private void cargarTablaAlumno() {
		String nombreBusqueda = txtFBuscarAlumno.getText().trim();
		String tipoProyecto = (String) cBoxFiltroAlumno.getValue();

		alumnos = FXCollections.observableArrayList();

		try {
			if ("Todos".equals(tipoProyecto)) {
				tipoProyecto = null;
			}

			List<Alumno> alumnosBD = AlumnoDAO.obtenerAlumnoYProyecto(nombreBusqueda, tipoProyecto);

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
		try {
			Stage stage = (Stage) txtFBuscarAlumno.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLInicioSesion.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Inicio Sesion");
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

	private void filtrarTipoProyectos() {
		String tipoProyecto = cBoxFiltroProyecto.getValue().toString();
		if (tipoProyecto.equals("Servicio Social")) {
			tblProyectoPP.setVisible(false);
			tblProyectoSS.setVisible(true);
			configurarTablaProyectoSS();
			cargarTablaProyectoSS();
		} else if (tipoProyecto.equals("Práctica Profesional")) {
			tblProyectoSS.setVisible(false);
			tblProyectoPP.setVisible(true);
			configurarTablaProyectoPP();
			cargarTablaProyectoPP();
		}
	}

	private void configurarTablaProyectoSS() {
		colNombreProyectoSS.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));
		colEEProyectoSS.setCellValueFactory(celda -> new SimpleStringProperty("Servicio Social"));
	}

	private void cargarTablaProyectoSS() {
		proyectosSS = FXCollections.observableArrayList();
		try {
			List<ProyectoSS> proyectoSSBD = ProyectoSSDAO.obtenerProyectosSS();
			if (proyectoSSBD != null) {
				proyectosSS.addAll(proyectoSSBD);
				tblProyectoSS.setItems(proyectosSS);
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
		colNombreProyectoPP.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));
		colEEProyectoPP.setCellValueFactory(celda -> new SimpleStringProperty("Práctica Profesional"));
	}

	private void cargarTablaProyectoPP() {
		proyectosPP = FXCollections.observableArrayList();
		try {
			List<ProyectoPP> proyectoPPBD = ProyectoPPDAO.obtenerProyectosPP();
			if (proyectoPPBD != null) {
				proyectosPP.addAll(proyectoPPBD);
				tblProyectoPP.setItems(proyectosPP);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de proyectos");
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
		irFormularioProyecto();
	}

	@FXML
	private void clickAsignar(ActionEvent event) {
	}

	private void configurarTablaEE() {
		colNombreEE.setCellValueFactory(new PropertyValueFactory("nombreEE"));
		colPeriodoEE.setCellValueFactory(new PropertyValueFactory("periodo"));
		colBloqueEE.setCellValueFactory(new PropertyValueFactory("seccion"));
	}

	private void cargarTablaEE() {
		experiencias = FXCollections.observableArrayList();
		try {
			List<EE> experienciasBD = EEDAO.obtenerEE();
			if (experienciasBD != null) {
				experiencias.addAll(experienciasBD);
				tblEE.setItems(experiencias);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de Experiencias educativas");
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
		}
	}

	@FXML
	private void clickBuscarEE(ActionEvent event) {
	}

	@FXML
	private void clickRegistrarEE(ActionEvent event) {
		irFormularioEE();
	}

	private void filtrarTablasAsignacion() {
		String tipoProyecto = cBoxFiltroAsignarProyecto.getValue().toString();
		if (tipoProyecto.equals("Servicio Social")) {
			tblAlumnoAsignacionPP.setVisible(false);
			tblAlumnoAsignacionSS.setVisible(true);
			configurarTablaAlumnoAsignacionSS();
			cargarTablaAlumnoAsignacionSS();
		} else if (tipoProyecto.equals("Práctica Profesional")) {
			tblAlumnoAsignacionSS.setVisible(false);
			tblAlumnoAsignacionPP.setVisible(true);
			configurarTablaAlumnoAsignacionPP();
			cargarTablaAlumnoAsignacionPP();
		}
	}

	private void configurarTablaAlumnoAsignacionSS() {
		colNombreAlumnoAsignacionSS.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colPromedioAsignacionSS.setCellValueFactory(new PropertyValueFactory("promedio"));
		colEEAsignacionSS.setCellValueFactory(new PropertyValueFactory("nombreEE"));
		colSeccionAsignacionSS.setCellValueFactory(new PropertyValueFactory("seccion"));
	}

	private void cargarTablaAlumnoAsignacionSS() {
		inscripcionesSS = FXCollections.observableArrayList();
		try {
			List<InscripcionEE> inscripcionesBD = InscripcionEEDAO.obtenerAlumnosSSParaAsignarProyectoSS();
			if (inscripcionesBD != null) {
				inscripcionesSS.addAll(inscripcionesBD);
				tblAlumnoAsignacionSS.setItems(inscripcionesSS);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de Inscripciones");
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
		}
	}

	private void configurarTablaAlumnoAsignacionPP() {
		colNombreAlumnoAsignacionPP.setCellValueFactory(new PropertyValueFactory("nombreAlumno"));
		colPromedioAsignacionPP.setCellValueFactory(new PropertyValueFactory("promedio"));
		colEEAsignacionPP.setCellValueFactory(new PropertyValueFactory("nombreEE"));
		colSeccionAsignacionPP.setCellValueFactory(new PropertyValueFactory("seccion"));
	}

	private void cargarTablaAlumnoAsignacionPP() {
		inscripcionesPP = FXCollections.observableArrayList();
		try {
			List<InscripcionEE> inscripcionesBD = InscripcionEEDAO.obtenerAlumnosPPParaAsignarProyectoPP();
			if (inscripcionesBD != null) {
				inscripcionesPP.addAll(inscripcionesBD);
				tblAlumnoAsignacionPP.setItems(inscripcionesPP);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
						"Error", "Lo sentimos, no se puede cargar en "
						+ "este momento la tabla de Inscripciones");
			}
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, el sistema presenta fallas para recuperar la información, "
					+ "vuelva a intentar");
		}
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

	private void irFormularioEE() {
		try {
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLRegistroExperienciaEducativa.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Registro de Experiencias educativas");
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void irFormularioProyecto() {
		try {
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLRegistroProyecto.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Registro de Proyectos");
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
