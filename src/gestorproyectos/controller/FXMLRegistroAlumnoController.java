package gestorproyectos.controller;

import gestorproyectos.interfaces.IObservador;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.utilidades.MisUtilidades;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

	private IObservador observador;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	public void inicializarValores() {

	}
	
	private boolean sonCamposValidos() {
			boolean valido = false;

			return valido;
		}
	
	@FXML
	private void clickRegistrar(ActionEvent event) throws SQLException {
		if (sonCamposValidos()) {
			String nombreAlumno = txtFNombreAlumno.getText().trim();
			String apellidoAlumno = (txtFApellidoPaterno.getText().trim() + " " + txtFApellidoMaterno.getText().trim());
			String matricula = txtFMatricula.getText().trim();
			String telefonoAlumno = txtFTelefono.getText().trim();
			String correo = txtFCorreo.getText().trim();
			float promedio = Float.parseFloat(txtFPromedio.getText().trim());

			
			Alumno alumno = new Alumno();
			alumno.setNombreAlumno(nombreAlumno);
			alumno.setApellidoAlumno(apellidoAlumno);
			alumno.setMatricula(matricula);
			alumno.setTelefonoAlumno(telefonoAlumno);
			alumno.setCorreo(correo);
			alumno.setPromedio(promedio);
			
			guardarNuevoAlumno(alumno);
		} 
	}

	private void guardarNuevoAlumno(Alumno alumno) throws SQLException {
		HashMap<String, Object> respuesta = AlumnoDAO.registrarAlumno(alumno);
		if (!((Boolean) respuesta.get("error"))) {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,
					"Alumno registrado", respuesta.get("mensaje").toString());
			cerrarFormulario();
			observador.notificarAlumnoGuardado(alumno.getNombreAlumno(), "Registro nuevo");
		} else {
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, 
					"Error al guardar", respuesta.get("mensaje").toString());
		}
	}

	@FXML
	private void clickCancelar(ActionEvent event) {
		cerrarFormulario();
	}

	private void cerrarFormulario() {
		Stage escenarioBase = (Stage) txtFNombreAlumno.getScene().getWindow();
		escenarioBase.close();
	}

}
