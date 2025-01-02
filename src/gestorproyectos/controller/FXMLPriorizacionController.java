package gestorproyectos.controller;

import gestorproyectos.modelo.dao.InscripcionEEDAO;
import gestorproyectos.modelo.dao.PriorizacionProyectosDAO;
import gestorproyectos.modelo.dao.ProyectoPPDAO;
import gestorproyectos.modelo.dao.ProyectoSSDAO;
import gestorproyectos.modelo.pojo.Alumno;
import gestorproyectos.modelo.pojo.EE;
import gestorproyectos.modelo.pojo.ProyectoPP;
import gestorproyectos.modelo.pojo.ProyectoSS;
import gestorproyectos.utilidades.MisUtilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javafx.stage.Stage;

public class FXMLPriorizacionController {

    @FXML
    private TableView<Object> tblPriorizacion; // Cambiar a Object

    @FXML
    private TableColumn<Object, String> colNombreProyecto;

    @FXML
    private TableColumn<Object, Integer> colOpciones;

    private EE eeSeleccionada;
	private Alumno alumno;

    private Set<Integer> prioridadesSeleccionadas = new HashSet<>();
	
    public void inicializarValores(EE eeSeleccionada, Alumno alumno) {
	this.alumno = alumno;
        this.eeSeleccionada = eeSeleccionada;
        System.out.println("[DEBUG] EE seleccionada: " + eeSeleccionada.getNombreEE());
        System.out.println("[DEBUG] Periodo de la EE seleccionada: " + eeSeleccionada.getPeriodo());
		cargarTablaPriorizacion();
    }

    private void configurarTablaPriorizacion(ObservableList<Object> proyectos) {
        colNombreProyecto.setCellValueFactory(new PropertyValueFactory<>("nombreProyecto"));

        colOpciones.setCellFactory(param -> new TableCell<Object, Integer>() {
            private final ComboBox<Integer> comboBox = new ComboBox<>();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(comboBox);

                    // Actualizar opciones basadas en las prioridades ya seleccionadas
                    actualizarOpcionesComboBox(comboBox);

                    // Seleccionar el valor actual o asignar un valor por defecto
                    comboBox.setValue(item != null ? item : getTableRow().getIndex() + 1);

                    // Listener para actualizar la prioridad seleccionada
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            // Remover la prioridad previa del conjunto
                            if (oldValue != null) {
                                prioridadesSeleccionadas.remove(oldValue);
                            }
                            // Agregar la nueva prioridad al conjunto
                            prioridadesSeleccionadas.add(newValue);

                            // Actualizar el valor de prioridad en el objeto correspondiente
                            if (getTableRow() != null && getTableRow().getItem() != null) {
                                Object proyecto = getTableRow().getItem();
                                if (proyecto instanceof ProyectoPP) {
                                    ((ProyectoPP) proyecto).setPrioridad(newValue);
                                    System.out.println("[DEBUG] Prioridad actualizada en ProyectoPP: " + newValue);
                                } else if (proyecto instanceof ProyectoSS) {
                                    ((ProyectoSS) proyecto).setPrioridad(newValue);
                                    System.out.println("[DEBUG] Prioridad actualizada en ProyectoSS: " + newValue);
                                }
                            }

                            // Actualizar las opciones de todos los ComboBox en la tabla
                            tblPriorizacion.refresh();
                        }
                    });
                }
            }
        });

        colOpciones.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
    }

    private void cargarTablaPriorizacion() {
        try {
            ObservableList<Object> proyectos = FXCollections.observableArrayList();

            // Si el nombre de la EE seleccionada es "Servicio Social"
            if ("Servicio Social".equals(eeSeleccionada.getNombreEE())) {
                System.out.println("[DEBUG] Cargando proyectos de Servicio Social...");
                String fechaPeriodo = obtenerFechaPeriodo(eeSeleccionada.getPeriodo());
                
                List<ProyectoSS> proyectosDisponiblesSS = ProyectoSSDAO.obtenerProyectosDisponiblesPorPeriodoDeEESS(fechaPeriodo);
                if (proyectosDisponiblesSS != null) {
                    System.out.println("[DEBUG] Proyectos de Servicio Social obtenidos: " + proyectosDisponiblesSS.size());
                    proyectos.addAll(proyectosDisponiblesSS);
                    tblPriorizacion.setItems(proyectos);
                    configurarTablaPriorizacion(proyectos);
                } else {
                    System.out.println("[DEBUG] No se encontraron proyectos de Servicio Social.");
                    MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pueden cargar los proyectos de Servicio Social.");
                }
            }
            // Si el nombre de la EE seleccionada es "Práctica Profesional"
            else if ("Práctica Profesional".equals(eeSeleccionada.getNombreEE())) {
                System.out.println("[DEBUG] Cargando proyectos de Práctica Profesional...");
                List<ProyectoPP> proyectosDisponiblesPP = ProyectoPPDAO.obtenerProyectosDisponiblesPorPeriodoDeEEPP(eeSeleccionada.getPeriodo());
                if (proyectosDisponiblesPP != null) {
                    System.out.println("[DEBUG] Proyectos de Práctica Profesional obtenidos: " + proyectosDisponiblesPP.size());
                    proyectos.addAll(proyectosDisponiblesPP);
                    tblPriorizacion.setItems(proyectos);
                    configurarTablaPriorizacion(proyectos);
                } else {
                    System.out.println("[DEBUG] No se encontraron proyectos de Práctica Profesional.");
                    MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pueden cargar los proyectos de Práctica Profesional.");
                }
            } else {
                System.out.println("[DEBUG] El nombre de la EE seleccionada no es válido.");
                MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "El nombre de la EE seleccionada no es válido.");
            }
        } catch (SQLException ex) {
            System.out.println("[DEBUG] Error al cargar proyectos: " + ex.getMessage());
            MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Hubo un problema al cargar los proyectos. Error: " + ex.getMessage());
        }
    }

    private void actualizarOpcionesComboBox(ComboBox<Integer> comboBox) {
        ObservableList<Integer> opciones = FXCollections.observableArrayList();

        int totalProyectos = tblPriorizacion.getItems().size();
        for (int i = 1; i <= totalProyectos; i++) {
            // Agregar la opción solo si no está seleccionada
            if (!prioridadesSeleccionadas.contains(i)) {
                opciones.add(i);
            }
        }
        comboBox.setItems(opciones);
    }

