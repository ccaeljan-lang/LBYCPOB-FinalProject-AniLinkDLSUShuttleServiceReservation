package ph.edu.dlsu.anilink.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

import java.util.List;
import java.util.UUID;

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
        if (user == null || trip == null) {
            return false;
        }

        if (trip.isFull()) {
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
            // 1. Post reservation to Supabase and get back the created JSON record
            String responseJson = supabaseService.postReservation(
                    user.getUserId(),
                    trip.getTripId(),
                    qrPayload
            );

            // 2. Parse returned created record array from Supabase
            List<Reservation> createdList = objectMapper.readValue(
                    responseJson,
                    new TypeReference<List<Reservation>>() {}
            );

            if (createdList == null || createdList.isEmpty()) {
                throw new RuntimeException("Failed to retrieve created reservation record from Supabase.");
            }

            // 3. Increment seats_taken in Supabase
            int updatedSeats = trip.getSeatsTaken() + 1;
            supabaseService.updateTripSeatsTaken(trip.getTripId(), updatedSeats);
            trip.setSeatsTaken(updatedSeats);

            return createdList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save reservation to Supabase.", e);
        }
    }
}