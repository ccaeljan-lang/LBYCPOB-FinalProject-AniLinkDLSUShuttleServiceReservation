package ph.edu.dlsu.anilink.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

import java.util.List;

/**
 * Service component responsible for executing booking validation strategies.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>Strategy Collection:</b> Automatically injects all Spring beans implementing {@link BookingRule}.</li>
 *   <li><b>Rule Validation:</b> Evaluates passenger and trip combinations sequentially against configured business constraints.</li>
 *   <li><b>Error Resolution:</b> Retrieves specific error feedback for failed validation rules.</li>
 * </ul>
 * </p>
 */
@Service
public class ValidationRuleService {

    private final List<BookingRule> rules;

    // Spring automatically injects all beans implementing BookingRule
    // (e.g., CapacityBookingRule, CategoryLimitRule, RouteLimitRule)
    public ValidationRuleService(List<BookingRule> rules) {
        this.rules = rules;
    }

    public boolean validateBooking(User user, Trip trip) {
        for (BookingRule rule : rules) {
            if (!rule.validate(user, trip)) {
                return false;
            }
        }
        return true;
    }

    public String getValidationError(User user, Trip trip) {
        for (BookingRule rule : rules) {
            if (!rule.validate(user, trip)) {
                return rule.getErrorMessage();
            }
        }
        return null;
    }
}