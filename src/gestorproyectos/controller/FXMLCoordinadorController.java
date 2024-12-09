package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
	private TextField txtFBuscar;
	@FXML
	private TableView<?> tblAlumno;
	@FXML
	private TableColumn<?, ?> colNombreAlumno;
	@FXML
	private TableColumn<?, ?> colEE;
	@FXML
	private TableColumn<?, ?> colProyecto;
	@FXML
	private ComboBox<?> cBoxFiltro;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickRegistrar(ActionEvent event) {
	}

	@FXML
	private void clickBuscar(ActionEvent event) {
	}
	
}
