package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private TextField scheduleIdField; // Kept temporarily for old sync logic
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

        loadRoutes();
        loadSchedules();
    }

    private void setupCustomFormatters() {
        StringConverter<Route> routeConverter = new StringConverter<>() {
            @Override
            public String toString(Route route) {
                return (route != null) ? route.getOrigin() + " ↔ " + route.getDestination() : "";
            }
            @Override
            public Route fromString(String string) { return null; }
        };

        routeComboBox.setConverter(routeConverter);
        routeComboBox.setCellFactory(param -> new ListCell<>() {
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

        scheduleListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DepartureSchedule item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String routeName = (item.getRoute() != null) ?
                            item.getRoute().getOrigin() + " ↔ " + item.getRoute().getDestination() : "Unknown Route";
                    setText(String.format("[%s]  %s  |  Capacity: %d",
                            item.getDepartureTime().toString(), routeName, item.getCapacity()));
                }
            }
        });
    }

    // Temporary old synchronous read methods
    private void loadRoutes() {
        try {
            String response = supabaseService.getRoutes();
            List<Route> routeList = objectMapper.readValue(response, new TypeReference<List<Route>>() {});
            routeComboBox.getItems().setAll(routeList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load routes from Supabase.");
        }
    }

    private void loadSchedules() {
        try {
            String response = supabaseService.getSchedules();
            List<DepartureSchedule> scheduleList = objectMapper.readValue(response, new TypeReference<List<DepartureSchedule>>() {});
            schedules.setAll(scheduleList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load schedules from Supabase.");
        }
    }

    // Temporary old synchronous add method
    @FXML
    private void handleAddSchedule() {
        String scheduleIdText = scheduleIdField.getText().trim();
        Route route = routeComboBox.getValue();
        String departureTimeText = departureTimeField.getText().trim();
        String capacityText = capacityField.getText().trim();

        if (scheduleIdText.isEmpty() || route == null || departureTimeText.isEmpty() || capacityText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please complete all fields.");
            return;
        }
        try {
            Long scheduleId = Long.parseLong(scheduleIdText);
            LocalTime departureTime = LocalTime.parse(departureTimeText, DateTimeFormatter.ofPattern("HH:mm"));
            int capacity = Integer.parseInt(capacityText);

            if (capacity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Capacity", "Capacity must be greater than zero.");
                return;
            }
            DepartureSchedule schedule = new DepartureSchedule(scheduleId, route, departureTime, capacity);
            supabaseService.createSchedule(schedule);
            schedules.add(schedule);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Schedule Added", "Departure schedule was successfully added.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Schedule ID and capacity must be valid numbers.");
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Time", "Please use the format HH:mm.\nExample: 08:30");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to save schedule to Supabase.");
        }
    }

    // Temporary old synchronous delete method
    @FXML
    private void handleDeleteSchedule() {
        DepartureSchedule selectedSchedule = scheduleListView.getSelectionModel().getSelectedItem();
        if (selectedSchedule == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a schedule to delete.");
            return;
        }
        try {
            supabaseService.deleteSchedule(selectedSchedule.getScheduleId());
            schedules.remove(selectedSchedule);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete schedule from Supabase.");
        }
    }

    private void clearFields() {
        scheduleIdField.clear();
        departureTimeField.clear();
        capacityField.clear();
        routeComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Sidebar Navigation Handlers ---

    @FXML private void handleDashboard(ActionEvent event) { viewNavigator.navigateTo(event, "/fxml/AdminDashboard.fxml", 1000, 650); }
    @FXML private void handleRouteManagement(ActionEvent event) { viewNavigator.navigateTo(event, "/fxml/RouteManagement.fxml", 1000, 650); }
    @FXML private void handleScheduleManagement(ActionEvent event) { /* Already here */ }
    @FXML private void handleTripManagement(ActionEvent event) { viewNavigator.navigateTo(event, "/fxml/TripManagement.fxml", 1000, 650); }
    @FXML private void handleLogout(ActionEvent event) { userSession.clearSession(); viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600); }
}