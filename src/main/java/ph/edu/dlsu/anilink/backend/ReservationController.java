package ph.edu.dlsu.anilink.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.Reservation;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.service.ReservationService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationController(
            ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {

        for (Reservation reservation : reservations) {

            if (reservation.getReservationId().equals(id)) {
                return ResponseEntity.ok(reservation);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // CREATE reservation
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam Long reservationId,
            @RequestBody ReservationRequest request) {

        Reservation reservation =
                reservationService.createReservation(
                        reservationId,
                        request.passenger(),
                        request.trip()
                );

        if (reservation == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        reservations.add(reservation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservation);
    }

    // CANCEL reservation
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(
            @PathVariable Long id) {

        for (Reservation reservation : reservations) {

            if (reservation.getReservationId().equals(id)) {

                reservation.cancel();

                reservation.getTrip().removePassenger();

                return ResponseEntity.ok(reservation);
            }
        }

        return ResponseEntity.notFound().build();
    }

    public record ReservationRequest(
            Passenger passenger,
            Trip trip
    ) {
    }
}
