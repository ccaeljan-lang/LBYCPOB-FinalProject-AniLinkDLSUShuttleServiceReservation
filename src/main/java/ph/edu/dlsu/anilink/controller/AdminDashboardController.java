package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class AdminDashboardController {
    @FXML
    private Label totalUsers;

    @FXML
    private Label todaysTrips;

    @FXML
    private Label activeTrips;

    @FXML
    private Label reservations;

    @FXML
    private Label availableSeats;

    @FXML
    private void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        totalUsers.setText("0");
        todaysTrips.setText("0");
        activeTrips.setText("0");
        reservations.setText("0");
        availableSeats.setText("0");
    }
}