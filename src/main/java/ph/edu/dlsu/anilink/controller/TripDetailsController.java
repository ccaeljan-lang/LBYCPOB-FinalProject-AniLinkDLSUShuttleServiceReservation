package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class TripDetailsController {

    @FXML
    private Label tripIdLabel;

    @FXML
    private Label routeLabel;

    @FXML
    private Label scheduleLabel;

    @FXML
    private Label capacityLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button viewPassengersButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        tripIdLabel.setText("Trip ID: --");
        routeLabel.setText("Route: --");
        scheduleLabel.setText("Schedule: --");
        capacityLabel.setText("Capacity: 0/0");
        statusLabel.setText("Status: SCHEDULED");
    }

    @FXML
    private void handleViewPassengers() {
        System.out.println("Opening passenger list...");
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to driver dashboard...");
    }
}