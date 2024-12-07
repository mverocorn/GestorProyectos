package gestorproyectos.controller;

import gestorproyectos.modelo.dao.UsuarioDAO;
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
public class FXMLInicioSesionController implements Initializable {

	@FXML
	private PasswordField txtFContrasenia;
	@FXML
	private TextField txtFCorreo;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickAceptar(ActionEvent event) {
		String correo = txtFCorreo.getText().trim();
		String contrasenia = txtFContrasenia.getText().trim();
		if (validarFormulario(correo, contrasenia)) {
			
		} else {
			
		}
		
	}
	
	private boolean validarFormulario(String correo, String contrasenia) {
		return !(correo == null || correo.isEmpty() || contrasenia == null || contrasenia.isEmpty());
	}
	
	private String identificarTipoUsuario(String correo) {
		String tipoUsuario = UsuarioDAO.identificarTipoUsuario(correo);
		return tipoUsuario;
	}
	
	private void verificarCredenciales(String correo, String contrasenia) {
		
	}
}
