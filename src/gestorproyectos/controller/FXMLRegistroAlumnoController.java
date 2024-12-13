package gestorproyectos.controller;

import gestorproyectos.interfaces.IObservador;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.utilidades.MisUtilidades;
import gestorproyectos.utilidades.Validador;
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
    }

    @FXML
    private void clickRegistrar(ActionEvent event) {
        try {
            Alumno alumno = construirAlumnoDesdeFormulario();
            if (AlumnoDAO.validarAlumnoARegistrar(alumno)) {
                guardarNuevoAlumno(alumno);
            }
        } catch (IllegalArgumentException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Validación", ex.getMessage());
        } catch (SQLException ex) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo registrar el alumno.");
        }
    }

    private Alumno construirAlumnoDesdeFormulario() {
        String nombreAlumno = txtFNombreAlumno.getText().trim();
        String apellidoPaterno = txtFApellidoPaterno.getText().trim();
        String apellidoMaterno = txtFApellidoMaterno.getText().trim();
        String matricula = txtFMatricula.getText().trim();
        String telefono = txtFTelefono.getText().trim();
        String correo = txtFCorreo.getText().trim();
        String promedioStr = txtFPromedio.getText().trim();
        String contrasenia = txtFContrasenia.getText().trim();

        // Validaciones
        Validador.validarTexto(nombreAlumno, "Nombre del alumno", 50);
        Validador.validarTexto(apellidoPaterno, "Apellido paterno", 50);
        Validador.validarTexto(apellidoMaterno, "Apellido materno", 50);
        Validador.validarMatricula(matricula);
        Validador.validarTelefono(telefono);
        Validador.validarCorreo(correo);
        Validador.validarContrasenia(contrasenia);

        float promedio;
        try {
            promedio = Float.parseFloat(promedioStr);
            Validador.validarPromedio(promedio);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El promedio debe ser un número válido entre 0.0 y 10.0.");
        }

        // Crear objeto Alumno
        Alumno alumno = new Alumno();
        alumno.setNombreAlumno(nombreAlumno);
        alumno.setApellidoAlumno(apellidoPaterno + " " + apellidoMaterno);
        alumno.setMatricula(matricula);
        alumno.setTelefonoAlumno(telefono);
        alumno.setCorreo(correo);
        alumno.setPromedio(promedio);
        alumno.setContrasenia(contrasenia);

        return alumno;
    }

    private void guardarNuevoAlumno(Alumno alumno) throws SQLException {
        HashMap<String, Object> respuesta = AlumnoDAO.registrarAlumno(alumno);
        if (!((Boolean) respuesta.get("error"))) {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", respuesta.get("mensaje").toString());
            limpiarFormulario();
            if (observador != null) {
                observador.notificarAlumnoGuardado(alumno.getNombreAlumno(), "Registro nuevo");
            }
        } else {
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", respuesta.get("mensaje").toString());
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        cerrarFormulario();
    }

    private void limpiarFormulario() {
        txtFNombreAlumno.clear();
        txtFApellidoPaterno.clear();
        txtFApellidoMaterno.clear();
        txtFMatricula.clear();
        txtFTelefono.clear();
        txtFCorreo.clear();
        txtFPromedio.clear();
        txtFContrasenia.clear();
    }

    private void cerrarFormulario() {
        Stage stage = (Stage) txtFNombreAlumno.getScene().getWindow();
        stage.close();
    }
}
