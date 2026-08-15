package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;

@Controller
public class LoginController {
    private final SupabaseService supabaseService;

    @Autowired
    public LoginController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

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

            User user = supabaseService.findUserByEmail(email);

            if (user == null) {
                showMessage("Invalid email or password.");
                return;
            }

            if (!user.getPassword().equals(password)) {
                showMessage("Invalid email or password.");
                return;
            }

            showMessage(
                    "Login successful. Welcome, "
                            + user.getName()
                            + "!"
            );

            System.out.println(
                    "Logged in as: " + user.getRole()
            );

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Unable to connect to the database."
            );
        }
    }

    private boolean isValidDLSUEmail(String email) {
        return email.toLowerCase().endsWith("@dlsu.edu.ph");
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}