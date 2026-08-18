package ph.edu.dlsu.anilink.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

/**
 * JavaFX controller for the Passenger Dashboard view.
 * <p>
 * This class manages the main landing interface for passenger users. It retrieves
 * the currently logged-in user from the {@link UserSession} to display a personalized
 * welcome message and provides an overview of the user's trip status. Additionally,
 * it handles user interactions and navigation to other passenger-specific modules,
 * such as booking reservations, viewing reservation history, and logging out, using
 * the {@link ViewNavigator}.
 * </p>
 */
@Controller
public class PassengerDashboardController {

    private final UserSession userSession;
    private final ViewNavigator viewNavigator;

    @FXML private Label passengerNameLabel;
    @FXML private Label currentTrip;
    @FXML private Label currentStatus;
    @FXML private ListView<?> tripsListView;
    @FXML private Button bookTripButton;
    @FXML private Button logoutButton;

    public PassengerDashboardController(UserSession userSession, ViewNavigator viewNavigator) {
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && passengerNameLabel != null) {
            passengerNameLabel.setText("Welcome, " + user.getName());
        }
    }

    // Navigation Handlers
    @FXML
    private void handleHome(ActionEvent event) {
        // Already on Home/Dashboard view
    }

    @FXML
    private void handleReservations(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/Reservation.fxml", 1000, 650);
    }

    @FXML
    private void handleBookSelectedTrip(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/MyReservations.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }
}