package ph.edu.dlsu.anilink.rules;

import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

public class RouteLimitRule implements BookingRule {
    private static final int MAX_ROUTE_BOOKINGS = 1;

    @Override
    public boolean validate(User user, Trip trip) {
        if (!(user instanceof Passenger) || trip == null || trip.getRoute() == null) {
            return false;
        }

        Passenger passenger = (Passenger) user;
        Long targetRouteId = trip.getRoute().getRouteId();
        int bookingCount = 0;

        for (Reservation reservation : passenger.getReservationHistory()) {
            if (reservation == null || reservation.getTrip() == null || reservation.getTrip().getRoute() == null) {
                continue;
            }

            if (!Reservation.CANCELLED.equals(reservation.getStatus())
                    && targetRouteId.equals(reservation.getTrip().getRoute().getRouteId())) {
                bookingCount++;
            }
        }

        return bookingCount < MAX_ROUTE_BOOKINGS;
    }

    @Override
    public String getErrorMessage() {
        return "You have reached the booking limit for this route.";
    }
}