package ph.edu.dlsu.anilink.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.service.QRService;

import java.awt.image.BufferedImage;

@Controller
public class QRCodeController {
    @FXML
    private ImageView qrCode;

    @FXML
    private Label reservationCode;

    @FXML
    private Label passengerName;

    @FXML
    private Label route;

    @FXML
    private Label departureTime;

    @FXML
    private Label status;

    @FXML
    private Button back;

    private QRService qrService;
    private Reservation reservation;

    @FXML
    private void initialize() {
        qrService = new QRService();
    }

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
    private void handleBack() {
        System.out.println("Returning to reservations...");
    }
}