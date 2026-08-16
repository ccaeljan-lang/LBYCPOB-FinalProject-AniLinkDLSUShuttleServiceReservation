package ph.edu.dlsu.anilink.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.QRService;
import ph.edu.dlsu.anilink.util.UserSession;
import ph.edu.dlsu.anilink.util.ViewNavigator;

import java.awt.image.BufferedImage;

@Controller
public class QRCodeController {

    private final QRService qrService;
    private final UserSession userSession;
    private final ViewNavigator viewNavigator;

    @FXML private Label userNameLabel;
    @FXML private ImageView qrCode;
    @FXML private Label reservationCode;
    @FXML private Label passengerName;
    @FXML private Label route;
    @FXML private Label departureTime;
    @FXML private Label status;
    @FXML private Button back;

    private Reservation reservation;

    public QRCodeController(QRService qrService,
                            UserSession userSession,
                            ViewNavigator viewNavigator) {
        this.qrService = qrService;
        this.userSession = userSession;
        this.viewNavigator = viewNavigator;
    }

    @FXML
    private void initialize() {
        User user = userSession.getCurrentUser();
        if (user != null && userNameLabel != null) {
            userNameLabel.setText("Welcome, " + user.getName());
        }
    }

    // Temporary old sync logic
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        if (reservation == null) {
            return;
        }

        reservationCode.setText("Reservation Code: " + reservation.getReservationId());
        passengerName.setText("Passenger: " + reservation.getPassenger().getName());
        route.setText("Route: " + reservation.getTrip().getRoute());
        departureTime.setText("Departure: " + reservation.getTrip().getSchedule());
        status.setText("Status: " + reservation.getStatus());

        generateQRCode();
    }

    private void generateQRCode() {
        if (reservation == null) {
            status.setText("Status: No reservation selected");
            return;
        }

        BufferedImage bufferedImage = qrService.generateQRCode(reservation);

        if (bufferedImage == null) {
            status.setText("Status: QR generation failed");
            return;
        }

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        qrCode.setImage(image);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Returning to reservations...");
    }
}