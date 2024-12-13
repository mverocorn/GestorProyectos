package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.pojo.EE;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLAlumnoController implements Initializable {

	private Alumno alumno;

	@FXML
	private Label lblNombreAlumno;

	@FXML
	private HBox experienciasContenedor;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Método de inicialización vacío
	}

	public void inicializarValores(Alumno alumno) {
		this.alumno = alumno;

		try {
			List<EE> experiencias = InscripcionEEDAO.obtenerEEActivaPorAlumno(alumno.getIdAlumno());
			agregarBotonesEE(experiencias);
		} catch (SQLException ex) {
			System.err.println("Error al obtener las experiencias educativas: " + ex.getMessage());
		}

		saludo(alumno);
	}

	private void agregarBotonesEE(List<EE> experiencias) {
		experienciasContenedor.getChildren().clear();

		for (EE ee : experiencias) {
			Button boton = new Button(ee.getNombreEE());
			boton.setOnAction(event -> {
				System.out.println("Seleccionaste la EE con id " + ee.getIdEE() + " y nombre: " + ee.getNombreEE());
				abrirDetalleEE(ee);

			});

			boton.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
			experienciasContenedor.getChildren().add(boton);
		}
	}

	private void saludo(Alumno alumno) {
		lblNombreAlumno.setText(alumno.getNombreAlumno() + " " + alumno.getApellidoAlumno());
	}

	private void abrirDetalleEE(EE eeSeleccionada) {
		try {
			Stage escenario = (Stage) lblNombreAlumno.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource(
					"vista/FXMLExperienciaEducativa.fxml"));
			Parent vista = loader.load();
			FXMLExperienciaEducativaController controladorDetalle = loader.getController();
			controladorDetalle.inicializarValores(eeSeleccionada);

			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Detalle de la EE");
			escenario.show();

		} catch (IOException ex) {
			ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
					"Lo sentimos, no se pudo cargar la ventana de la ee");
		}
	}

	@FXML
	private void clickCerrarSesion(ActionEvent event) {
		try {
			Stage stage = (Stage) lblNombreAlumno.getScene().getWindow();
			stage.close();
			FXMLLoader loader = new FXMLLoader(
					gestorproyectos.GestorProyectos.class.getResource(
							"vista/FXMLInicioSesion.fxml"));
			Parent vista = loader.load();
			Stage escenario = new Stage();
			Scene escena = new Scene(vista);
			escenario.setScene(escena);
			escenario.setTitle("Inicio Sesión");
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
