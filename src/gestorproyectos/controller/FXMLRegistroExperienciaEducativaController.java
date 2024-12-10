package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickCancelar(ActionEvent event) {
	}

	@FXML
	private void clickRegistrar(ActionEvent event) {
	}
	
}
