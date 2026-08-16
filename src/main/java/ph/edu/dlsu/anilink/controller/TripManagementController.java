package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.DepartureSchedule;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.util.List;

@Controller
public class TripManagementController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper;

    @FXML private Label adminNameLabel;
    @FXML private ComboBox<Route> routeComboBox;
    @FXML private ComboBox<DepartureSchedule> scheduleComboBox;
    @FXML private ComboBox<User> driverComboBox;
    @FXML private TextField capacityField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<User> updateDriverComboBox;
    @FXML private ComboBox<String> updateStatusComboBox;
    @FXML private ListView<Trip> tripListView;

    private final ObservableList<Trip> trips = FXCollections.observableArrayList();
    private final ObservableList<String> statuses = FXCollections.observableArrayList(
            "SCHEDULED", "ARRIVING", "BOARDING", "IN_TRANSIT", "COMPLETED", "CANCELLED"
    );

    public TripManagementController(SupabaseService supabaseService, UserSession userSession, ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;

        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @FXML
    public void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && adminNameLabel != null) {
            adminNameLabel.setText("Welcome, " + user.getName());
        }

        tripListView.setItems(trips);

        statusComboBox.setItems(statuses);
        statusComboBox.setValue("SCHEDULED");
        updateStatusComboBox.setItems(statuses);

        setupDriverComboBoxConverters();
    }

    private void setupDriverComboBoxConverters() {
        StringConverter<User> driverConverter = new StringConverter<>() {
            @Override
            public String toString(User user) {
                return (user != null) ? user.getName() : "";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        };

        driverComboBox.setConverter(driverConverter);
        updateDriverComboBox.setConverter(driverConverter);
    }

    // Temporary Stubs
    @FXML private void handleRouteSelection() {}
    @FXML private void handleAddTrip() {}
    @FXML private void handleAssignDriver() {}
    @FXML private void handleUpdateStatus() {}
    @FXML private void handleDeleteTrip() {}

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // --- Navigation Handlers ---

    @FXML
    private void handleDashboard(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/AdminDashboard.fxml", 1100, 700);
    }

    @FXML
    private void handleRouteManagement(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/RouteManagement.fxml", 1100, 700);
    }

    @FXML
    private void handleScheduleManagement(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/ScheduleManagement.fxml", 1100, 700);
    }

    @FXML
    private void handleTripManagement(ActionEvent event) {
        // Already on this page
    }

    @FXML
    private void handleRoutes(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/RouteManagement.fxml", 1100, 700);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }
}