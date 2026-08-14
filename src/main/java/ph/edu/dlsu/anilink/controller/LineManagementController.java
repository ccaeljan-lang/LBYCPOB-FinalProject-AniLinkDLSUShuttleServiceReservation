package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LineManagementController {
    @FXML
    private TextField lineNameField;

    @FXML
    private TextField locationAField;

    @FXML
    private TextField locationBField;

    @FXML
    private ListView<String> lineListView;

    private final ObservableList<String> lines =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        lineListView.setItems(lines);
    }

    @FXML
    private void handleAddLine() {

        String lineName = lineNameField.getText().trim();
        String locationA = locationAField.getText().trim();
        String locationB = locationBField.getText().trim();

        if (lineName.isEmpty()
                || locationA.isEmpty()
                || locationB.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please fill in all fields."
            );

            return;
        }

        String line =
                lineName
                        + ": "
                        + locationA
                        + " ↔ "
                        + locationB;

        lines.add(line);
        clearFields();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Line Added",
                "The line was successfully added."
        );
    }

    @FXML
    private void handleDeleteLine() {

        String selectedLine =
                lineListView.getSelectionModel()
                        .getSelectedItem();

        if (selectedLine == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a line to delete."
            );

            return;
        }

        lines.remove(selectedLine);
    }

    private void clearFields() {

        lineNameField.clear();
        locationAField.clear();
        locationBField.clear();
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
