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
    private Label availableSeatsLabel;

    @FXML
    private Label reservationMessageLabel;

    @FXML
    private Button reserveButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        loadRoutes();
        loadSchedules();

        availableSeatsLabel.setText("Available Seats: --");
        reservationMessageLabel.setText("");
    }

    private void loadRoutes() {
        routeSelection.getItems().clear();

        routeSelection.getItems().addAll(
                "Manila → Laguna",
                "Laguna → Manila"
        );
    }

    private void loadSchedules() {
        scheduleSelection.getItems().clear();

        scheduleSelection.getItems().addAll(
                "7:00 AM",
                "9:00 AM",
                "11:00 AM",
                "1:00 PM",
                "3:00 PM",
                "5:00 PM"
        );
    }

    @FXML
    private void handleRouteSelection() {
        String selectedRoute = routeSelection.getValue();

        if (selectedRoute == null) {
            availableSeatsLabel.setText("Available Seats: --");
            return;
        }

        availableSeatsLabel.setText("Available Seats: 30");
    }

    @FXML
    private void handleReserve() {
        String route = routeSelection.getValue();
        LocalDate date = dateSelection.getValue();
        String schedule = scheduleSelection.getValue();

        if (route == null || route.isEmpty()) {
            showMessage("Please select a route.");
            return;
        }

        if (date == null) {
            showMessage("Please select a date.");
            return;
        }

        if (schedule == null || schedule.isEmpty()) {
            showMessage("Please select a schedule.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showMessage("Please select a valid date.");
            return;
        }

        processReservation(route, date, schedule);
    }

    private void processReservation(
            String route,
            LocalDate date,
            String schedule) {

        showMessage(
                "Reservation request submitted for "
                        + route
                        + " on "
                        + date
                        + " at "
                        + schedule
                        + "."
        );
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to passenger dashboard...");
    }

    private void showMessage(String message) {
        reservationMessageLabel.setText(message);
    }
}