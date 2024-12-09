package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLCoordinadorController implements Initializable {

	@FXML
	private TableView<?> tblAlumno;
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
	private ComboBox<?> cBoxFiltroAlumno;
	@FXML
	private Tab tabProyectos;
	@FXML
	private Tab tabListado;
	@FXML
	private TextField txtFBuscarProyecto;
	@FXML
	private TableView<?> tblProyecto;
	@FXML
	private TableColumn<?, ?> colNombreProyecto;
	@FXML
	private TableColumn<?, ?> colEEProyecto;
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

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickCerrarSesion(ActionEvent event) {
	}

	@FXML
	private void clickRegistrarAlumno(ActionEvent event) {
	}

	@FXML
	private void clickBuscarAlumno(ActionEvent event) {
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
	
}
