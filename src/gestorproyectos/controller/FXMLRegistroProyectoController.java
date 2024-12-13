package gestorproyectos.controller;

import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.dao.ResponsableDAO;
import gestorproyectos.modelo.pojo.Empresa;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.pojo.Responsable;
import gestorproyectos.utilidades.MisUtilidades;
import gestorproyectos.utilidades.Validador;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLRegistroProyectoController implements Initializable {

	@FXML
	private RadioButton rbPractica;
	@FXML
	private RadioButton rbServicio;
	@FXML
	private TextField txtFNombreProyecto;
	@FXML
	private DatePicker dpFechaInicio;
	@FXML
	private TextField txtFCupo;
	@FXML
	private ComboBox<Responsable> cBoxResponsable;
	@FXML
	private TextArea txtAObjetivo;
	@FXML
	private TextArea txtADescripcion;
	@FXML
	private ComboBox<Empresa> cBoxEmpresa;

	private ToggleGroup toggleGroup;
	private ObservableList<Empresa> empresas;
	private ObservableList<Responsable> responsables;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cargarListaResponsables();
		toggleGroup = new ToggleGroup();
		rbPractica.setToggleGroup(toggleGroup);
		rbServicio.setToggleGroup(toggleGroup);
	}

	@FXML
	private void clickRegistrar(ActionEvent event) {
		String seleccion = getRadioButtonSeleccionado();

		if ("Pŕactica Profesional".equals(seleccion)) {
			System.out.println("El usuario seleccionó Práctica Profesional.");
			registrarProyectoPP();

		} else if ("Servicio Social".equals(seleccion)) {
			System.out.println("El usuario seleccionó Servicio Social.");
			registrarProyectoSS();
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia",
					"Por favor, selecciona un tipo de proyecto.");
		}
	}

	private String getRadioButtonSeleccionado() {
		if (toggleGroup.getSelectedToggle() != null) {
			RadioButton seleccionado = (RadioButton) toggleGroup.getSelectedToggle();
			return seleccionado.getText();
		} else {
			return "No se ha seleccionado ninguna opción.";
		}
	}

	private void registrarProyectoSS() {
		try {
			String nombreProyecto = txtFNombreProyecto.getText().trim();
			int cupo = Integer.parseInt(txtFCupo.getText());
			String descripcion = txtADescripcion.getText().trim();
			String objetivo = txtAObjetivo.getText().trim();
			String fechaInicio = dpFechaInicio.getValue().toString();
			int idResponsable = cBoxResponsable.getSelectionModel().getSelectedItem().getIdResponsable();

			Validador.validarTexto(nombreProyecto, "Nombre del proyecto", 150);
			Validador.validarTexto(descripcion, "Descripción", 500);
			Validador.validarTexto(objetivo, "Objetivo", 255);
			Validador.validarCupo(cupo);
			Validador.validarResponsable(idResponsable);
			Validador.validarFechaProyecto(fechaInicio);

			ProyectoSS proyectoSS = new ProyectoSS();

			proyectoSS.setNombreProyecto(nombreProyecto);
			proyectoSS.setCupoProyecto(cupo);
			proyectoSS.setDescripcionProyecto(descripcion);
			proyectoSS.setFechaProyecto(fechaInicio);
			proyectoSS.setObjetivoProyecto(objetivo);
			proyectoSS.setIdResponsable(idResponsable);

			ProyectoSSDAO.validarProyectoSS(proyectoSS);
			ProyectoSSDAO.registrarProyectoSS(proyectoSS);
		} catch (IllegalArgumentException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Validación", ex.getMessage());
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"No se ha podido registrar el proyecto");
		}
	}

	private void registrarProyectoPP() {
		try {
			String nombreProyecto = txtFNombreProyecto.getText().trim();
			int cupo = Integer.parseInt(txtFCupo.getText());
			String descripcion = txtADescripcion.getText().trim();
			String objetivo = txtAObjetivo.getText().trim();
			String fechaInicio = dpFechaInicio.getValue().toString();
			int idResponsable = cBoxResponsable.getSelectionModel().getSelectedItem().getIdResponsable();

			Validador.validarTexto(nombreProyecto, "Nombre del proyecto", 150);
			Validador.validarTexto(descripcion, "Descripción", 500);
			Validador.validarTexto(objetivo, "Objetivo", 255);
			Validador.validarCupo(cupo);
			Validador.validarResponsable(idResponsable);
			Validador.validarFechaProyecto(fechaInicio);

			ProyectoPP proyectoPP = new ProyectoPP();

			proyectoPP.setNombreProyecto(nombreProyecto);
			proyectoPP.setCupoProyecto(cupo);
			proyectoPP.setDescripcionProyecto(descripcion);
			proyectoPP.setFechaProyecto(fechaInicio);
			proyectoPP.setObjetivoProyecto(objetivo);
			proyectoPP.setIdResponsable(idResponsable);

			ProyectoPPDAO.validarProyectoPP(proyectoPP);
			ProyectoPPDAO.registrarProyectoPP(proyectoPP);
		} catch (IllegalArgumentException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Validación", ex.getMessage());
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"No se ha podido registrar el proyecto");
		}
	}

	private void cargarListaResponsables() {
		try {
			responsables = FXCollections.observableArrayList();
			List<Responsable> responsablesBD = ResponsableDAO.obtenerResponsables();
			if (responsablesBD != null && responsablesBD.size() > 0) {
				responsables.addAll(responsablesBD);
				cBoxResponsable.setItems(responsables);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error: Sin información",
						"Lo sentimos, no se pueden cargar los responsables o "
						+ "todavía no hay ninguno registrado en el sistema");
				cerrarFormulario();
			}
		} catch (SQLException ex) {

		}
	}

	private void cerrarFormulario() {
		Stage escenarioBase = (Stage) cBoxEmpresa.getScene().getWindow();
		escenarioBase.close();
	}
}
