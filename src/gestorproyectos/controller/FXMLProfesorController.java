package gestorproyectos.controller;

import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.Profesor;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

	}

	public void inicializarValores(Profesor profesor) {
		this.profesor = profesor;

		try {
			List<EE> experiencias = InscripcionEEDAO.obtenerEEPorProfesor(profesor.getIdProfesor());
			agregarBotonesEE(experiencias);

		} catch (SQLException ex) {
			System.err.println("Error al inicializar valores: "
					+ ex.getMessage());
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Hubo un problema al cargar la información del alumno.");
		}

		saludo(profesor);
	}

	public void saludo(Profesor profesor) {
		lblNombreProfesor.setText(profesor.getNombreProfesor() + " "
				+ profesor.getApellidoProfesor());
	}

	private void agregarBotonesEE(List<EE> experiencias) {
		experienciasContenedor.getChildren().clear();

		experienciasContenedor.setSpacing(10);

		for (EE ee : experiencias) {
			VBox contenedorBoton = crearContenedorBoton(ee);

			Button boton = new Button();
			boton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
			boton.setPrefWidth(200);
			boton.setPrefHeight(80);
			boton.setGraphic(contenedorBoton);

			boton.setOnAction(event -> {
				System.out.println("Seleccionaste la EE con id " + ee.getIdEE()
						+ " y nombre: " + ee.getNombreEE() + ", periodo: "
						+ ee.getPeriodo() + ", NRC: " + ee.getNrc());

				abrirDetalleEE(ee);
			});

			experienciasContenedor.getChildren().add(boton);
		}
	}

	private VBox crearContenedorBoton(EE ee) {
		VBox contenedorBoton = new VBox();
		contenedorBoton.setSpacing(5);

		Label nombreLabel = new Label(ee.getNombreEE());
		nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");

		Label periodoLabel = new Label(ee.getPeriodo());
		periodoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

		Label nrcLabel = new Label("NRC: " + ee.getNrc());
		nrcLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

		contenedorBoton.getChildren().addAll(nombreLabel, periodoLabel, nrcLabel);

		return contenedorBoton;
	}

	private void abrirDetalleEE(EE eeSeleccionada) {
		try {
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLExperienciaEducativa.fxml"));
			Parent vista = loader.load();

			FXMLExperienciaEducativaController controladorDetalle = loader.getController();
			controladorDetalle.inicializarValores(eeSeleccionada);

			Stage nuevoEscenario = new Stage();
			Scene escena = new Scene(vista);
			nuevoEscenario.setScene(escena);
			nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
			nuevoEscenario.setTitle("Detalle de la EE");
			nuevoEscenario.showAndWait();
		} catch (IOException ex) {
			ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se pudo cargar la ventana de la EE");
		}
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
