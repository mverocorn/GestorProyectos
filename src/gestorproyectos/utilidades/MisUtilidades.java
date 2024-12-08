package gestorproyectos.utilidades;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Vero
 */
public class MisUtilidades {

	public static void crearAlertaSimple(Alert.AlertType tipo, String titulo, String contenido) {
		Alert alerta = new Alert(tipo);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(contenido);
		alerta.showAndWait();
	}

	public static boolean crearAlertaConfirmacion(String titulo, String contenido) {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(contenido);
		Optional<ButtonType> botonSeleccionado = alerta .showAndWait();
		return (botonSeleccionado.get() == ButtonType.OK);
	}

}
