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
            System.err.println("Error al inicializar valores: " + ex.getMessage());
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

        for (EE ee : experiencias) {
            Button boton = new Button(ee.getNombreEE());
            boton.setOnAction(event -> {
                System.out.println("Seleccionaste la EE con id " + ee.getIdEE() + " y nombre: " + ee.getNombreEE());
                abrirDetalleProyecto(obtenerIdProyecto());
            });

            boton.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            experienciasContenedor.getChildren().add(boton);
        }
    }

    private void saludo(Alumno alumno) {
        lblNombreAlumno.setText(alumno.getNombreAlumno() + " " + alumno.getApellidoAlumno());
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
            System.err.println("Error al validar priorización: " + ex.getMessage());
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