@FXML
private void clickRealizarPriorizacion(ActionEvent event) {
    ObservableList<Object> proyectos = tblPriorizacion.getItems();
    if (proyectos.isEmpty()) {
        System.out.println("[DEBUG] No hay proyectos para priorizar.");
        MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "No hay proyectos para priorizar.");
        return;
    }

    Map<Integer, Integer> proyectosYPrioridades = new HashMap<>();
    boolean prioridadesValidas = true;

    // Recorrer los proyectos para obtener sus prioridades
    for (Object proyecto : proyectos) {
        if (proyecto instanceof ProyectoPP) {
            ProyectoPP proyectoPP = (ProyectoPP) proyecto;
            if (proyectoPP.getPrioridad()<=0) {
                prioridadesValidas = false;
                break;
            }
            proyectosYPrioridades.put(proyectoPP.getIdProyectoPP(), proyectoPP.getPrioridad());
        } else if (proyecto instanceof ProyectoSS) {
            ProyectoSS proyectoSS = (ProyectoSS) proyecto;
            if (proyectoSS.getPrioridad() <=0) {
                prioridadesValidas = false;
                break;
            }
            proyectosYPrioridades.put(proyectoSS.getIdProyectoSS(), proyectoSS.getPrioridad());
        }
    }

    if (!prioridadesValidas) {
        MisUtilidades.crearAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Por favor, selecciona una prioridad para todos los proyectos.");
        return;
    }

    try {
        if ("Servicio Social".equals(eeSeleccionada.getNombreEE())) {
            System.out.println("[DEBUG] Guardando prioridades para Servicio Social...");
            PriorizacionProyectosDAO.guardarPriorizacionProyectosSS(InscripcionEEDAO.obtenerIdInscripcionEE(alumno.getIdAlumno()), proyectosYPrioridades);
        } else if ("Práctica Profesional".equals(eeSeleccionada.getNombreEE())) {
            System.out.println("[DEBUG] Guardando prioridades para Práctica Profesional...");
            PriorizacionProyectosDAO.guardarPriorizacionProyectosPP(InscripcionEEDAO.obtenerIdInscripcionEE(alumno.getIdAlumno()), proyectosYPrioridades);
        }

        // Mostrar mensaje de éxito
        MisUtilidades.crearAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Se ha guardado tu orden de preferencia para proyectos. Tu coordinador pronto te asignará tu proyecto\"");
        System.out.println("[DEBUG] Priorización guardada correctamente.");
		Stage stage = (Stage) tblPriorizacion.getScene().getWindow();
			stage.close();
    } catch (SQLException ex) {
        MisUtilidades.crearAlertaSimple(Alert.AlertType.ERROR, "Error", "Hubo un problema al guardar las prioridades. Intenta nuevamente más tarde.");
        System.out.println("[DEBUG] Error al guardar priorización: " + ex.getMessage());
    }
}

    private String obtenerFechaPeriodo(String periodo) {
        
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("MMMuuuu", Locale.ENGLISH);

        String[] partes = periodo.split("-");
        String inicioPeriodo = partes[0]; 

        LocalDate fechaInicio = LocalDate.parse(inicioPeriodo, formatterEntrada).withDayOfMonth(1);
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fechaInicio.format(formatterSalida);
    
    }

}
