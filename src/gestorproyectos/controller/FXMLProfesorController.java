package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Profesor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLProfesorController implements Initializable {
	
	Profesor profesor;
	@FXML
	private Label lblNombreProfesor;
	@FXML
	private HBox experienciasContenedor;

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
		saludo(profesor);
	}
	
	public void saludo(Profesor profesor) {
		lblNombreProfesor.setText(profesor.getNombreProfesor() + " " + profesor.getApellidoProfesor());
	}
	
}
