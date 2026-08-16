package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class QRScannerController {

    private final SupabaseService supabaseService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;

    @FXML private Label driverNameLabel;
    @FXML private Label scanResultLabel;
    @FXML private Button simulateScanButton;
    @FXML private Button backButton;

    public QRScannerController(SupabaseService supabaseService,
                               UserSession userSession,
                               ViewNavigator viewNavigator) {
        this.supabaseService = supabaseService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && driverNameLabel != null) {
            driverNameLabel.setText("Welcome, " + user.getName());
        }
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