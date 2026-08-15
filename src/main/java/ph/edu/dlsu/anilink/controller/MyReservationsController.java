package ph.edu.dlsu.anilink.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

@Controller
public class MyReservationsController {

    @FXML
    private TableView<?> reservationTable;

    @FXML
    private TableColumn<?, ?> reservationCodeColumn;

    @FXML
    private TableColumn<?, ?> routeColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> departureColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private void initialize() {
        loadReservations();
    }

    private void loadReservations() {
        reservationTable.getItems().clear();
    }
}