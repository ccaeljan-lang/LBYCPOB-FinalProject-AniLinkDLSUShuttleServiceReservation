package ph.edu.dlsu.anilink.rules;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

/**
 * Strategy validation rule component that verifies shuttle capacity before allowing a booking.
 *
 * <p>This class implements {@link BookingRule} to enforce vehicle seat limits. It encapsulates:
 * <ul>
 *   <li><b>Spring Component Managed:</b> Annotated with {@link Component} for automated dependency injection.</li>
 *   <li><b>Null Safety Verification:</b> Ensures a valid {@link Trip} instance is present before evaluation.</li>
 *   <li><b>Capacity Enforcement:</b> Compares occupied seats against max capacity to prevent overbooking.</li>
 *   <li><b>Error Message State:</b> Maintains descriptive failure feedback via {@link #getErrorMessage()} when validation fails.</li>
 * </ul>
 * </p>
 */
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