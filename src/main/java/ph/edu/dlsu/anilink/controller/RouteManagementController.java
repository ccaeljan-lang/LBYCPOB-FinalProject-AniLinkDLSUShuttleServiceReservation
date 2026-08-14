package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import ph.edu.dlsu.anilink.model.Route;

public class RouteManagementController {

    @FXML
    private TextField routeIdField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField lineField;

    @FXML
    private TextField originField;

    @FXML
    private TextField destinationField;

    @FXML
    private ListView<Route> routeListView;

    private final ObservableList<Route> routes =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        routeListView.setItems(routes);
    }

    @FXML
    private void handleAddRoute() {

        String routeId =
                routeIdField.getText().trim();

        String category =
                categoryField.getText().trim();

        String line =
                lineField.getText().trim();

        String origin =
                originField.getText().trim();

        String destination =
                destinationField.getText().trim();

        if (routeId.isEmpty()
                || category.isEmpty()
                || line.isEmpty()
                || origin.isEmpty()
                || destination.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please complete all fields."
            );

            return;
        }

        Route route =
                new Route(
                        routeId,
                        category,
                        line,
                        origin,
                        destination
                );

        routes.add(route);

        clearFields();

        showAlert(
                Alert.AlertType.INFORMATION,
                "Route Added",
                "Route was successfully added."
        );
    }

    @FXML
    private void handleDeleteRoute() {

        Route selectedRoute =
                routeListView.getSelectionModel()
                        .getSelectedItem();

        if (selectedRoute == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a route."
            );

            return;
        }

        routes.remove(selectedRoute);
    }

    private void clearFields() {

        routeIdField.clear();
        categoryField.clear();
        lineField.clear();
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
