package ph.edu.dlsu.anilink.model;

import java.util.ArrayList;
import java.util.List;

public class Passenger extends User {

    private String category;
    private List<Reservation> reservationHistory;

    public Passenger(Long userId, String name, String email, String password, String category) {
        super(userId, name, email, password);
        this.category = category;
        this.reservationHistory = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }

        this.category = category.trim();
    }

    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }

        reservationHistory.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }

        reservationHistory.remove(reservation);
    }

    public List<Reservation> getReservationHistory() {
        return new ArrayList<>(reservationHistory);
    }

    @Override
    public String getRole() {
        return "PASSENGER";
    }
}