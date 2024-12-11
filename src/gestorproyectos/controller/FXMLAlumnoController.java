package gestorproyectos.controller;

import gestorproyectos.modelo.ConexionBD;
import gestorproyectos.modelo.pojo.Alumno;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FXMLAlumnoController implements Initializable {

	Alumno alumno;
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

		int idAlumno = alumno.getIdAlumno();
		try {
			// Obtener y agregar botones para las EE del alumno
			List<String> experiencias = obtenerEEPorAlumno(idAlumno);
			agregarBotonesEE(experiencias);
		} catch (SQLException ex) {
			System.err.println("Error al obtener las experiencias educativas: " + ex.getMessage());
		}
	}

	private List<String> obtenerEEPorAlumno(int idAlumno) throws SQLException {
		List<String> experienciasEducativas = new ArrayList<>();
		Connection conexionBD = ConexionBD.abrirConexion();
		if (conexionBD != null) {
			try {
				String consulta = "SELECT e.nombreEE "
						+ "FROM ee e "
						+ "JOIN inscripcionee i ON e.idEE = i.idEE "
						+ "WHERE i.idAlumno = ? AND i.estadoInscripcion = 'inscrito';";

				PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
				prepararConsulta.setInt(1, idAlumno);
				ResultSet resultado = prepararConsulta.executeQuery();
				while (resultado.next()) {
					experienciasEducativas.add(resultado.getString("nombreEE"));
				}
			} finally {
				conexionBD.close();
			}
		}
		return experienciasEducativas;
	}

	private void agregarBotonesEE(List<String> experiencias) {
		experienciasContenedor.getChildren().clear(); // Limpiar contenedor

		for (String ee : experiencias) {
			Button boton = new Button(ee);
			boton.setOnAction(event -> {
				System.out.println("Seleccionaste la EE: " + ee);
				// Aquí puedes agregar más lógica si es necesario
			});

			boton.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
			experienciasContenedor.getChildren().add(boton);
		}
	}
}
