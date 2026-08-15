package ph.edu.dlsu.anilink.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.edu.dlsu.anilink.model.Reservation;
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

    // GET all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservations;
    }

    // GET reservation by ID
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
}
