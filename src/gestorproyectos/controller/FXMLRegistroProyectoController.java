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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
	private TextField txtFNombreProyecto;
	@FXML
	private TextField txtFCupo;
	@FXML
	private ComboBox<Responsable> cBoxResponsable;
	@FXML
	private TextArea txtAObjetivo;
	@FXML
	private TextArea txtADescripcion;
	@FXML
	private TextField txtFPeriodo;

	private ToggleGroup toggleGroup;
	private ObservableList<Empresa> empresas;
	private ObservableList<Responsable> responsables;
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cargarListaResponsables();
	}

	@FXML
	private void clickRegistrar(ActionEvent event) {
		registrarProyectoSS();
	}

	private void registrarProyectoSS() {
		try {
			String nombreProyecto = txtFNombreProyecto.getText().trim();
			int cupo = Integer.parseInt(txtFCupo.getText());
			String descripcion = txtADescripcion.getText().trim();
			String objetivo = txtAObjetivo.getText().trim();
			String periodo = txtFPeriodo.getText().trim();

			Validador.validarTexto(nombreProyecto, "Nombre del proyecto", 150);
			Validador.validarTexto(descripcion, "Descripción", 500);
			Validador.validarTexto(objetivo, "Objetivo", 255);
			Validador.validarPeriodo(periodo);

			if (cBoxResponsable.getSelectionModel().getSelectedItem() == null) {
				throw new IllegalArgumentException("Debe seleccionar un responsable.");
			}

			int idResponsable = cBoxResponsable.getSelectionModel().getSelectedItem().getIdResponsable();

			Validador.validarResponsable(idResponsable);
			
			ProyectoSS proyectoSS = new ProyectoSS();
			proyectoSS.setNombreProyecto(nombreProyecto);
			proyectoSS.setCupoProyecto(cupo);
			proyectoSS.setDescripcionProyecto(descripcion);
			proyectoSS.setFechaProyecto(periodo);
			proyectoSS.setObjetivoProyecto(objetivo);
			proyectoSS.setIdResponsable(idResponsable);

			ProyectoSSDAO.validarProyectoSS(proyectoSS);
			ProyectoSSDAO.registrarProyectoSS(proyectoSS);
			MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Se ha registrado correctamente");
			cerrarFormulario();

		} catch (IllegalArgumentException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Validación", ex.getMessage());
		} catch (SQLException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error en la base de datos",
					"No se ha podido registrar el proyecto. Error: " + ex.getMessage());
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
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de Base de Datos",
					"Hubo un problema al cargar los responsables: " + ex.getMessage());
		}
	}

	private void cerrarFormulario() {
		Stage escenarioBase = (Stage) cBoxResponsable.getScene().getWindow();
		escenarioBase.close();
	}
}
