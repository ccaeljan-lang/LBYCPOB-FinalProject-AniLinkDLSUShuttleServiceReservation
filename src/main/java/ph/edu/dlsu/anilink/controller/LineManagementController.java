package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX controller for the Line (Route) Management view.
 * <p>
 * This class provides an administrative interface for managing transportation routes.
 * It allows administrators to asynchronously load, add, and delete routes containing
 * origin and destination locations via the {@link SupabaseService}. The controller
 * ensures that long-running database operations do not block the UI by using background tasks,
 * and handles view transitions using the {@link ViewNavigator}.
 * </p>
 */
@Controller
public class LineManagementController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private Label adminNameLabel;
    @FXML private TextField lineNameField;
    @FXML private TextField locationAField;
    @FXML private TextField locationBField;
    @FXML private ListView<String> lineListView;

    private final ObservableList<String> displayLines = FXCollections.observableArrayList();
    private final List<Long> routeIds = new ArrayList<>();

    public LineManagementController(SupabaseService supabaseService,
                                    UserSession userSession,
                                    ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    public void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && adminNameLabel != null) {
            adminNameLabel.setText("Welcome, " + user.getName());
        }

        lineListView.setItems(displayLines);
        loadRoutesAsync();
    }

    private void loadRoutesAsync() {
        Task<List<RouteDisplayItem>> task = new Task<>() {
            @Override
            protected List<RouteDisplayItem> call() throws Exception {
                List<RouteDisplayItem> items = new ArrayList<>();
                String json = supabaseService.getRoutes();
                JsonNode routesArray = objectMapper.readTree(json);

                if (routesArray.isArray()) {
                    for (JsonNode route : routesArray) {
                        long id = route.path("id").asLong();
                        String origin = route.path("origin").asText();
                        String destination = route.path("destination").asText();
                        items.add(new RouteDisplayItem(id, String.format("Route #%d: %s ↔ %s", id, origin, destination)));
                    }
                }
                return items;
            }
        };

        task.setOnSucceeded(e -> {
            displayLines.clear();
            routeIds.clear();
            for (RouteDisplayItem item : task.getValue()) {
                displayLines.add(item.displayText);
                routeIds.add(item.id);
            }
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load routes from Supabase.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAddLine() {
        String locationA = locationAField.getText().trim();
        String locationB = locationBField.getText().trim();

        if (locationA.isEmpty() || locationB.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in Origin (Location A) and Destination (Location B).");
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                supabaseService.createRoute(locationA, locationB);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            loadRoutesAsync();
            handleClearFields();
            showAlert(Alert.AlertType.INFORMATION, "Line Added", "The route was successfully created in the database.");
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save the new route.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleDeleteLine() {
        int selectedIndex = lineListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0 || selectedIndex >= routeIds.size()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a route to delete.");
            return;
        }

        Long selectedRouteId = routeIds.get(selectedIndex);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                supabaseService.deleteRoute(selectedRouteId);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            loadRoutesAsync();
            showAlert(Alert.AlertType.INFORMATION, "Line Deleted", "The route was removed successfully.");
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Delete Failed", "Cannot delete route. It may be linked to active schedules or trips.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleClearFields() {
        if (lineNameField != null) lineNameField.clear();
        locationAField.clear();
        locationBField.clear();
    }

    // Navigation Action Handlers
    @FXML
    private void handleDashboard(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/AdminDashboard.fxml", 1000, 650);
    }

    @FXML
    private void handleLineManagement(ActionEvent event) {
        // Already on Line Management view
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
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

    private static class RouteDisplayItem {
        final long id;
        final String displayText;

        RouteDisplayItem(long id, String displayText) {
            this.id = id;
            this.displayText = displayText;
        }
    }
}