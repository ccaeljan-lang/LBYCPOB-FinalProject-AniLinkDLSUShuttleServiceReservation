package ph.edu.dlsu.anilink.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.BookingValidationService;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.time.LocalDate;

@Controller
public class ReservationController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final BookingValidationService bookingValidationService;

    @FXML private Label userNameLabel;
    @FXML private ComboBox<String> routeSelection;
    @FXML private DatePicker dateSelection;
    @FXML private ComboBox<String> scheduleSelection;
    @FXML private Label availableSeats;
    @FXML private Label tripStatus;
    @FXML private Button reserveButton;
    @FXML private Label reservationMessage;

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

        loadRoutes();
        loadSchedules();
        availableSeats.setText("Available Seats: --");
        tripStatus.setText("Trip Status: --");
        reservationMessage.setText("");
    }

    // Temporary static data loader
    private void loadRoutes() {
        routeSelection.getItems().addAll("Manila → Laguna", "Laguna → Manila");
    }

    // Temporary static data loader
    private void loadSchedules() {
        scheduleSelection.getItems().addAll("7:00 AM", "9:00 AM", "11:00 AM", "1:00 PM", "3:00 PM", "5:00 PM");
    }

    @FXML
    private void handleRouteSelection() {
        if (routeSelection.getValue() == null) {
            availableSeats.setText("Available Seats: --");
            tripStatus.setText("Trip Status: --");
            return;
        }
        availableSeats.setText("Available Seats: 30");
        tripStatus.setText("Trip Status: Available");
    }

    @FXML
    private void handleReserve() {
        String route = routeSelection.getValue();
        LocalDate date = dateSelection.getValue();
        String schedule = scheduleSelection.getValue();

        if (route == null || date == null || schedule == null) {
            showMessage("Please fill in all fields.");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showMessage("Please select a valid date.");
            return;
        }

        showMessage("Reservation request submitted.");
    }

    private void showMessage(String message) {
        reservationMessage.setText(message);
    }

    // Sidebar Action Handlers
    @FXML
    private void handleHome(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/PassengerDashboard.fxml", 1000, 650);
    }

    @FXML
    private void handleReservations(ActionEvent event) {
        // Already here
    }

    @FXML
    private void handleTripHistory(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/MyReservations.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }
}