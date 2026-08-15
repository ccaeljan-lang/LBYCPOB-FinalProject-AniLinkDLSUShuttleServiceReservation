package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.service.SupabaseService;

import java.util.List;

public class RouteManagementController {
    @FXML
    private TextField routeIdField;

    @FXML
    private TextField originField;

    @FXML
    private TextField destinationField;

    @FXML
    private ListView<Route> routeListView;

    private final ObservableList<Route> routes = FXCollections.observableArrayList();
    private final SupabaseService supabaseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RouteManagementController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @FXML
    public void initialize() {
        routeListView.setItems(routes);
        loadRoutes();
    }

    private void loadRoutes() {
        try {
            String response = supabaseService.getRoutes();

            List<Route> routeList = objectMapper.readValue(
                    response,
                    new TypeReference<List<Route>>() {}
            );

            routes.setAll(routeList);
        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    "Unable to load routes from Supabase."
            );
        }
    }

    @FXML
    private void handleAddRoute() {
        String routeIdText = routeIdField.getText().trim();
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();

        if (routeIdText.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please complete all fields."
            );
            return;
        }

        try {
            Long routeId = Long.parseLong(routeIdText);

            Route route = new Route(
                    routeId,
                    origin,
                    destination
            );

            supabaseService.createRoute(route);
            routes.add(route);
            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Route Added",
                    "Route was successfully added."
            );
        } catch (NumberFormatException e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Route ID",
                    "Route ID must be a valid number."
            );
        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    "Unable to add route to Supabase."
            );
        }
    }

    @FXML
    private void handleDeleteRoute() {
        Route selectedRoute = routeListView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedRoute == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a route."
            );
            return;
        }

        try {
            supabaseService.deleteRoute(selectedRoute.getRouteId());
            routes.remove(selectedRoute);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Route Deleted",
                    "Route was successfully deleted."
            );
        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    "Unable to delete route from Supabase."
            );
        }
    }

    private void clearFields() {
        routeIdField.clear();
        originField.clear();
        destinationField.clear();
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}