package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import java.util.Optional;

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

        // Setup ListView bindings and custom cell formatting
        routeListView.setItems(routes);
        routeListView.setCellFactory(param -> new ListCell<Route>() {
            @Override
            protected void updateItem(Route item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getOrigin() + " ↔ " + item.getDestination());
                }
            }
        });

        loadRoutesAsync();
    }

    private void loadRoutesAsync() {
        Task<List<Route>> task = new Task<>() {
            @Override
            protected List<Route> call() throws Exception {
                String response = supabaseService.getRoutes();
                return objectMapper.readValue(response, new TypeReference<List<Route>>() {});
            }
        };

        task.setOnSucceeded(e -> {
            routes.setAll(task.getValue());
        });

        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load routes from Supabase.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleAddRoute() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();

        if (origin.isEmpty() || destination.isEmpty()) {
            showInlineStatus("Please complete all fields.", "#DC2626"); // Red
            return;
        }

        showInlineStatus("Adding route...", "#0284C7"); // Blue

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                supabaseService.createRoute(origin, destination);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            showInlineStatus("Route added successfully!", "#16A34A"); // Green
            clearFields();
            loadRoutesAsync();
        });

        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            showInlineStatus("Failed to add route.", "#DC2626"); // Red
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to add route to Supabase.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleDeleteRoute() {
        Route selectedRoute = routeListView.getSelectionModel().getSelectedItem();

        if (selectedRoute == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a route from the list to delete.");
            return;
        }

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Route");
        confirm.setContentText("Are you sure you want to delete the route: \n" +
                selectedRoute.getOrigin() + " ↔ " + selectedRoute.getDestination() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    supabaseService.deleteRoute(selectedRoute.getRouteId());
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                showInlineStatus("Route deleted.", "#16A34A");
                loadRoutesAsync();
            });

            task.setOnFailed(e -> {
                e.getSource().getException().printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete route. It may be assigned to an active schedule or trip.");
            });

            new Thread(task).start();
        }
    }

    private void clearFields() {
        originField.clear();
        destinationField.clear();
    }

    private void showInlineStatus(String message, String colorCode) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + colorCode + "; -fx-font-weight: bold; -fx-font-size: 13px;");
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
        // Assuming a consolidated trip/schedule management view exists
        viewNavigator.navigateTo(event, "/fxml/TripManagement.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }
}