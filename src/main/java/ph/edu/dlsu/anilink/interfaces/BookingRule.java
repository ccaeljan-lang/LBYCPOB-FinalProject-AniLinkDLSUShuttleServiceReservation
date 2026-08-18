package ph.edu.dlsu.anilink.interfaces;

import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

/**
 * Strategy interface defining validation rules for processing trip reservations.
 */
public interface BookingRule {

    /**
     * Validates whether a user is eligible to book a specific trip.
     *
     * @param user the user attempting to make a reservation
     * @param trip the target trip for the reservation
     * @return true if the booking satisfies the rule, false otherwise
     */
    boolean validate(User user, Trip trip);

    /**
     * Retrieves the error message explaining why the validation failed.
     *
     * @return a descriptive error message string
     */
    String getErrorMessage();
}