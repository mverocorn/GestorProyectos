package gestorproyectos.controller;

import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.dao.AlumnoDAO;
import gestorproyectos.modelo.dao.EmpresaDAO;
import gestorproyectos.modelo.dao.ExpedienteDAO;
import gestorproyectos.modelo.dao.PriorizacionProyectosDAO;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.dao.ResponsableDAO;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.Empresa;
import gestorproyectos.modelo.pojo.Expediente;
import gestorproyectos.modelo.pojo.InscripcionEE;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.modelo.pojo.Responsable;
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
		experienciasContenedor.setSpacing(10);
		for (EE ee : experiencias) {
			VBox contenedorBoton = new VBox();
			contenedorBoton.setSpacing(5);
			Label nombreLabel = new Label(ee.getNombreEE());
			nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");
			Label periodoLabel = new Label(ee.getPeriodo());
			periodoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
			Label nrcLabel = new Label("NRC: " + ee.getNrc());
			nrcLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

			contenedorBoton.getChildren().addAll(nombreLabel, periodoLabel, nrcLabel);

			Button boton = new Button();
			boton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
			boton.setPrefWidth(200);
			boton.setPrefHeight(80);
			boton.setGraphic(contenedorBoton);

			boton.setOnAction(event -> {
				System.out.println("Seleccionaste la EE con id " + ee.getIdEE()
						+ " y nombre: " + ee.getNombreEE() + ", periodo: "
						+ ee.getPeriodo() + ", NRC: " + ee.getNrc());
                                                verificarProyectoAlumno(alumno.getIdAlumno(), ee);
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

	private void validarPriorizacionRealizada(int idAlumno, EE eeSeleccionada) {
		try {
			boolean priorizacionHecha = PriorizacionProyectosDAO.validarPriorizacionPorAlumno(idAlumno);

			if (priorizacionHecha) {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Aviso", "Tu coordinador pronto te asignará tu proyecto");
				abrirVentanaExpediente(eeSeleccionada);
			} else {
				MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Aviso", "Realiza tu priorización de proyectos.");
				abrirVentanaPriorizacion(eeSeleccionada ,alumno); // Pasar EE seleccionada
			}
		} catch (SQLException ex) {
			System.err.println("Error al validar priorización: "
					+ ex.getMessage());
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Hubo un problema al verificar la priorización de proyectos.");
		}
	}

	private void abrirVentanaPriorizacion(EE eeSeleccionada, Alumno alumno) {
		try {
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLPriorizacion.fxml"));
			Parent vista = loader.load();
			FXMLPriorizacionController controladorPriorizacion = loader.getController();
			controladorPriorizacion.inicializarValores(eeSeleccionada, alumno);

			Stage stage = new Stage();
			stage.setScene(new Scene(vista));
			stage.setTitle("Priorización de proyectos");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de priorización.");
		}
	}

	private void abrirVentanaExpediente(EE ee) {
                try {
			FXMLLoader loader = new FXMLLoader(gestorproyectos.GestorProyectos.class.getResource("vista/FXMLExpedienteAlumno.fxml"));
			Parent vista = loader.load();
			
                        InscripcionEE inscripcionEE = InscripcionEEDAO.obtenerInscripcionEE(alumno.getIdAlumno(),ee.getIdEE());
                        Expediente expediente = ExpedienteDAO.obtenerExpediente(inscripcionEE.getIdInscripcionEE());
                        if(ee.getNombreEE().equals("Servicio Social")){
                            ProyectoSS proyecto = ProyectoSSDAO.obtenerProyectoSSPorIdProyectoSS(inscripcionEE.getIdProyectoSS());
                            Responsable responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyecto.getIdResponsable());
                            Empresa empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
                            FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
                            controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyecto,true);
                        }else{
                            ProyectoPP proyecto = ProyectoPPDAO.obtenerProyectoPPPorIdProyectoPP(inscripcionEE.getIdProyectoPP());
                            Responsable responsable = ResponsableDAO.obtenerResponsablePorIdResponsable(proyecto.getIdResponsable());
                            Empresa empresa = EmpresaDAO.obtenerEmpresaPorIdEmpresa(responsable.getIdEmpresa());
                            FXMLExpedienteAlumnoController controladorExpediente = loader.getController();
                            controladorExpediente.inicializarValores(empresa, responsable, ee, inscripcionEE, expediente, proyecto,true);
                        }
                        

			Stage stage = new Stage();
			stage.setScene(new Scene(vista));
			stage.setTitle("Expediente");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de expediente.");
		} catch (SQLException ex){
                        ex.printStackTrace();
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana de expediente 1.");
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

    private void verificarProyectoAlumno(int idAlumno, EE ee) {
        try {
			boolean proyectoInscrito = AlumnoDAO.validarProyectoInscrito(idAlumno, ee.getIdEE());

			if (proyectoInscrito) {
                            abrirVentanaExpediente(ee);
			} else {
                            validarPriorizacionRealizada(idAlumno, ee);
			}
		} catch (SQLException ex) {
			System.err.println("Error al validar proyectos inscritos: "
					+ ex.getMessage());
			MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Hubo un problema al verificar la inscripcion de proyectos.");
		}
    }
}
