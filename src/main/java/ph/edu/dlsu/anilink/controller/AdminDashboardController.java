package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminDashboardController {

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label todaysTripsLabel;

    @FXML
    private Label activeTripsLabel;

    @FXML
    private Label reservationsLabel;

    @FXML
    private Label availableSeatsLabel;

    @FXML
    private void initialize() {
        totalUsersLabel.setText("Total Users: --");
        todaysTripsLabel.setText("Today's Trips: --");
        activeTripsLabel.setText("Active Trips: --");
        reservationsLabel.setText("Reservations: --");
        availableSeatsLabel.setText("Available Seats: --");
    }
}