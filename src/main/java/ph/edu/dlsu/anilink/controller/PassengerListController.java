package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class PassengerListController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private Label driverNameLabel;
    @FXML private ListView<String> passengerListView;
    @FXML private Button backButton;

    public PassengerListController(SupabaseService supabaseService,
                                   UserSession userSession,
                                   ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && driverNameLabel != null) {
            driverNameLabel.setText("Welcome, " + user.getName());
        }

        loadPassengers();
    }

    // Temporary sync data method
    private void loadPassengers() {
        passengerListView.getItems().clear();
        passengerListView.getItems().addAll(
                "Passenger 1 - Verified",
                "Passenger 2 - Pending Verification"
        );
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Returning to trip details...");
    }
}