package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Profesor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLProfesorController implements Initializable {
	
	Profesor profesor;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	public void inicializarValores(Profesor profesor) {
		this.profesor = profesor;
		System.out.println("Datos profesor: " + profesor.getNombreProfesor() + " " 
				+ profesor.getApellidoProfesor());
	}
	
}
