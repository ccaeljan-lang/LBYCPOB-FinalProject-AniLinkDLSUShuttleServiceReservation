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
import java.util.Optional;

@Controller
public class ScheduleManagementController {

    @FXML private Label adminNameLabel;
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
        // Load Admin details
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
        // Formatter for Route ComboBox
        StringConverter<Route> routeConverter = new StringConverter<>() {
            @Override
            public String toString(Route route) {
                return (route != null) ? route.getOrigin() + " ↔ " + route.getDestination() : "";
            }

            @Override
            public Route fromString(String string) {
                return null; // Not needed
            }
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

        // Formatter for Schedule ListView
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
    private void handleAddSchedule() {
        Route route = routeComboBox.getValue();
        String departureTimeText = departureTimeField.getText().trim();
        String capacityText = capacityField.getText().trim();

        if (route == null || departureTimeText.isEmpty() || capacityText.isEmpty()) {
            showInlineStatus("Please fill in all fields.", "#DC2626");
            return;
        }

        try {
            // Use H:mm so it accepts both "8:30" and "08:30"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime parsedTime = LocalTime.parse(departureTimeText, inputFormatter);

            // Format to exact PostgreSQL standard: "HH:mm:ss"
            String dbFormattedTime = parsedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            int capacity = Integer.parseInt(capacityText);
            if (capacity <= 0) {
                showInlineStatus("Capacity must be > 0.", "#DC2626");
                return;
            }

            showInlineStatus("Adding schedule...", "#0284C7");

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    // Send the safely formatted HH:mm:ss string to Supabase
                    supabaseService.createSchedule(route.getRouteId(), dbFormattedTime, capacity);
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                showInlineStatus("Schedule created successfully!", "#16A34A");
                clearFields();
                loadSchedulesAsync();
            });

            task.setOnFailed(e -> {
                Throwable ex = e.getSource().getException();
                ex.printStackTrace();

                showInlineStatus("Failed to create schedule.", "#DC2626");

                // Show the actual error message from Supabase to help with debugging
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Supabase rejected the request. Reason:\n" + ex.getMessage());
            });

            new Thread(task).start();

        } catch (NumberFormatException e) {
            showInlineStatus("Capacity must be a number.", "#DC2626");
        } catch (DateTimeParseException e) {
            showInlineStatus("Use 24h time format (e.g., 08:30 or 14:00).", "#DC2626");
        }
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
                // Call Supabase endpoint filtering by selectedRoute.getRouteId()
                String response = supabaseService.getSchedulesByRoute(selectedRoute.getRouteId());
                return objectMapper.readValue(response, new TypeReference<List<DepartureSchedule>>() {});
            }
        };

        task.setOnSucceeded(e -> schedules.setAll(task.getValue()));
        task.setOnFailed(e -> e.getSource().getException().printStackTrace());

        new Thread(task).start();
    }

    @FXML
    private void handleDeleteSchedule() {
        DepartureSchedule selectedSchedule = scheduleListView.getSelectionModel().getSelectedItem();

        if (selectedSchedule == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a schedule to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Schedule");
        confirm.setContentText("Are you sure you want to delete this schedule?\n" +
                "Time: " + selectedSchedule.getDepartureTime());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    supabaseService.deleteSchedule(selectedSchedule.getScheduleId());
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                showInlineStatus("Schedule deleted.", "#16A34A");
                loadSchedulesAsync();
            });

            task.setOnFailed(e -> {
                e.getSource().getException().printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete schedule. It may be linked to active trips.");
            });

            new Thread(task).start();
        }
    }

    private void clearFields() {
        departureTimeField.clear();
        capacityField.clear();
        routeComboBox.getSelectionModel().clearSelection();
    }

    private void showInlineStatus(String message, String colorCode) {
        Platform.runLater(() -> {
            statusLabel.setText(message);
            statusLabel.setStyle("-fx-text-fill: " + colorCode + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        });
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
        viewNavigator.navigateTo(event, "/fxml/RouteManagement.fxml", 1000, 650);
    }

    @FXML
    private void handleScheduleManagement(ActionEvent event) {
        // Already on Trips & Schedules Management
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