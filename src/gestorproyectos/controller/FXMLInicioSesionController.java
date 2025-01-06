package gestorproyectos.controller;

import gestorproyectos.modelo.dao.UsuarioDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.IUsuario;
import gestorproyectos.modelo.pojo.Profesor;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import gestorproyectos.utilidades.Constantes;

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

	}

	@FXML
	private void clickAceptar(ActionEvent event) throws SQLException {
		String correo = txtFCorreo.getText().trim();
		String contrasenia = txtFContrasenia.getText().trim();
		System.out.println("Correo: " + correo + ", Contraseña: " + contrasenia);
		if (validarFormulario(correo, contrasenia)) {
			if (!verificarCoordinador(correo, contrasenia)) {
				tipoUsuario = UsuarioDAO.identificarTipoUsuario(correo);
				System.out.println("Tipo usuario: " + tipoUsuario);
				if (tipoUsuario != null) {
					IUsuario usuario = UsuarioDAO.verificarCredencialesUsuario(tipoUsuario, correo, contrasenia);
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
				irMenuCoordinador();
			}

		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Campos vacíos",
					"Por favor ingrese los datos solicitados");
		}
	}

	private boolean validarFormulario(String correo, String contrasenia) {
		return !(correo == null || correo.isEmpty() || contrasenia == null || contrasenia.isEmpty());
	}

	private void irSiguientePantalla(IUsuario usuario) {
		if (null == tipoUsuario) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se ha podido recuperar la información, "
					+ "por favor intente más tarde");
		} else {
			switch (tipoUsuario) {
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
	}

	private void irMenuAlumno(Alumno alumno) {
		try {
			Stage escenario = (Stage) txtFCorreo.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLAlumno.fxml"));
			Parent vista = loader.load();
			FXMLAlumnoController controlador = loader.getController();
			controlador.inicializarValores(alumno);

			Scene escenaPrincipalAlumno = new Scene(vista);
			escenario.setScene(escenaPrincipalAlumno);
			escenario.setTitle("Inicio Alumno");
			escenario.show();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se pudo cargar la ventana principal de alumno");
		}
	}

	private void irMenuProfesor(Profesor profesor) {
		try {
			Stage escenario = (Stage) txtFCorreo.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLProfesor.fxml"));
			Parent vista = loader.load();
			FXMLProfesorController controlador = loader.getController();
			controlador.inicializarValores(profesor);

			Scene escenaPrincipalProfesor = new Scene(vista);
			escenario.setScene(escenaPrincipalProfesor);
			escenario.setTitle("Inicio Profesor");
			escenario.show();
		} catch (IOException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se pudo cargar la ventana principal de profesor" + ex.getMessage());
		}
	}

	private void irMenuCoordinador() {
		try {
			Stage escenario = (Stage) txtFCorreo.getScene().getWindow();
			Parent vista = FXMLLoader.load(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLCoordinador.fxml"));
			Scene escenaPrincipalCoordinador = new Scene(vista);
			escenario.setScene(escenaPrincipalCoordinador);
			escenario.setTitle("Inicio Coordinador");
			escenario.show();
		} catch (IOException ex) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,
					"Error", "Lo sentimos, no se pudo cargar la ventana principal de coordinador");
		}
	}

	private boolean verificarCoordinador(String correo, String contrasenia) {
		boolean esCoordinador = false;
		if (correo.equals(Constantes.USER_COORDINADOR)) {
			if (contrasenia.equals(Constantes.PASSWORD_COORDINADOR)) {
				esCoordinador = true;
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Contraseña incorrecta",
						"La contraseña ingresada es incorrecta, "
						+ "por favor intente de nuevo");
			}
		}
		return esCoordinador;
	}

}
