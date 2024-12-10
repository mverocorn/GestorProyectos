/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLRegistroProyectoController implements Initializable {

	@FXML
	private RadioButton rbPractica;
	@FXML
	private RadioButton rbServicio;
	@FXML
	private TextField txtFNombreProyecto;
	@FXML
	private DatePicker dpFechaInicio;
	@FXML
	private TextField txtFPeriodo;
	@FXML
	private TextField txtFCupo;
	@FXML
	private TextField txtFEmpresa;
	@FXML
	private ComboBox<?> cBoxResponsable;
	@FXML
	private TextArea txtAObjetivo;
	@FXML
	private TextArea txtADescripcion;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickRegistrar(ActionEvent event) {
	}
	
}
