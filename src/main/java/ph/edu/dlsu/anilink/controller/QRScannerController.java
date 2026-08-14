package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class QRScannerController {

    @FXML
    private Label scanResultLabel;

    @FXML
    private Button simulateScanButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        scanResultLabel.setText("Awaiting scan...");
    }

    @FXML
    private void handleSimulateScan() {
        scanResultLabel.setText("Scan successful: Passenger Verified.");
        System.out.println("Processing QR payload...");
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to driver dashboard...");
    }
}