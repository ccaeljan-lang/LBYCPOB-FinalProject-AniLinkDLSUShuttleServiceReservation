package ph.edu.dlsu.anilink.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

import java.util.List;

/**
 * Core business service responsible for evaluating reservation requests against system booking rules.
 *
 * <p>This service acts as the context/runner for the Strategy pattern implementation of booking validation.
 * It encapsulates the following components:
 * <ul>
 *   <li><b>Automated Rule Injection:</b> Leverages Spring's {@link Service} dependency injection to discover
 *       and collect all spring-managed beans implementing {@link BookingRule}.</li>
 *   <li><b>Chain Validation Execution:</b> Iterates sequentially through all injected rules in {@link #validateBooking(User, Trip)},
 *       short-circuiting on the first validation failure.</li>
 *   <li><b>Encapsulated Result Transport:</b> Employs an immutable nested class ({@link ValidationResult})
 *       to cleanly communicate validation status and failure reasons back to UI controllers.</li>
 * </ul>
 * </p>
 */
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