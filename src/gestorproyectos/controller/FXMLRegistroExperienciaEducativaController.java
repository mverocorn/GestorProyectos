package gestorproyectos.controller;

import gestorproyectos.modelo.dao.ProfesorDAO;
import gestorproyectos.modelo.pojo.Profesor;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLRegistroExperienciaEducativaController implements Initializable {

	@FXML
	private TextField txtFNombreEE;
	@FXML
	private TextField txtFNRC;
	@FXML
	private TextField txtFSeccion;
	@FXML
	private TextField txtFPeriodo;
	
	private ObservableList<Profesor> profesores;
	@FXML
	private ComboBox<Profesor> cBoxProfesor;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cargarListaProfesores();
		// TODO
	}	

	@FXML
	private void clickCancelar(ActionEvent event) {
	}

	@FXML
	private void clickRegistrar(ActionEvent event) {
	}
	
	private void cargarListaProfesores() {
		try {
			profesores = FXCollections.observableArrayList();
			List<Profesor> profesoresBD = ProfesorDAO.obtenerProfesores();
			if(profesoresBD != null && !profesoresBD.isEmpty()) {
				profesores.addAll(profesoresBD);
				cBoxProfesor.setItems(profesores);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error: Sin información",
						"Lo sentimos, no se pueden cargar los entrenadores o "
								+ "todavía no hay ninguno registrado en el sistema");
			}
		} catch (SQLException ex) {
		}
	}
	
	
	
}
