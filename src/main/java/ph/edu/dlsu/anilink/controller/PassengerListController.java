package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.util.ArrayList;
import java.util.List;

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

        loadPassengersAsync();
    }

    private void loadPassengersAsync() {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                List<String> passengersList = new ArrayList<>();
                String json = supabaseService.getUsersByRole("PASSENGER");
                JsonNode passengersArray = objectMapper.readTree(json);

                if (passengersArray.isArray() && !passengersArray.isEmpty()) {
                    for (JsonNode passenger : passengersArray) {
                        long id = passenger.path("id").asLong();
                        String name = passenger.path("name").asText("Unknown");
                        String email = passenger.path("email").asText("N/A");
                        String category = passenger.path("category").asText("STUDENT");

                        String displayText = String.format("#%d | %s (%s) — [%s]", id, name, email, category);
                        passengersList.add(displayText);
                    }
                } else {
                    passengersList.add("No registered passengers found.");
                }
                return passengersList;
            }
        };

        task.setOnSucceeded(e -> {
            passengerListView.getItems().clear();
            passengerListView.getItems().addAll(task.getValue());
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            showAlert("Database Error", "Failed to retrieve passenger list from Supabase.");
        });

        new Thread(task).start();
    }

    // Navigation Handlers
    @FXML
    private void handleDashboard(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/DriverDashboard.fxml", 1000, 650);
    }

    @FXML
    private void handleTripDetails(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/TripDetails.fxml", 1000, 650);
    }

    @FXML
    private void handleScanQR(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/QRScanner.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/DriverDashboard.fxml", 1000, 650);
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}