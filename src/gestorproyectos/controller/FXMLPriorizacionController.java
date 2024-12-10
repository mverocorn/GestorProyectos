package gestorproyectos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import gestorproyectos.modelo.pojo.PriorizacionProyectos;
import javafx.event.ActionEvent;

public class FXMLPriorizacionController {

    @FXML
    private TableView<PriorizacionProyectos> tblPriorizacion;

    @FXML
    private TableColumn<PriorizacionProyectos, String> colNombreProyecto;

    @FXML
    private TableColumn<PriorizacionProyectos, String> colOpciones;
	
    // Método que inicializa la tabla y sus columnas
    public void initialize() {

        // Configurar la columna con ComboBox
        colOpciones.setCellFactory(param -> new TableCell<PriorizacionProyectos, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(comboBox);
                    // Asignamos las opciones al ComboBox
                    comboBox.setItems(FXCollections.observableArrayList("Opción 1", "Opción 2", "Opción 3"));
                    comboBox.setValue(item);  // Establecemos el valor de la celda actual en el ComboBox

                    // Listener para capturar cuando el usuario selecciona una opción
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            PriorizacionProyectos proyecto = (PriorizacionProyectos) getTableRow().getItem();
                            proyecto.setPrioridad(getSelectedPrioridad(newValue)); // Actualizamos la propiedad de prioridad
                        }
                    });
                }
            }
        });
    }

    // Método para convertir la opción seleccionada en un valor de prioridad
    private int getSelectedPrioridad(String seleccion) {
        switch (seleccion) {
            case "Opción 1": return 1;
            case "Opción 2": return 2;
            case "Opción 3": return 3;
            default: return 0;
        }
    }

	@FXML
	private void clickRealizarPriorizacion(ActionEvent event) {
	}
}
