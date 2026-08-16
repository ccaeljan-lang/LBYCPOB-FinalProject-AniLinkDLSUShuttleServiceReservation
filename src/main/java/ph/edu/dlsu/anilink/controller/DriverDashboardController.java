package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class AdminDashboardController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper;

    @FXML private Label totalUsers;
    @FXML private Label todaysTrips;
    @FXML private Label activeTrips;
    @FXML private Label reservations;
    @FXML private Label availableSeats;

    public AdminDashboardController(SupabaseService supabaseService, UserSession userSession, ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
        this.objectMapper = new ObjectMapper();
    }

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