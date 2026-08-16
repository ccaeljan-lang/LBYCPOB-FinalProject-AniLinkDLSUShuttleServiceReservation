package ph.edu.dlsu.anilink.rules;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

@Component
public class PassengerRoleBookingRule implements BookingRule {

    private String errorMessage = "";

    @Override
    public boolean validate(User user, Trip trip) {
        if (user == null) {
            errorMessage = "User session not found. Please log in again.";
            return false;
        }

        if (!"PASSENGER".equalsIgnoreCase(user.getRole())) {
            errorMessage = "Only registered passengers are permitted to make reservations.";
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}