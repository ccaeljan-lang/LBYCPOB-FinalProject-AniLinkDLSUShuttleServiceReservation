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
