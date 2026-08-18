package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

/**
 * JavaFX controller for the Driver Dashboard view.
 * <p>
 * This class manages the main dashboard interface for driver users. It is
 * responsible for displaying a personalized welcome message and asynchronously
 * retrieving the driver's currently assigned active trip using {@link SupabaseService}.
 * It safely updates the UI components on the JavaFX Application Thread. Additionally,
 * it handles navigation to other driver-specific modules such as Trip Details and
 * the QR Scanner using the {@link ViewNavigator}.
 * </p>
 */
@Controller
public class DriverDashboardController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper;

    @FXML private Label driverNameLabel;
    @FXML private Label currentTripLabel;

    public DriverDashboardController(SupabaseService supabaseService, UserSession userSession, ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
        this.objectMapper = new ObjectMapper();
    }

    @FXML
    public void initialize() {
        User currentUser = userSession.getCurrentUser();
        if (currentUser != null) {
            if (driverNameLabel != null) {
                driverNameLabel.setText("Welcome, " + currentUser.getName());
            }
            loadCurrentTripAsync(currentUser.getUserId());
        }
    }

    private void loadCurrentTripAsync(Long driverId) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                // 1. Get the active trips for this driver
                String tripsJson = supabaseService.getTripsByDriver(driverId);
                JsonNode tripsArray = objectMapper.readTree(tripsJson);

                if (!tripsArray.isArray() || tripsArray.isEmpty()) {
                    return "No active trip assigned.";
                }

                // 2. Grab the first active trip
                JsonNode firstTrip = tripsArray.get(0);
                Long tripId = firstTrip.path("id").asLong(); // Adjust to "trip_id" if that's your DB column name

                // 3. Get full details (including route and schedule joins)
                String tripDetailsJson = supabaseService.getTripDetails(tripId);
                JsonNode tripDetailsArray = objectMapper.readTree(tripDetailsJson);

                if (tripDetailsArray.isArray() && !tripDetailsArray.isEmpty()) {
                    JsonNode details = tripDetailsArray.get(0);

                    String origin = details.path("route").path("origin").asText("Unknown Origin");
                    String destination = details.path("route").path("destination").asText("Unknown Destination");
                    String status = details.path("status").asText("SCHEDULED");

                    int capacity = details.path("capacity").asInt(0);
                    int seatsTaken = details.path("seats_taken").asInt(0);

                    return String.format("%s ➔ %s\nStatus: %s  |  Passengers: %d / %d",
                            origin, destination, status, seatsTaken, capacity);
                }

                return "Trip details unavailable.";
            }
        };

        task.setOnSucceeded(e -> {
            Platform.runLater(() -> currentTripLabel.setText(task.getValue()));
        });

        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            Platform.runLater(() -> currentTripLabel.setText("Error loading trip data."));
        });

        new Thread(task).start();
    }

    // --- Navigation Handlers ---

    @FXML
    private void handleDashboard(ActionEvent event) {
        // Already on this page
    }

    @FXML
    private void handleViewTripDetails(ActionEvent event) {
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
}