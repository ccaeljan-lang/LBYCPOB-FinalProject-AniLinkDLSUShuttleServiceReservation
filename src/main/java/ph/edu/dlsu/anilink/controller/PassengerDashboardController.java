package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class PassengerDashboardController {
    @FXML
    private Label passengerName;

    @FXML
    private Label currentTrip;

    @FXML
    private Label currentStatus;

    @FXML
    private Button bookTrip;

    @FXML
    private Button reservations;

    @FXML
    private Button profile;

    @FXML
    private Button logout;

    @FXML
    private Button viewTicket;

    @FXML
    private void initialize() {
        currentTrip.setText("No current trip");
        currentStatus.setText("No active reservation");
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
        passengerName.setText(name);
    }

    public void setCurrentTrip(String trip) {
        currentTrip.setText(trip);
    }

    public void setCurrentStatus(String status) {
        currentStatus.setText(status);
    }
}