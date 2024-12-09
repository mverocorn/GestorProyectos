package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Alumno;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLAlumnoController implements Initializable {
	
	Alumno alumno;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	public void inicializarValores(Alumno alumno){
		this.alumno = alumno;
		System.out.println("Datos alumno: " + alumno.getNombreAlumno()+ " " 
				+ alumno.getApellidoAlumno());
	}
	
}
