package ph.edu.dlsu.anilink.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.interfaces.BookingRule;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;

@Component
public class CategoryLimitRule implements BookingRule {

    private static final int MAX_CATEGORY_BOOKINGS = 2;
    private final SupabaseService supabaseService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String errorMessage = "";

    public CategoryLimitRule(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @Override
    public boolean validate(User user, Trip trip) {
        if (!(user instanceof Passenger)) {
            errorMessage = "Only registered passengers can make shuttle reservations.";
            return false;
        }

        try {
            String json = supabaseService.getActiveReservationsByPassenger(user.getUserId());
            JsonNode array = objectMapper.readTree(json);

            int activeBookings = (array.isArray()) ? array.size() : 0;

            if (activeBookings >= MAX_CATEGORY_BOOKINGS) {
                errorMessage = "You have reached the maximum allowed limit of "
                        + MAX_CATEGORY_BOOKINGS + " active reservations.";
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Unable to verify booking limits against Supabase.";
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}