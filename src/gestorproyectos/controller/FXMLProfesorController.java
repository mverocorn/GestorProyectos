package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

	@FXML
	private void clickCerrarSesion(ActionEvent event) {
		try {
			Stage stage = (Stage) lblNombreProfesor.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLInicioSesion.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Inicio Sesion");
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
