package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DriverDashboardController {

    @FXML
    private Label driverNameLabel;

    @FXML
    private Label currentTripLabel;

    @FXML
    private Button viewTripDetailsButton;

    @FXML
    private Button scanQRButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        driverNameLabel.setText("Welcome, Driver");
        currentTripLabel.setText("No active trip assigned");
    }

    @FXML
    private void handleViewTripDetails() {
        System.out.println("Opening trip details...");
    }

    @FXML
    private void handleScanQR() {
        System.out.println("Opening QR scanner...");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");
    }
}