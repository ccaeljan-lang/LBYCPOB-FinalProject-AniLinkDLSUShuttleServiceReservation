package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import ph.edu.dlsu.anilink.model.DepartureSchedule;
import ph.edu.dlsu.anilink.model.Route;

public class ScheduleManagementController {

    @FXML
    private TextField scheduleIdField;

    @FXML
    private ComboBox<Route> routeComboBox;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField reservationLimitField;

    @FXML
    private ListView<DepartureSchedule> scheduleListView;

    private final ObservableList<DepartureSchedule> schedules =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        scheduleListView.setItems(schedules);
    }

    private void clearFields() {

        scheduleIdField.clear();

        departureTimeField.clear();

        reservationLimitField.clear();

        routeComboBox.getSelectionModel()
                .clearSelection();
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
