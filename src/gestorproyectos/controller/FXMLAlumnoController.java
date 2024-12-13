package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
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
            // Obtener experiencias educativas desde InscripcionEEDAO
            List<String> experiencias = InscripcionEEDAO.obtenerNombreTodasEEPorAlumno(alumno.getIdAlumno());
            if (!experiencias.isEmpty()) {
                agregarBotonesEE(experiencias);
            } else {
                System.out.println("No se encontraron experiencias educativas para este alumno.");
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener las experiencias educativas: " + ex.getMessage());
        }
        saludo(alumno);
    }

    private void saludo(Alumno alumno) {
        lblNombreAlumno.setText(alumno.getNombreAlumno() + " " + alumno.getApellidoAlumno());
    }

    private void agregarBotonesEE(List<String> experiencias) {
        experienciasContenedor.getChildren().clear(); // Limpiar contenedor

        for (String ee : experiencias) {
            System.out.println("Creando botón para: " + ee); // Mensaje de depuración
            Button boton = new Button(ee);
            boton.setOnAction(event -> {
                System.out.println("Seleccionaste la EE: " + ee);
                // Agregar lógica adicional si es necesario
            });

            // Estilo del botón
            boton.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            experienciasContenedor.getChildren().add(boton);
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
