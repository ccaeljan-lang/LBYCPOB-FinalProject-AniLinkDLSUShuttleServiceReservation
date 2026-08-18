package ph.edu.dlsu.anilink.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Service managing reservation lifecycles, validation, and Supabase synchronization.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>Validation:</b> Delegates pre-booking rules to {@link BookingValidationService}.</li>
 *   <li><b>QR Generation:</b> Assigns unique UUID payloads for boarding verification.</li>
 *   <li><b>Persistence:</b> Atomically updates trip seat counts prior to persisting reservations to prevent overbooking.</li>
 * </ul>
 * </p>
 *
 * @see ph.edu.dlsu.anilink.service.SupabaseService
 * @see ph.edu.dlsu.anilink.service.BookingValidationService
 */
@Service
public class ReservationService {

    private final SupabaseService supabaseService;
    private final BookingValidationService bookingValidationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReservationService(
            SupabaseService supabaseService,
            BookingValidationService bookingValidationService) {
        this.supabaseService = supabaseService;
        this.bookingValidationService = bookingValidationService;
    }

    public boolean canBook(User user, Trip trip) {
        if (user == null || trip == null || trip.isFull()) {
            return false;
        }
        return bookingValidationService.validateBooking(user, trip).isValid();
    }

    public String getBookingError(User user, Trip trip) {
        if (user == null || trip == null) {
            return "Invalid passenger or trip selected.";
        }
        if (trip.isFull()) {
            return "This trip is already fully booked.";
        }

        BookingValidationService.ValidationResult result =
                bookingValidationService.validateBooking(user, trip);

        return result.isValid() ? "" : result.getMessage();
    }

    public Reservation createReservation(User user, Trip trip) {
        if (!canBook(user, trip)) {
            throw new IllegalStateException(getBookingError(user, trip));
        }

        String qrPayload = "ANILINK-RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        try {
            // 1. Reserve seat in Supabase first (if this fails, no reservation record is created)
            int updatedSeats = trip.getSeatsTaken() + 1;
            supabaseService.updateTripSeatsTaken(trip.getTripId(), updatedSeats);

            // 2. Post reservation record to Supabase
            String responseJson = supabaseService.postReservation(
                    user.getUserId(),
                    trip.getTripId(),
                    qrPayload
            );

            // 3. Parse created reservation response
            List<Reservation> createdList = objectMapper.readValue(
                    responseJson,
                    new TypeReference<List<Reservation>>() {}
            );

            if (createdList == null || createdList.isEmpty()) {
                throw new RuntimeException("Failed to retrieve created reservation record from Supabase.");
            }

            // 4. Update local trip object state only after both remote calls succeed
            trip.setSeatsTaken(updatedSeats);

            return createdList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save reservation to Supabase: " + e.getMessage(), e);
        }
    }
}