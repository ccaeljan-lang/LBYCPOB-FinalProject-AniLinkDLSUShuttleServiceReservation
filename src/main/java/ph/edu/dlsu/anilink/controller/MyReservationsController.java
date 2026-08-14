package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MyReservationsController {

    @FXML
    private ListView<String> reservationList;

    @FXML
    private Label reservationDetailsLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Button viewButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        loadReservations();
        messageLabel.setText("");
        reservationDetailsLabel.setText("Select a reservation to view its details.");
    }

    private void loadReservations() {
        reservationList.getItems().clear();

        reservationList.getItems().addAll(
                "Manila → Laguna | August 15, 2026 | 7:00 AM",
                "Laguna → Manila | August 17, 2026 | 5:00 PM"
        );
    }

    @FXML
    private void handleViewReservation() {
        String selectedReservation = reservationList.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showMessage("Please select a reservation.");
            return;
        }

        reservationDetailsLabel.setText("Reservation Details:\n" + selectedReservation + "\nStatus: CONFIRMED");
        showMessage("");
    }

    @FXML
    private void handleCancelReservation() {
        String selectedReservation = reservationList.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showMessage("Please select a reservation.");
            return;
        }

        reservationList.getItems().remove(selectedReservation);
        reservationDetailsLabel.setText("Select a reservation to view its details.");

        showMessage("Reservation cancelled successfully.");
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to passenger dashboard...");
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}