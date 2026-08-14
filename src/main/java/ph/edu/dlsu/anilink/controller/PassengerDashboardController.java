package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PassengerDashboardController {

    @FXML
    private Label passengerNameLabel;

    @FXML
    private Label currentTripLabel;

    @FXML
    private Label currentStatusLabel;

    @FXML
    private Button bookTripButton;

    @FXML
    private Button reservationsButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button viewTicketButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        currentTripLabel.setText("No current trip");
        currentStatusLabel.setText("No active reservation");
    }

    @FXML
    private void handleBookTrip() {
        System.out.println("Opening trip reservation...");
    }

    @FXML
    private void handleReservations() {
        System.out.println("Opening reservations...");
    }

    @FXML
    private void handleProfile() {
        System.out.println("Opening profile...");
    }

    @FXML
    private void handleViewTicket() {
        System.out.println("Opening ticket...");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");
    }

    public void setPassengerName(String name) {
        passengerNameLabel.setText(name);
    }

    public void setCurrentTrip(String trip) {
        currentTripLabel.setText(trip);
    }

    public void setCurrentStatus(String status) {
        currentStatusLabel.setText(status);
    }
}