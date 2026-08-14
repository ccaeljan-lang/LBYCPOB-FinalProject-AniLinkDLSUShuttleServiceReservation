package ph.edu.dlsu.anilink.model;
import java.time.LocalDateTime;

public class Reservation {
    private Long reservationId;
    private Passenger passenger;
    private Trip trip;
    private LocalDateTime createdAt;

    public Reservation(Long reservationId,
                       Passenger passenger,
                       Trip trip) {

        this.reservationId = reservationId;
        this.passenger = passenger;
        this.trip = trip;
        this.createdAt = LocalDateTime.now();

        // Default status
        this.status = "WAITLISTED";
    }
    public String getStatus() {
        return status;
    }
    public Passenger getPassenger() {
        return passenger;
    }

    public Trip getTrip() {
        return trip;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getQrPayload() {
        return qrPayload;
    }
}
