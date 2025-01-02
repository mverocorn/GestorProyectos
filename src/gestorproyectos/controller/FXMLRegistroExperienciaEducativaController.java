package gestorproyectos.controller;

import gestorproyectos.modelo.dao.EEDAO;
import gestorproyectos.modelo.dao.ProfesorDAO;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.Profesor;
import gestorproyectos.utilidades.MisUtilidades;
import gestorproyectos.utilidades.Validador;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vero
 */
public class FXMLRegistroExperienciaEducativaController implements Initializable {

	@FXML
	private TextField txtFNRC;
	@FXML
	private TextField txtFSeccion;
	
	private ObservableList<Profesor> profesores;
	@FXML
	private ComboBox<Profesor> cBoxProfesor;
        @FXML
        private ComboBox<String> cBoxNombreEE;
        @FXML
        private ComboBox<String> cBoxPeriodo;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cargarListaProfesores();
		cargarPeriodos();
                cargarNombreEE();
	}	

	@FXML
	private void clickCancelar(ActionEvent event) {
            cerrarVentana();
	}

	@FXML
	private void clickRegistrar(ActionEvent event) {
            
        boolean datosValidos = datosValidos();
        try{
        if (datosValidos){
            EE experienciaEducativa = obtenerEE();
            HashMap<String,Object> respuesta = EEDAO.registrarEE(experienciaEducativa);
                if(!(boolean)respuesta.get("error")){
                    MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION,"Se ha registrado la experiencia educativa", ""+respuesta.get("mensaje"));
                    cerrarVentana();
                }else{
                    MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", ""+respuesta.get("mensaje"));
                }
        }else{
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR,"Error", "Lo sentimos, los datos estan incompletos");
        }
        }catch (SQLException ex) {
                MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No se pudo subir el archivo.");
            }  
	}
	
	private void cargarListaProfesores() {
		try {
			profesores = FXCollections.observableArrayList();
			List<Profesor> profesoresBD = ProfesorDAO.obtenerProfesores();
			if(profesoresBD!=null && !profesoresBD.isEmpty()) {
				profesores.addAll(profesoresBD);
				cBoxProfesor.setItems(profesores);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error: Sin información",
						"Lo sentimos, no se pueden cargar los profesores o "
								+ "todavía no hay ninguno registrado en el sistema");
			}
		} catch (SQLException ex) {
		}
	}
	
private void cargarPeriodos() {
        ObservableList<String> periodos = FXCollections.observableArrayList();
        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        for(int anioPeriodo = 2020; anioPeriodo<=anioActual;anioPeriodo++){
            String periodoFebreroJulio = ("FEB" +anioPeriodo+ "-" + "JUL" + anioPeriodo);
            String periodoAgostoEnero = ("AGO" +anioPeriodo+ "-" + "ENE" + (anioPeriodo+1));
            periodos.addAll(periodoFebreroJulio,periodoAgostoEnero);
        }
        cBoxPeriodo.setItems(periodos);
    }

    private void cargarNombreEE() {
        ObservableList<String> nombresEE = FXCollections.observableArrayList("Servicio Social","Practica Profesional");
        cBoxNombreEE.setItems(nombresEE);
    }	

    private void cerrarVentana() {
        Stage stage = (Stage) cBoxPeriodo.getScene().getWindow();
        stage.close();
    }
    
    private EE obtenerEE() {
        EE experienciaEducativa = new EE();
        
        experienciaEducativa.setNombreEE(cBoxNombreEE.getSelectionModel().getSelectedItem());
        experienciaEducativa.setNrc(Integer.parseInt(txtFNRC.getText()));
        experienciaEducativa.setSeccion(Integer.parseInt(txtFSeccion.getText()));
        experienciaEducativa.setIdProfesor(cBoxProfesor.getSelectionModel().getSelectedItem().getIdProfesor());
        experienciaEducativa.setPeriodo(cBoxPeriodo.getSelectionModel().getSelectedItem());
        
        return experienciaEducativa;
    }    

    private boolean datosValidos() {
        try{
        Validador.validarNRC(Integer.parseInt(txtFNRC.getText()));
        Validador.validarSeccion(Integer.parseInt(txtFSeccion.getText()));
        if(cBoxNombreEE.getSelectionModel().getSelectedItem().isEmpty() || cBoxPeriodo.getSelectionModel().getSelectedItem().isEmpty() || cBoxProfesor.getSelectionModel().getSelectedItem()==null){
            return false;
        }
        return true;
        }catch(IllegalArgumentException ex){
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error de campos", ex.getMessage());
            return false;
        }
    }
	
}
