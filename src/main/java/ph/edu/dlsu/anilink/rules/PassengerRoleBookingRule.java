package ph.edu.dlsu.anilink.rules;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

/**
 * Strategy validation rule component that verifies user role permissions prior to making a booking.
 *
 * <p>This class implements {@link BookingRule} to restrict booking actions strictly to passengers. It encapsulates:
 * <ul>
 *   <li><b>Spring Component Management:</b> Annotated with {@link Component} for automated dependency injection within the rule chain.</li>
 *   <li><b>Session Guard:</b> Performs null checks on the active {@link User} object to handle missing session states.</li>
 *   <li><b>Role-Based Access Control (RBAC):</b> Validates that the active user's role evaluates to {@code PASSENGER} case-insensitively.</li>
 *   <li><b>Error Message State:</b> Captures descriptive error feedback via {@link #getErrorMessage()} when access is restricted.</li>
 * </ul>
 * </p>
 */
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