package gestorproyectos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLExpedienteAlumnoController implements Initializable {

	@FXML
	private Label lblResultadoProyectp;
	@FXML
	private Label lblResultadoEmpresa;
	@FXML
	private Label lblResultadoResponsable;
	@FXML
	private Label lblResultadoPeriodo;
	@FXML
	private Label lblResultadoFecha;
	@FXML
	private Label lblResultadoDescripcion;
	@FXML
	private Label lblResultadoObjetivo;
	@FXML
	private TableView<?> tblDocumentos;
	@FXML
	private TableColumn<?, ?> colNombreDocumento;
	@FXML
	private TableColumn<?, ?> colValidar;
	@FXML
	private TableView<?> tblReportes;
	@FXML
	private TableColumn<?, ?> colNombreReporte;
	@FXML
	private TableColumn<?, ?> colHoras;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	

	@FXML
	private void clickDarDeBaja(ActionEvent event) {
	}
	
}
