package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.dao.PriorizacionProyectosDAO;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.utilidades.MisUtilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class FXMLAlumnoController implements Initializable {

    private Alumno alumno;

    @FXML
    private Label lblNombreAlumno;

    @FXML
    private HBox experienciasContenedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void inicializarValores(Alumno alumno) {
        this.alumno = alumno;

        try {
            List<EE> experiencias = InscripcionEEDAO.obtenerEEActivaPorAlumno(alumno.getIdAlumno());
            agregarBotonesEE(experiencias);
            validarPriorizacionRealizada(alumno.getIdAlumno());

        } catch (SQLException ex) {
            System.err.println("Error al inicializar valores: "
                + ex.getMessage());
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error",
                "Hubo un problema al cargar la información del alumno.");
        }

        saludo(alumno);
    }

    private int obtenerIdProyecto() {
        int idProyecto = 0;
        try {
            idProyecto = AlumnoDAO.obtenerIdProyectoPorIdAlumno(alumno.getIdAlumno());
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idProyecto;
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
                    FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLPriorizacion.fxml"));
                    Stage stage = new Stage(); // Nueva ventana
                    stage.setScene(new Scene(loader.load())); // Cargar la escena de FXML
                    stage.setTitle("Priorizacion de proyectos"); // Título de la ventana
                    stage.show(); // Mostrar la ventana
                } catch (IOException e) {
                    e.printStackTrace(); // Manejar el error en caso de que no se cargue el FXML
                }
            });

            experienciasContenedor.getChildren().add(boton);
        }
    }

    private void saludo(Alumno alumno) {
        lblNombreAlumno.setText(alumno.getNombreAlumno() + " "
            + alumno.getApellidoAlumno());
    }

    private void abrirDetalleProyecto(int idProyecto) {
        int idAlumno = alumno.getIdAlumno();
        try {
            Stage nuevoEscenario = new Stage();
            FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLProyecto.fxml"));
            Parent vista = loader.load();
            FXMLProyectoController controladorDetalle = loader.getController();
            controladorDetalle.inicializarValores(idProyecto, idAlumno);

            Scene nuevaEscena = new Scene(vista);
            nuevoEscenario.setScene(nuevaEscena);
            nuevoEscenario.setTitle("Detalle de la EE");
            nuevoEscenario.initModality(Modality.APPLICATION_MODAL);
            nuevoEscenario.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Lo sentimos, no se pudo cargar la ventana de la EE.");
        }
    }

    private void validarPriorizacionRealizada(int idAlumno) {
        try {
            boolean priorizacionHecha = PriorizacionProyectosDAO.validarPriorizacionPorAlumno(idAlumno);

            if (priorizacionHecha) {
                System.out.println("El alumno ya ha realizado su priorización de proyectos.");
            } else {
                MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Aviso", "El alumno no ha realizado su priorización de proyectos.");
            }
        } catch (SQLException ex) {
            System.err.println("Error al validar priorización: "
                + ex.getMessage());
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Hubo un problema al verificar la priorización de proyectos.");
        }
    }

    @FXML
    private void clickCerrarSesion(ActionEvent event) {
        try {
            Stage stage = (Stage) lblNombreAlumno.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLInicioSesion.fxml"));
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
