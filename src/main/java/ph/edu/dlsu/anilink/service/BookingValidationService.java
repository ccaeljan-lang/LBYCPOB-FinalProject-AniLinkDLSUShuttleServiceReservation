package ph.edu.dlsu.anilink.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

import java.util.List;

@Service
public class BookingValidationService {

    private final List<BookingRule> bookingRules;

    // Spring automatically injects all beans implementing BookingRule
    public BookingValidationService(List<BookingRule> bookingRules) {
        this.bookingRules = bookingRules;
    }

    public ValidationResult validateBooking(User user, Trip trip) {
        for (BookingRule rule : bookingRules) {
            if (!rule.validate(user, trip)) {
                return new ValidationResult(false, rule.getErrorMessage());
            }
        }
        return new ValidationResult(true, "Validation successful");
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}