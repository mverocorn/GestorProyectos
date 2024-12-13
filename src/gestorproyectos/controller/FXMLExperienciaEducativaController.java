package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.EE;
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
public class FXMLExperienciaEducativaController implements Initializable {

	@FXML
	private TableView<?> tblAlumnosDeExperiencia;
	@FXML
	private TableColumn<?, ?> colNombreAlumno;
	@FXML
	private TableColumn<?, ?> colMatricula;
	@FXML
	private Label txtFResultadoNombre;
	@FXML
	private Label txtFResultadoNRC;
	@FXML
	private Label txtFResultadoSeccion;

	private EE ee;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	public void inicializarValores(EE ee) {
		this.ee = ee;
		cargarInfo();
	}

	private void cargarInfo() {
		txtFResultadoNombre.setText(ee.getNombreEE());
		txtFResultadoNRC.setText(ee.getNrc() + "");
		txtFResultadoSeccion.setText(ee.getSeccion() + "");
	}

	@FXML
	private void clickAsignarAlumno(ActionEvent event) {
	}

}
