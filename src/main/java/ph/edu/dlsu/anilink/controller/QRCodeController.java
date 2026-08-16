package ph.edu.dlsu.anilink.controller;

import javafx.concurrent.Task;
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

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;

        if (reservation == null) {
            status.setText("No reservation selected");
            return;
        }

        reservationCode.setText("Reservation Code: " + (reservation.getReservationId() != null ? reservation.getReservationId() : "N/A"));

        if (reservation.getPassenger() != null) {
            passengerName.setText("Passenger: " + reservation.getPassenger().getName());
        } else {
            passengerName.setText("Passenger: Unknown");
        }

        if (reservation.getTrip() != null && reservation.getTrip().getRoute() != null) {
            String origin = reservation.getTrip().getRoute().getOrigin();
            String destination = reservation.getTrip().getRoute().getDestination();
            route.setText("Route: " + origin + " ↔ " + destination);
        } else {
            route.setText("Route: N/A");
        }

        if (reservation.getTrip() != null && reservation.getTrip().getSchedule() != null) {
            departureTime.setText("Departure: " + reservation.getTrip().getSchedule().getDepartureTime());
        } else {
            departureTime.setText("Departure: N/A");
        }

        status.setText(reservation.getStatus() != null ? reservation.getStatus() : "WAITLISTED");

        generateQRCodeAsync();
    }

    private void generateQRCodeAsync() {
        if (reservation == null) {
            status.setText("No reservation selected");
            return;
        }

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {
                BufferedImage bufferedImage = qrService.generateQRCode(reservation);
                if (bufferedImage != null) {
                    return SwingFXUtils.toFXImage(bufferedImage, null);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            Image image = task.getValue();
            if (image != null) {
                qrCode.setImage(image);
            } else {
                status.setText("QR generation failed");
            }
        });

        task.setOnFailed(e -> {
            if (task.getException() != null) {
                task.getException().printStackTrace();
            }
            status.setText("Error rendering QR Code");
        });

        new Thread(task).start();
    }

    // Sidebar Action Handlers
    @FXML
    private void handleHome(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/PassengerDashboard.fxml", 1000, 650);
    }

    @FXML
    private void handleReservations(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/Reservation.fxml", 1000, 650);
    }

    @FXML
    private void handleTripHistory(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/MyReservations.fxml", 1000, 650);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        userSession.clearSession();
        viewNavigator.navigateTo(event, "/fxml/Login.fxml", 900, 600);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        viewNavigator.navigateTo(event, "/fxml/PassengerDashboard.fxml", 1000, 650);
    }
}