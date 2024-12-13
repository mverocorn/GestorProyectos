package gestorproyectos;

import gestorproyectos.modelo.dao.InscripcionEEDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

public class GestorProyectos extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        actualizarEstadoInscripcion();

        Parent root = FXMLLoader.load(getClass().getResource("vista/FXMLInicioSesion.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarEstadoInscripcion() {
        try {
            InscripcionEEDAO inscripcionEEDAO = new InscripcionEEDAO();
            inscripcionEEDAO.actualizarEstadoInscripcion();
        } catch (SQLException ex) {
            System.err.println("Error al actualizar estado de inscripción: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
