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

    @FXML
    private void handleAddSchedule() {

        String scheduleId =
                scheduleIdField.getText().trim();

        Route route =
                routeComboBox.getValue();

        String departureTimeText =
                departureTimeField.getText().trim();

        String limitText =
                reservationLimitField.getText().trim();

        if (scheduleId.isEmpty()
                || route == null
                || departureTimeText.isEmpty()
                || limitText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please complete all fields."
            );

            return;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyy-MM-dd HH:mm"
                );

        LocalDateTime departureTime =
                LocalDateTime.parse(
                        departureTimeText,
                        formatter
                );

        int reservationLimit =
                Integer.parseInt(limitText);

        DepartureSchedule schedule =
                new DepartureSchedule(
                        scheduleId,
                        route,
                        departureTime,
                        reservationLimit
                );

        schedules.add(schedule);

        route.addSchedule(schedule);

        clearFields();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Schedule Added",
                "Departure schedule was successfully added."
        );
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
