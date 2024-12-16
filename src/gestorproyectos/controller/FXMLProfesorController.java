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
        // TODO
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

        // Agregar un espaciado entre los botones en el contenedor principal
        experienciasContenedor.setSpacing(10);  // Espacio de 10px entre cada botón

        for (EE ee : experiencias) {
            // Crear un contenedor para los elementos del botón
            VBox contenedorBoton = new VBox();
            contenedorBoton.setSpacing(5);  // Espaciado entre los Labels

            // Crear el Label para el nombre de la EE en negritas con color específico
            Label nombreLabel = new Label(ee.getNombreEE());
            nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");  // Color negro para el nombre

            // Crear el Label para el periodo en texto normal y tamaño pequeño con color específico
            Label periodoLabel = new Label(ee.getPeriodo());
            periodoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");  // Color gris para el periodo

            // Crear el Label para el NRC en texto normal y tamaño pequeño con color específico
            Label nrcLabel = new Label("NRC: " + ee.getNrc());
            nrcLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");  // Color gris para el NRC

            // Agregar los Labels al contenedor
            contenedorBoton.getChildren().addAll(nombreLabel, periodoLabel, nrcLabel);

            // Crear el botón y asignar el contenedor con los Labels como su contenido
            Button boton = new Button();
            boton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");  // Fondo verde y texto blanco en el botón
            boton.setPrefWidth(200);  // Puedes ajustar el ancho si es necesario
            boton.setPrefHeight(80); // Puedes ajustar la altura si es necesario
            boton.setGraphic(contenedorBoton);

            // Configurar la acción del botón
            boton.setOnAction(event -> {
                System.out.println("Seleccionaste la EE con id " + ee.getIdEE()
                    + " y nombre: " + ee.getNombreEE() + ", periodo: "
                    + ee.getPeriodo() + ", NRC: " + ee.getNrc());

                // Abrir la interfaz AsignacionAlumnoAEE.fxml
                try {
                    FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLAsignacionAlumnoAEE.fxml"));
                    Stage stage = new Stage(); // Nueva ventana
                    stage.setScene(new Scene(loader.load())); // Cargar la escena de FXML
                    stage.setTitle("Asignación de Alumno a EE"); // Título de la ventana
                    stage.show(); // Mostrar la ventana
                } catch (IOException e) {
                    e.printStackTrace(); // Manejar el error en caso de que no se cargue el FXML
                }
            });
            
            experienciasContenedor.getChildren().add(boton);
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
