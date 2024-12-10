package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLRegistroAlumnoController implements Initializable {

	@FXML
	private TextField txtFNombreAlumno;
	@FXML
	private TextField txtFApellidoPaterno;
	@FXML
	private TextField txtFApellidoMaterno;
	@FXML
	private TextField txtFMatricula;
	@FXML
	private TextField txtFCorreo;
	@FXML
	private TextField txtFTelefono;
	@FXML
	private TextField txtFPromedio;
	@FXML
	private PasswordField txtFContrasenia;

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
	private void clickCancelar(ActionEvent event) {
	}
	
}
