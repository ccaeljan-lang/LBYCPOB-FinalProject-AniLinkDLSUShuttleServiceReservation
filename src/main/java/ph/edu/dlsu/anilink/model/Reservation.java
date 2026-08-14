package ph.edu.dlsu.anilink.model;

import java.time.LocalDateTime;

public class Reservation {
    public static final String CONFIRMED = "CONFIRMED";
    public static final String WAITLISTED = "WAITLISTED";
    public static final String VERIFIED = "VERIFIED";
    public static final String CANCELLED = "CANCELLED";
    public static final String COMPLETED = "COMPLETED";

    private Long reservationId;
    private Passenger passenger;
    private Trip trip;
    private LocalDateTime createdAt;
    private String status;
    private String qrPayload;

    public Reservation(Long reservationId, Passenger passenger, Trip trip) {
        this.reservationId = reservationId;
        this.passenger = passenger;
        this.trip = trip;
        this.createdAt = LocalDateTime.now();
        this.status = WAITLISTED;
        this.qrPayload = "ANILINK-RES-" + reservationId;
    }

    public void confirm() {
        this.status = CONFIRMED;
    }

    public void waitlist() {
        this.status = WAITLISTED;
    }

    public void cancel() {
        this.status = CANCELLED;
    }

    public void verify() {
        this.status = VERIFIED;
    }

    public void complete() {
        this.status = COMPLETED;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Trip getTrip() {
        return trip;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public String getQrPayload() {
        return qrPayload;
    }
}