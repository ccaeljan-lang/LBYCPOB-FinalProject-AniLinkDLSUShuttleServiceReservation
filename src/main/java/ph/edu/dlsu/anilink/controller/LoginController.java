package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

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

        showMessage("Login successful.");
    }

    private boolean isValidDLSUEmail(String email) {
        return email.toLowerCase().endsWith("@dlsu.edu.ph");
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}