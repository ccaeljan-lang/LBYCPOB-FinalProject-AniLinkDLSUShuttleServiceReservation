package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.DepartureSchedule;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class ScheduleManagementController {

    @FXML private Label adminNameLabel;
    @FXML private TextField scheduleIdField; // Kept temporarily
    @FXML private ComboBox<Route> routeComboBox;
    @FXML private TextField departureTimeField;
    @FXML private TextField capacityField;
    @FXML private ListView<DepartureSchedule> scheduleListView;
    @FXML private Label statusLabel;

    private final ObservableList<DepartureSchedule> schedules = FXCollections.observableArrayList();

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;
    private final ObjectMapper objectMapper;

    public ScheduleManagementController(SupabaseService supabaseService,
                                        UserSession userSession,
                                        ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @FXML
    public void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && adminNameLabel != null) {
            adminNameLabel.setText("Welcome, " + user.getName());
        }

        setupCustomFormatters();
        scheduleListView.setItems(schedules);

        loadRoutesAsync();
        loadSchedulesAsync();
    }

    private void setupCustomFormatters() {
        // [Omitted for brevity - exactly same as Commit 1]
    }

    private void loadRoutesAsync() {
        Task<List<Route>> task = new Task<>() {
            @Override
            protected List<Route> call() throws Exception {
                String response = supabaseService.getRoutes();
                return objectMapper.readValue(response, new TypeReference<List<Route>>() {});
            }
        };

        task.setOnSucceeded(e -> routeComboBox.getItems().setAll(task.getValue()));
        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load routes.");
        });

        new Thread(task).start();
    }

    private void loadSchedulesAsync() {
        Task<List<DepartureSchedule>> task = new Task<>() {
            @Override
            protected List<DepartureSchedule> call() throws Exception {
                String response = supabaseService.getSchedules();
                return objectMapper.readValue(response, new TypeReference<List<DepartureSchedule>>() {});
            }
        };

        task.setOnSucceeded(e -> schedules.setAll(task.getValue()));
        task.setOnFailed(e -> {
            e.getSource().getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load schedules.");
        });

        new Thread(task).start();
    }

    @FXML
    private void handleRouteSelection() {
        Route selectedRoute = routeComboBox.getValue();
        if (selectedRoute == null) {
            loadSchedulesAsync(); // Load all if cleared
            return;
        }

        Task<List<DepartureSchedule>> task = new Task<>() {
            @Override
            protected List<DepartureSchedule> call() throws Exception {
                String response = supabaseService.getSchedulesByRoute(selectedRoute.getRouteId());
                return objectMapper.readValue(response, new TypeReference<List<DepartureSchedule>>() {});
            }
        };

        task.setOnSucceeded(e -> schedules.setAll(task.getValue()));
        task.setOnFailed(e -> e.getSource().getException().printStackTrace());

        new Thread(task).start();
    }

    // Add Schedule, Delete Schedule, clearFields, showAlert, and Navigation methods remain unchanged
    // ... [Omitted for brevity] ...
}