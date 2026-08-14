package ph.edu.dlsu.anilink.rules;

import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

public class RouteLimitRule implements BookingRule {
    private static final int MAX_ROUTE_BOOKINGS = 1;

    @Override
    public boolean validate(User user, Trip trip) {

        // Rule only applies to passengers
        if (!(user instanceof Passenger)) {
            return false;
        }

        Passenger passenger = (Passenger) user;

        int bookingCount = 0;

        String targetRouteId = trip
                .getDepartureSchedule()
                .getRoute()
                .getRouteId();

        for (Reservation reservation : passenger.getReservations()) {

            String existingRouteId = reservation
                    .getTrip()
                    .getDepartureSchedule()
                    .getRoute()
                    .getRouteId();

            if (existingRouteId.equals(targetRouteId)) {
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
