package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.util.List;

@Controller
public class RouteManagementController {

    @FXML private Label adminNameLabel;
    @FXML private TextField originField;
    @FXML private TextField destinationField;
    @FXML private ListView<Route> routeListView;
    @FXML private Label statusLabel;

    private final ObservableList<Route> routes = FXCollections.observableArrayList();

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RouteManagementController(SupabaseService supabaseService,
                                     UserSession userSession,
                                     ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    public void initialize() {
        // Load User details
        User user = userSession.getCurrentUser();
        if (user != null && adminNameLabel != null) {
            adminNameLabel.setText("Welcome, " + user.getName());
        }

        routeListView.setItems(routes);
        loadRoutes();
    }

    // Temporary old synchronous load method
    private void loadRoutes() {
        try {
            String response = supabaseService.getRoutes();
            List<Route> routeList = objectMapper.readValue(
                    response,
                    new TypeReference<List<Route>>() {}
            );
            routes.setAll(routeList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load routes from Supabase.");
        }
    }

    // Temporary old synchronous add method
    @FXML
    private void handleAddRoute() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();

        if (origin.isEmpty() || destination.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please complete all fields.");
            return;
        }

        try {
            Route route = new Route(origin, destination);
            supabaseService.createRoute(origin, destination);
            routes.add(route);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Route Added", "Route was successfully added.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to add route to Supabase.");
        }
    }

    // Temporary old synchronous delete method
    @FXML
    private void handleDeleteRoute() {
        Route selectedRoute = routeListView.getSelectionModel().getSelectedItem();

        if (selectedRoute == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a route.");
            return;
        }

        try {
            supabaseService.deleteRoute(selectedRoute.getRouteId());
            routes.remove(selectedRoute);
            showAlert(Alert.AlertType.INFORMATION, "Route Deleted", "Route was successfully deleted.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete route from Supabase.");
        }
    }

    private void clearFields() {
        originField.clear();
        destinationField.clear();
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

    // --- Sidebar Navigation Handlers ---

    @FXML
    private void handleDashboard(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/AdminDashboard.fxml", 1000, 650);
    }

    @FXML
    private void handleRouteManagement(ActionEvent event) {
        // Already on Route Management
    }

    @FXML
    private void handleScheduleManagement(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/ScheduleManagement.fxml", 1100, 700);
    }

    @FXML
    private void handleTripManagement(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/TripManagement.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }
}