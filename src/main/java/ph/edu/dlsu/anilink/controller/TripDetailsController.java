package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class TripDetailsController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private Label driverNameLabel;
    @FXML private Label tripIdLabel;
    @FXML private Label routeLabel;
    @FXML private Label scheduleLabel;
    @FXML private Label capacityLabel;
    @FXML private Label statusLabel;
    @FXML private Button viewPassengersButton;
    @FXML private Button backButton;

    private Long currentTripId;

    public TripDetailsController(SupabaseService supabaseService, UserSession userSession, ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        // Setup User Label
        User user = userSession.getCurrentUser();
        if (user != null && driverNameLabel != null) {
            driverNameLabel.setText("Welcome, " + user.getName());
        }

        // Temporary placeholder text
        tripIdLabel.setText("Trip ID: --");
        routeLabel.setText("Route: --");
        scheduleLabel.setText("Schedule: --");
        capacityLabel.setText("Capacity: 0/0");
        statusLabel.setText("Status: SCHEDULED");
    }

    @FXML
    private void handleViewPassengers(ActionEvent event) {
        System.out.println("Opening passenger list...");
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        System.out.println("Returning to driver dashboard...");
    }
}