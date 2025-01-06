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
		configurarTablaProyectos();
	}

	public void inicializarValores(Alumno alumnoSeleccionado) throws SQLException {
		this.alumno = alumnoSeleccionado;
		cargarInfo();
		cargarTablaProyectoDeAlumno();
	}

	private void cargarInfo() throws SQLException {
		if (alumno != null) {
			Alumno alumnoCompleto = AlumnoDAO.obtenerAlumnoPorId(alumno.getIdAlumno());
			int idProyecto = AlumnoDAO.obtenerIdProyectoPorIdAlumno(alumno.getIdAlumno());
			if (alumnoCompleto != null) {
				alumno = alumnoCompleto;
				lblResultadoAlumno.setText(alumno.getNombreAlumno() + " " + alumno.getApellidoAlumno());
				lblResultadoMatricula.setText(alumno.getMatricula() != null ? alumno.getMatricula() : "No disponible");
				lblResultadoCorreo.setText(alumno.getCorreo() != null ? alumno.getCorreo() : "No disponible");
				lblResultadoTelefono.setText(alumno.getTelefonoAlumno() != null ? alumno.getTelefonoAlumno() : "No disponible");
				lblResultadoPromedio.setText(alumno.getPromedio() != null ? String.format("%.2f", alumno.getPromedio()) : "No disponible");
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo recuperar la información del alumno.");
			}
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "El alumno no está disponible.");
		}
	}

	private void cargarTablaProyectoDeAlumno() {
		if (alumno == null || alumno.getIdAlumno() == 0) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "No se puede cargar la tabla sin un alumno válido.");
			return;
		}

		ObservableList<InscripcionEE> inscripciones = FXCollections.observableArrayList();
		try {
			List<InscripcionEE> inscripcionesBD = InscripcionEEDAO.obtenerDetallesInscripcionesPorAlumno(alumno.getIdAlumno());
			if (inscripcionesBD != null && !inscripcionesBD.isEmpty()) {
				inscripciones.addAll(inscripcionesBD);
				tblProyectos.setItems(inscripciones);
			} else {
				tblProyectos.setItems(FXCollections.observableArrayList());
				MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Información", "El alumno no tiene inscripciones registradas.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cargar la tabla de proyectos.");
		}
	}

	private void configurarTablaProyectos() {
		colNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));
		colExperienciaEducativa.setCellValueFactory(new PropertyValueFactory<>("nombreEE"));
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoInscripcion"));

		tblProyectos.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				InscripcionEE seleccionada = tblProyectos.getSelectionModel().getSelectedItem();
				if (seleccionada != null) {
					try {
						EE ee = obtenerEEporInscripcionEE(seleccionada);
						abrirVentanaExpediente(ee);
					} catch (SQLException ex) {
						ex.printStackTrace();
						MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
								"No se pudo cargar la información de la inscripción seleccionada.");
					}
				}
			}
		});
	}

	private void abrirVentanaExpediente(EE ee) {
		try {

			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLExpedienteAlumno.fxml"));
			Parent vista = loader.load();

			InscripcionEE inscripcionEE = InscripcionEEDAO.obtenerInscripcionEE(alumno.getIdAlumno(), ee.getIdEE());
			if (inscripcionEE == null) {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Inscripción no encontrada", "No se encontró la inscripción de la EE para el alumno.");
				return;
			}

			Expediente expediente = ExpedienteDAO.obtenerExpediente(inscripcionEE.getIdInscripcionEE());
			if (expediente == null) {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Expediente no encontrado", "No se encontró el expediente relacionado con la inscripción.");
				return;
			}

			Empresa empresa = null;
			Responsable responsable = null;

			if (ee.getNombreEE().equals("Servicio Social")) {
				ProyectoSS proyectoSS = ProyectoSSDAO.obtenerProyectoSSPorIdProyectoSS(inscripcionEE.getIdProyectoSS());
				if (proyectoSS == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Proyecto de Servicio Social no encontrado", "No se encontró el proyecto de Servicio Social relacionado.");
					return;
				}
				responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyectoSS.getIdResponsable());
				if (responsable == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Responsable no encontrado", "No se encontró el responsable del proyecto de Servicio Social.");
					return;
				}
				empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
				if (empresa == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Empresa no encontrada", "No se encontró la empresa asociada al proyecto de Servicio Social.");
					return;
				}

				FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
				controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyectoSS, false);

			} else {
				ProyectoPP proyectoPP = ProyectoPPDAO.obtenerProyectoPPPorIdProyectoPP(inscripcionEE.getIdProyectoPP());
				if (proyectoPP == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Proyecto profesional no encontrado", "No se encontró el proyecto profesional relacionado.");
					return;
				}
				responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyectoPP.getIdResponsable());
				if (responsable == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Responsable no encontrado", "No se encontró el responsable del proyecto profesional.");
					return;
				}
				empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
				if (empresa == null) {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Empresa no encontrada", "No se encontró la empresa asociada al proyecto profesional.");
					return;
				}

				FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
				controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyectoPP, false);
			}

			Stage stage = new Stage();
			stage.setScene(new Scene(vista));
			stage.setTitle("Expediente");
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de carga", "No se pudo cargar la ventana de expediente.");
		} catch (SQLException e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error en la base de datos", "Ocurrió un problema al acceder a la base de datos: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error inesperado", "Ha ocurrido un error inesperado: " + e.getMessage());
		}
	}

	private EE obtenerEEporInscripcionEE(InscripcionEE inscripcionEE) throws SQLException {
		EE ee = EEDAO.obtenerEEPorInscripcion(inscripcionEE.getIdInscripcionEE());
		return ee;

	}
}
