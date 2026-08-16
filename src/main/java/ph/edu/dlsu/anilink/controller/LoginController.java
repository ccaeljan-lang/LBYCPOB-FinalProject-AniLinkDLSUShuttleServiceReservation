package ph.edu.dlsu.anilink.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class LoginController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    public LoginController(SupabaseService supabaseService, UserSession userSession, ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter your email and password.");
            return;
        }

        if (!isValidDLSUEmail(email)) {
            messageLabel.setText("Please use a valid DLSU email.");
            return;
        }

        try {
            User user = supabaseService.findUserByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                messageLabel.setText("Invalid email or password.");
                return;
            }

            // Temporary direct routing using the new ViewNavigator
            viewNavigator.navigateTo(event, "/fxml/PassengerDashboard.fxml", 1000, 650);

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Unable to connect to the database.");
        }
    }

    private boolean isValidDLSUEmail(String email) {
        return email.toLowerCase().endsWith("@dlsu.edu.ph");
    }
}