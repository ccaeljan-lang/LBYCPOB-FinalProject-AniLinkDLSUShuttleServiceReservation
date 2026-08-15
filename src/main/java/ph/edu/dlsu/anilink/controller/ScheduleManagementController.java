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
import ph.edu.dlsu.anilink.service.SupabaseService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class ScheduleManagementController {
    private final SupabaseService supabaseService;

    public ScheduleManagementController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @FXML
    private TextField scheduleIdField;

    @FXML
    private ComboBox<Route> routeComboBox;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField capacityField;

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

        String scheduleIdText =
                scheduleIdField.getText().trim();

        Route route =
                routeComboBox.getValue();

        String departureTimeText =
                departureTimeField.getText().trim();

        String capacityText =
                capacityField.getText().trim();

        if (scheduleIdText.isEmpty()
                || route == null
                || departureTimeText.isEmpty()
                || capacityText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please complete all fields."
            );

            return;
        }

        try {

            Long scheduleId =
                    Long.parseLong(scheduleIdText);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("HH:mm");

            LocalTime departureTime =
                    LocalTime.parse(
                            departureTimeText,
                            formatter
                    );

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

            DepartureSchedule schedule =
                    new DepartureSchedule(
                            scheduleId,
                            route,
                            departureTime,
                            capacity
                    );

            try {
                supabaseService.createSchedule(schedule);
                schedules.add(schedule);
                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Schedule Added",
                        "Departure schedule was successfully added."
                );
            } catch (Exception e) {
                showAlert(
                        Alert.AlertType.ERROR,
                        "Database Error",
                        "Unable to save schedule to Supabase."
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Input",
                    "Schedule ID and capacity must be valid numbers."
            );

        } catch (DateTimeParseException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Time",
                    "Please use the format HH:mm.\nExample: 08:30"
            );
        }
    }

    @FXML
    private void handleDeleteSchedule() {
        DepartureSchedule selectedSchedule = scheduleListView.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a schedule to delete."
            );
            return;
        }

        try {
            supabaseService.deleteSchedule(
                    selectedSchedule.getScheduleId()
            );

            schedules.remove(selectedSchedule);
        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    "Unable to delete schedule from Supabase."
            );
        }
    }

    private void clearFields() {

        scheduleIdField.clear();
        departureTimeField.clear();
        capacityField.clear();

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