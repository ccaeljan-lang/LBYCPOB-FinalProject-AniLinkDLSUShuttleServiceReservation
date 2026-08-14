package ph.edu.dlsu.anilink.rules;

import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

public class CategoryLimitRule implements BookingRule {
    private static final int MAX_CATEGORY_BOOKINGS = 2;

    @Override
    public boolean validate(User user, Trip trip) {
        return true; // Placeholder for future logic
    }

    @Override
    public String getErrorMessage() {
        return "You have reached the maximum number of allowed bookings.";
    }
}
