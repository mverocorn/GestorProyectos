package gestorproyectos.controller;

import gestorproyectos.modelo.dao.UsuarioDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.Profesor;
import gestorproyectos.utilidades.MisUtilidades;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
	String tipoUsuario;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	private void clickAceptar(ActionEvent event) throws SQLException {
		String correo = txtFCorreo.getText().trim();
		String contrasenia = txtFContrasenia.getText().trim();
		if (validarFormulario(correo, contrasenia)) {
			tipoUsuario = UsuarioDAO.identificarTipoUsuario(correo);
			if (tipoUsuario != null) {
				Object usuario = UsuarioDAO.verificarCredencialesUsuario(tipoUsuario, correo, contrasenia);
				if (usuario != null) {
					irSiguientePantalla(usuario);
				} else {
					MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
							"Lo sentimos, no se ha podido recuperar la información, "
							+ "por favor intente más tarde");
				}
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Datos inválidos",
						"Los datos ingresados no son correctos, "
						+ "por favor verifique e intente nuevamente");
			}
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Campos vacíos",
					"Por favor ingrese los datos solicitados");
		}
	}

	private boolean validarFormulario(String correo, String contrasenia) {
		return !(correo == null || correo.isEmpty() || contrasenia == null || contrasenia.isEmpty());
	}

	private void irSiguientePantalla(Object usuario) {
		if (null == tipoUsuario) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se ha podido recuperar la información, "
							+ "por favor intente más tarde");
		} else switch (tipoUsuario) {
			case "coordinador":
				irMenuCoordinador();
				break;
			case "profesor":
				irMenuProfesor((Profesor) usuario);
				break;
			case "alumno":
				irMenuAlumno((Alumno) usuario);
				break;
			default:
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
						"Lo sentimos, no se ha podido recuperar la información, "
								+ "por favor intente más tarde");
				break;
		}
	}

	private void irMenuAlumno(Alumno alumno) {

	}

	private void irMenuProfesor(Profesor profesor) {

	}

	private void irMenuCoordinador() {

	}

}
