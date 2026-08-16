package ph.edu.dlsu.anilink.rules;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

@Component
public class CapacityBookingRule implements BookingRule {

    private String errorMessage = "";

    @Override
    public boolean validate(User user, Trip trip) {
        if (trip == null) {
            errorMessage = "No trip specified for validation.";
            return false;
        }

        if (trip.getSeatsTaken() >= trip.getCapacity()) {
            errorMessage = "This trip is fully booked. No available seats remaining.";
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}