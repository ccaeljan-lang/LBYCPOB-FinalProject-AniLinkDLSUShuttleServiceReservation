package ph.edu.dlsu.anilink.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.service.SupabaseService;

@Controller
public class RegisterController {

    private final SupabaseService supabaseService;
    private final ApplicationContext applicationContext;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Button backToLoginButton;
    @FXML private Label messageLabel;

    @Autowired
    public RegisterController(SupabaseService supabaseService, ApplicationContext applicationContext) {
        this.supabaseService = supabaseService;
        this.applicationContext = applicationContext;
    }

    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "STUDENT", "ADMIN", "DRIVER"
        ));
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String selection = categoryComboBox.getValue();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selection == null) {
            showMessage("Please fill in all required fields.", true);
            return;
        }

        if (!email.toLowerCase().endsWith("@dlsu.edu.ph")) {
            showMessage("Please use a valid DLSU email address.", true);
            return;
        }

        try {
            // Temporarily still using the old method name
            supabaseService.registerPassenger(name, email, password, selection);
            showMessage("Account created successfully!", false);
            handleGoToLogin();
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            e.printStackTrace();
            System.err.println("Supabase Error Response: " + e.getResponseBodyAsString());
            showMessage("Server Error (" + e.getStatusCode().value() + "): " + e.getResponseBodyAsString(), true);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleGoToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error returning to login screen.", true);
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}