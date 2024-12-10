package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLExperienciaEducativaController implements Initializable {

	@FXML
	private TableView<?> tblAlumnosDeExperiencia;
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

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickAsignarAlumno(ActionEvent event) {
	}
	
}
