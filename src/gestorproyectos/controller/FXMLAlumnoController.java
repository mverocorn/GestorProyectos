/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
		System.out.println("Datos colaborador: " + alumno.getNombreAlumno()+ " " 
				+ alumno.getApellidoAlumno());
	}
	
}
