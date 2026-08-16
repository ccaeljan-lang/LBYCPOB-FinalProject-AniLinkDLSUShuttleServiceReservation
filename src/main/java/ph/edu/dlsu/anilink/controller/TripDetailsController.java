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

        loadAssignedTripAsync();
    }

    public void setTripId(Long tripId) {
        this.currentTripId = tripId;
        loadTripDetailsAsync(tripId);
    }

    private void loadAssignedTripAsync() {
        User user = userSession.getCurrentUser();
        if (user == null) {
            resetLabels();
            return;
        }

        Task<Long> task = new Task<>() {
            @Override
            protected Long call() throws Exception {
                String json = supabaseService.getTripsByDriver(user.getUserId());
                JsonNode trips = objectMapper.readTree(json);

                if (trips.isArray() && !trips.isEmpty()) {
                    return trips.get(0).path("id").asLong();
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            Long tripId = task.getValue();
            if (tripId != null) {
                setTripId(tripId);
            } else {
                resetLabels();
            }
        });

        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            resetLabels();
        });

        new Thread(task).start();
    }

    private void loadTripDetailsAsync(Long tripId) {
        Task<JsonNode> task = new Task<>() {
            @Override
            protected JsonNode call() throws Exception {
                String json = supabaseService.getTripDetails(tripId);
                JsonNode array = objectMapper.readTree(json);
                if (array.isArray() && !array.isEmpty()) {
                    return array.get(0);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            JsonNode trip = task.getValue();
            if (trip != null) {
                this.currentTripId = trip.path("id").asLong();

                String status = trip.path("status").asText("SCHEDULED");
                int capacity = trip.path("capacity").asInt(0);
                int seatsTaken = trip.path("seats_taken").asInt(0);

                // Resilient lookup: check alias "route" first, fallback to "routes"
                JsonNode route = trip.has("route") ? trip.path("route") : trip.path("routes");
                String origin = route.path("origin").asText("Unknown");
                String destination = route.path("destination").asText("Unknown");

                // Resilient lookup: check alias "schedule" first, fallback to "departure_schedules"
                JsonNode schedule = trip.has("schedule") ? trip.path("schedule") : trip.path("departure_schedules");
                String departureTime = schedule.path("departure_time").asText("N/A");

                tripIdLabel.setText("#" + currentTripId);
                routeLabel.setText(origin + " ↔ " + destination);
                scheduleLabel.setText(departureTime);
                capacityLabel.setText(String.format("%d / %d", seatsTaken, capacity));

                updateStatusBadge(status);
                viewPassengersButton.setDisable(false);
            } else {
                resetLabels();
            }
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load trip details.");
        });

        new Thread(task).start();
    }

    private void updateStatusBadge(String status) {
        statusLabel.setText(status.toUpperCase());

        String baseStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12; ";
        switch (status.toUpperCase()) {
            case "SCHEDULED":
                statusLabel.setStyle(baseStyle + "-fx-background-color: #DBEAFE; -fx-text-fill: #1D4ED8;");
                break;
            case "IN_TRANSIT":
                statusLabel.setStyle(baseStyle + "-fx-background-color: #FEF3C7; -fx-text-fill: #B45309;");
                break;
            case "COMPLETED":
                statusLabel.setStyle(baseStyle + "-fx-background-color: #DCFCE7; -fx-text-fill: #15803D;");
                break;
            default:
                statusLabel.setStyle(baseStyle + "-fx-background-color: #E2E8F0; -fx-text-fill: #475569;");
                break;
        }
    }

    private void resetLabels() {
        Platform.runLater(() -> {
            tripIdLabel.setText("--");
            routeLabel.setText("No active trip");
            scheduleLabel.setText("--");
            capacityLabel.setText("0 / 0");
            updateStatusBadge("N/A");
            viewPassengersButton.setDisable(true);
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
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