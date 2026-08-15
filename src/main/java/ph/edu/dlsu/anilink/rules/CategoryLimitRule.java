package ph.edu.dlsu.anilink.rules;

import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

public class CategoryLimitRule implements BookingRule {
    private static final int MAX_CATEGORY_BOOKINGS = 2;

    @Override
    public boolean validate(User user, Trip trip) {
        if (!(user instanceof Passenger)) {
            return false;
        }

        Passenger passenger = (Passenger) user;
        int bookingCount = 0;

        for (Reservation reservation : passenger.getReservationHistory()) {
            String status = reservation.getStatus();

            if (!Reservation.CANCELLED.equals(status)
                    && !Reservation.COMPLETED.equals(status)) {
                bookingCount++;
            }
        }

        return bookingCount < MAX_CATEGORY_BOOKINGS;
    }

    @Override
    public String getErrorMessage() {
        return "You have reached the maximum number of allowed bookings.";
    }
}