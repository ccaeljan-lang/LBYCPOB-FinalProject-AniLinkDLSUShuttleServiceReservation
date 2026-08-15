package ph.edu.dlsu.anilink.service;

import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.Trip;

@Service
public class ReservationService {

    private final SupabaseService supabaseService;
    private final ValidationRuleService validationRuleService;

    public ReservationService(
            SupabaseService supabaseService,
            ValidationRuleService validationRuleService) {

        this.supabaseService = supabaseService;
        this.validationRuleService = validationRuleService;
    }

    public boolean canBook(Passenger passenger, Trip trip) {

        if (passenger == null || trip == null) {
            return false;
        }

        if (trip.isFull()) {
            return false;
        }

        return validationRuleService
                .validateBooking(passenger, trip);
    }

    public String getBookingError(
            Passenger passenger,
            Trip trip) {

        if (passenger == null || trip == null) {
            return "Invalid passenger or trip.";
        }

        if (trip.isFull()) {
            return "This trip is already full.";
        }

        return validationRuleService
                .getValidationError(passenger, trip);
    }

    public Reservation createReservation(
            Long reservationId,
            Passenger passenger,
            Trip trip) {

        if (!canBook(passenger, trip)) {
            return null;
        }

        Reservation reservation =
                new Reservation(
                        reservationId,
                        passenger,
                        trip
                );

        trip.addPassenger();
        passenger.addReservation(reservation);

        return reservation;
    }
}