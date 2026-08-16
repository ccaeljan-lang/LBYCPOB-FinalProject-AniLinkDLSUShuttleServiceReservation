package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.BookingValidationService;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReservationController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final BookingValidationService bookingValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private Label userNameLabel;
    @FXML private ComboBox<String> routeSelection;
    @FXML private DatePicker dateSelection;
    @FXML private ComboBox<String> scheduleSelection;
    @FXML private Label availableSeats;
    @FXML private Label tripStatus;
    @FXML private Button reserveButton;
    @FXML private Label reservationMessage;

    private final Map<String, Route> routeMap = new HashMap<>();
    private final Map<String, TripDetails> tripMap = new HashMap<>();

    private Long selectedTripId = null;
    private int currentCapacity = 0;
    private int currentSeatsTaken = 0;

    public ReservationController(SupabaseService supabaseService,
                                 UserSession userSession,
                                 ViewNavigator viewNavigator,
                                 BookingValidationService bookingValidationService) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
        this.bookingValidationService = bookingValidationService;
    }

    @FXML
    private void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && userNameLabel != null) {
            userNameLabel.setText("Welcome, " + user.getName());
        }

        setupListeners();
        loadRoutesAsync();
    }

    private void setupListeners() {
        routeSelection.valueProperty().addListener((obs, oldVal, newVal) -> {
            dateSelection.setValue(null);
            scheduleSelection.getItems().clear();
            resetTripDetails();
        });

        dateSelection.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && routeSelection.getValue() != null) {
                Route selectedRoute = routeMap.get(routeSelection.getValue());
                if (selectedRoute != null) {
                    loadTripsAsync(selectedRoute.getRouteId(), newVal);
                }
            }
        });

        scheduleSelection.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && tripMap.containsKey(newVal)) {
                TripDetails details = tripMap.get(newVal);
                selectedTripId = details.tripId;
                currentCapacity = details.capacity;
                currentSeatsTaken = details.seatsTaken;

                availableSeats.setText("Available Seats: " + Math.max(0, currentCapacity - currentSeatsTaken));
                tripStatus.setText("Status: " + details.status);
            } else {
                resetTripDetails();
            }
        });
    }

    private void resetTripDetails() {
        selectedTripId = null;
        currentCapacity = 0;
        currentSeatsTaken = 0;
        availableSeats.setText("Available Seats: --");
        tripStatus.setText("Status: --");
        showMessage("", "#334155");
    }

    private void loadRoutesAsync() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String json = supabaseService.getRoutes();
                JsonNode routesArray = objectMapper.readTree(json);

                Platform.runLater(() -> {
                    routeSelection.getItems().clear();
                    routeMap.clear();

                    if (routesArray.isArray()) {
                        for (JsonNode node : routesArray) {
                            Route route = objectMapper.convertValue(node, Route.class);
                            String display = route.getOrigin() + " ↔ " + route.getDestination();

                            routeMap.put(display, route);
                            routeSelection.getItems().add(display);
                        }
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void loadTripsAsync(Long routeId, LocalDate date) {
        scheduleSelection.getItems().clear();
        tripMap.clear();
        resetTripDetails();
        showMessage("Loading schedules...", "#0284C7");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String json = supabaseService.getTripsByRoute(routeId);
                JsonNode tripsArray = objectMapper.readTree(json);

                Platform.runLater(() -> {
                    if (tripsArray.isArray() && !tripsArray.isEmpty()) {
                        for (JsonNode trip : tripsArray) {
                            long id = trip.path("id").asLong();
                            String time = trip.path("schedule").path("departure_time").asText("Unknown Time");
                            int capacity = trip.path("capacity").asInt(0);
                            int seatsTaken = trip.path("seats_taken").asInt(0);
                            String status = trip.path("status").asText("SCHEDULED");

                            String display = "Departure: " + time;
                            tripMap.put(display, new TripDetails(id, capacity, seatsTaken, status));
                            scheduleSelection.getItems().add(display);
                        }
                        showMessage("", "#334155");
                    } else {
                        showMessage("No schedules found for selected route and date.", "#DC2626");
                    }
                });
                return null;
            }
        };

        task.setOnFailed(e -> Platform.runLater(() -> showMessage("Failed to load schedules.", "#DC2626")));
        new Thread(task).start();
    }

    @FXML
    private void handleReserve() {
        // Will be updated in the next commit
        showMessage("Temporary stub", "#334155");
    }

    private void showMessage(String message, String colorCode) {
        reservationMessage.setText(message);
        reservationMessage.setStyle("-fx-text-fill: " + colorCode + "; -fx-font-weight: bold; -fx-font-size: 13px;");
    }

    // Sidebar Action Handlers (Omitted for brevity, unchanged from Commit 1)
    @FXML private void handleHome(ActionEvent event) { viewNavigator.navigateTo(event, "/fxml/PassengerDashboard.fxml", 1000, 650); }
    @FXML private void handleReservations(ActionEvent event) {}
    @FXML private void handleTripHistory(ActionEvent event) { viewNavigator.navigateTo(event, "/fxml/MyReservations.fxml", 1000, 650); }
    @FXML private void handleLogout(ActionEvent event) { userSession.clearSession(); viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600); }

    private static class TripDetails {
        final Long tripId;
        final int capacity;
        final int seatsTaken;
        final String status;

        TripDetails(Long tripId, int capacity, int seatsTaken, String status) {
            this.tripId = tripId;
            this.capacity = capacity;
            this.seatsTaken = seatsTaken;
            this.status = status;
        }
    }
}