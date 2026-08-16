package ph.edu.dlsu.anilink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private final ObservableList<String> lines = FXCollections.observableArrayList();

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
        lineListView.setItems(lines);
    }

    // Existing sync CRUD operations (temporarily kept as local state)
    @FXML
    private void handleAddLine() {
        String lineName = lineNameField.getText().trim();
        String locationA = locationAField.getText().trim();
        String locationB = locationBField.getText().trim();

        if (lineName.isEmpty() || locationA.isEmpty() || locationB.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all fields.");
            return;
        }
        lines.add(lineName + ": " + locationA + " ↔ " + locationB);
        handleClearFields();
        showAlert(Alert.AlertType.INFORMATION, "Line Added", "The line was successfully added.");
    }

    @FXML
    private void handleDeleteLine() {
        String selectedLine = lineListView.getSelectionModel().getSelectedItem();
        if (selectedLine == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a line to delete.");
            return;
        }
        lines.remove(selectedLine);
    }

    @FXML
    private void handleClearFields() {
        if (lineNameField != null) lineNameField.clear();
        locationAField.clear();
        locationBField.clear();
    }

    // --- Navigation Action Handlers ---
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
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}