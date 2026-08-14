package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import ph.edu.dlsu.anilink.model.DepartureSchedule;
import ph.edu.dlsu.anilink.model.Trip;

public class TripManagementController {
    @FXML
    private TextField tripIdField;

    @FXML
    private ComboBox<DepartureSchedule>
            scheduleComboBox;

    @FXML
    private TextField capacityField;

    @FXML
    private ComboBox<Trip.TripStatus>
            statusComboBox;

    @FXML
    private ListView<Trip> tripListView;

    private final ObservableList<Trip> trips =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        tripListView.setItems(trips);

        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        Trip.TripStatus.values()
                )
        );

        statusComboBox.setValue(
                Trip.TripStatus.SCHEDULED
        );
    }

    private void clearFields() {

        tripIdField.clear();

        capacityField.clear();

        scheduleComboBox.getSelectionModel()
                .clearSelection();

        statusComboBox.setValue(
                Trip.TripStatus.SCHEDULED
        );
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
