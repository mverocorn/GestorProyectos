package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLEntregaDeArchivoController implements Initializable {

	@FXML
	private TextField txtFNombreEntrega;
	@FXML
	private ComboBox<?> cBoxTipoArchivo;
	@FXML
	private TextField txtFCantidadHoras;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickSubirArchivo(ActionEvent event) {
	}

	@FXML
	private void clickEntregar(ActionEvent event) {
	}
	
}
