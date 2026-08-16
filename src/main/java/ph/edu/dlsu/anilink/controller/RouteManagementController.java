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

    // Add Route, Delete Route, clearFields, showAlert, and Navigation methods remain unchanged from Commit 1

    // ... [Omitted for brevity] ...
}