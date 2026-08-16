package ph.edu.dlsu.anilink.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

@Controller
public class PassengerDashboardController {

    private final UserSession userSession;
    private final ViewNavigator viewNavigator;

    @FXML private Label passengerNameLabel;
    @FXML private Label currentTrip;
    @FXML private Label currentStatus;
    @FXML private ListView<?> tripsListView;
    @FXML private Button bookTripButton;
    @FXML private Button logoutButton;

    public PassengerDashboardController(UserSession userSession, ViewNavigator viewNavigator) {
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        // Will be updated to use session
    }
}