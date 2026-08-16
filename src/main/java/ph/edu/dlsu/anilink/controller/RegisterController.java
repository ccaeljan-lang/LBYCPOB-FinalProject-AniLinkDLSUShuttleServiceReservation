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

    // Determine the actual database Role based on the dropdown selection
    String dbRole;
    if ("ADMIN".equals(selection)) {
        dbRole = "ADMINISTRATOR";
    } else if ("DRIVER".equals(selection)) {
        dbRole = "DRIVER";
    } else {
        dbRole = "PASSENGER"; // STUDENT defaults to PASSENGER
    }

    try {
        // Still using the old method signature temporarily
        supabaseService.registerPassenger(name, email, password, dbRole);
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