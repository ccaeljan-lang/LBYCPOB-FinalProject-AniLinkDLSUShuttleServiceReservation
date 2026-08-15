package ph.edu.dlsu.anilink.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.rules.CategoryLimitRule;
import ph.edu.dlsu.anilink.rules.RouteLimitRule;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationRuleService {

    private final List<BookingRule> rules = new ArrayList<>();

    public ValidationRuleService() {
        rules.add(new CategoryLimitRule());
        rules.add(new RouteLimitRule());
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