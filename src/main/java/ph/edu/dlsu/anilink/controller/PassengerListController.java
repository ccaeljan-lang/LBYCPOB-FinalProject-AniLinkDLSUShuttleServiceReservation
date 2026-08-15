package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Controller;

@Controller
public class PassengerListController {

    @FXML
    private ListView<String> passengerListView;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        loadPassengers();
    }

    private void loadPassengers() {
        passengerListView.getItems().clear();
        passengerListView.getItems().addAll(
                "Passenger 1 - Verified",
                "Passenger 2 - Pending Verification"
        );
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to trip details...");
    }
}