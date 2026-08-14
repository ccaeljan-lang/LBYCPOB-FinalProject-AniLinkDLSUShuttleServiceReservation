package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class ReservationController {
    @FXML
    private ComboBox<String> routeSelection;

    @FXML
    private DatePicker dateSelection;

    @FXML
    private ComboBox<String> scheduleSelection;

    @FXML
    private Label availableSeats;

    @FXML
    private Label tripStatus;

    @FXML
    private Button reserveButton;

    @FXML
    private Label reservationMessage;

    @FXML
    private void initialize() {
        loadRoutes();
        loadSchedules();
        availableSeats.setText("Available Seats: --");
        tripStatus.setText("Trip Status: --");
        reservationMessage.setText("");
    }

    private void loadRoutes() {
        routeSelection.getItems().addAll("Manila → Laguna", "Laguna → Manila");
    }

    private void loadSchedules() {
        scheduleSelection.getItems().addAll("7:00 AM", "9:00 AM", "11:00 AM", "1:00 PM", "3:00 PM", "5:00 PM");
    }

    @FXML
    private void handleRouteSelection() {
        if (routeSelection.getValue() == null) {
            availableSeats.setText("Available Seats: --");
            tripStatus.setText("Trip Status: --");
            return;
        }

        availableSeats.setText("Available Seats: 30");
        tripStatus.setText("Trip Status: Available");
    }

    @FXML
    private void handleReserve() {
        String route = routeSelection.getValue();
        LocalDate date = dateSelection.getValue();
        String schedule = scheduleSelection.getValue();

        if (route == null) {
            showMessage("Please select a route.");
            return;
        }

        if (date == null) {
            showMessage("Please select a date.");
            return;
        }

        if (schedule == null) {
            showMessage("Please select a schedule.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showMessage("Please select a valid date.");
            return;
        }

        showMessage("Reservation request submitted.");
    }

    private void showMessage(String message) {
        reservationMessage.setText(message);
    }
}