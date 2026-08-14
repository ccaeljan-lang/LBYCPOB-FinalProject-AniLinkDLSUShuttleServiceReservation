package ph.edu.dlsu.anilink.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.service.QRService;

import java.awt.image.BufferedImage;

public class QRCodeController {

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Label reservationIdLabel;

    @FXML
    private Label tripDetailsLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button generateButton;

    @FXML
    private Button backButton;

    private QRService qrService;
    private Reservation reservation;

    @FXML
    private void initialize() {
        qrService = new QRService();
        statusLabel.setText("");
    }

    @FXML
    private void handleGenerateQR() {
        if (reservation == null) {
            showStatus("No reservation selected.");
            return;
        }

        BufferedImage bufferedImage = qrService.generateQRCode(reservation);

        if (bufferedImage == null) {
            showStatus("Unable to generate QR code.");
            return;
        }

        Image qrImage = SwingFXUtils.toFXImage(bufferedImage, null);

        qrCodeImageView.setImage(qrImage);
        showStatus("QR code generated successfully.");
    }

    @FXML
    private void handleBack() {
        System.out.println("Returning to reservations...");
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        if (reservation != null) {
            reservationIdLabel.setText("Reservation: " + reservation.getReservationId());
            tripDetailsLabel.setText("Trip: " + reservation.getTrip());
        }
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
    }
}