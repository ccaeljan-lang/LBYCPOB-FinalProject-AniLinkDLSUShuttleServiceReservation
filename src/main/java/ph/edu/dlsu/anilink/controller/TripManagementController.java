package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.DepartureSchedule;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.model.Trip;

@Controller
public class TripManagementController {

    @FXML
    private TextField tripIdField;

    @FXML
    private ComboBox<Route> routeComboBox;

    @FXML
    private ComboBox<DepartureSchedule> scheduleComboBox;

    @FXML
    private TextField capacityField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ListView<Trip> tripListView;

    private final ObservableList<Trip> trips =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        tripListView.setItems(trips);

        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        Trip.SCHEDULED,
                        Trip.ARRIVING,
                        Trip.BOARDING,
                        Trip.DEPARTED,
                        Trip.COMPLETED
                )
        );

        statusComboBox.setValue(Trip.SCHEDULED);
    }

    @FXML
    private void handleAddTrip() {

        String tripIdText =
                tripIdField.getText().trim();

        Route route =
                routeComboBox.getValue();

        DepartureSchedule schedule =
                scheduleComboBox.getValue();

        String capacityText =
                capacityField.getText().trim();

        if (tripIdText.isEmpty()
                || route == null
                || schedule == null
                || capacityText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please complete all fields."
            );

            return;
        }

        try {

            Long tripId =
                    Long.parseLong(tripIdText);

            int capacity =
                    Integer.parseInt(capacityText);

            if (capacity <= 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Capacity",
                        "Capacity must be greater than zero."
                );

                return;
            }

            Trip trip =
                    new Trip(
                            tripId,
                            route,
                            schedule,
                            capacity
                    );

            String status =
                    statusComboBox.getValue();

            if (status != null) {
                trip.updateStatus(status);
            }

            trips.add(trip);

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Trip Added",
                    "Trip was successfully created."
            );

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Input",
                    "Trip ID and capacity must be valid numbers."
            );
        }
    }

    @FXML
    private void handleUpdateStatus() {

        Trip selectedTrip =
                tripListView
                        .getSelectionModel()
                        .getSelectedItem();

        String selectedStatus =
                statusComboBox.getValue();

        if (selectedTrip == null
                || selectedStatus == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Selection",
                    "Please select a trip and status."
            );

            return;
        }

        selectedTrip.updateStatus(selectedStatus);

        tripListView.refresh();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Trip Updated",
                "Trip status updated to "
                        + selectedStatus
        );
    }

    @FXML
    private void handleDeleteTrip() {

        Trip selectedTrip =
                tripListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedTrip == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a trip to delete."
            );

            return;
        }

        trips.remove(selectedTrip);
    }

    private void clearFields() {

        tripIdField.clear();
        capacityField.clear();

        routeComboBox.getSelectionModel()
                .clearSelection();

        scheduleComboBox.getSelectionModel()
                .clearSelection();

        statusComboBox.setValue(Trip.SCHEDULED);
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