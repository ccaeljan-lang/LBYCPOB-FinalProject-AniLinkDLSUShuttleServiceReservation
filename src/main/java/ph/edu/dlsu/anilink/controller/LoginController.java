package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;

@Controller
public class LoginController {
    private final SupabaseService supabaseService;
    private final ApplicationContext applicationContext;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @Autowired
    public LoginController(SupabaseService supabaseService, ApplicationContext applicationContext) {
        this.supabaseService = supabaseService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void handleLogin() {

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please enter your email and password.");
            return;
        }

        if (!isValidDLSUEmail(email)) {
            showMessage("Please use a valid DLSU email.");
            return;
        }

        try {
            // Fetch the real user from Supabase
            User user = supabaseService.findUserByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                showMessage("Invalid email or password.");
                return;
            }

            System.out.println("Logged in as: " + user.getRole());

            // Switch to dashboard
            goToDashboard(user);

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Unable to connect to the database.");
        }
    }

    private void goToDashboard(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PassengerDashboard.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent root = fxmlLoader.load();

            // Pass the user's name to the dashboard
            PassengerDashboardController dashboardController = fxmlLoader.getController();
            dashboardController.setPassengerName("Welcome, " + user.getName());

            // Swap the scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 650));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading dashboard.");
        }
    }

    private boolean isValidDLSUEmail(String email) {
        return email.toLowerCase().endsWith("@dlsu.edu.ph");
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}