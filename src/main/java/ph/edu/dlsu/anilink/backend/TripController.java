package ph.edu.dlsu.anilink.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ph.edu.dlsu.anilink.model.Trip;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trips")

public class TripController {
    private final List<Trip> trips = new ArrayList<>();

    // GET all trips
    @GetMapping
    public List<Trip> getAllTrips() {
        return trips;
    }

    // GET trip by ID
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(
            @PathVariable Long id) {

        for (Trip trip : trips) {

            if (trip.getTripId().equals(id)) {
                return ResponseEntity.ok(trip);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // CREATE a new trip
    @PostMapping
    public ResponseEntity<Trip> createTrip(
            @RequestBody Trip trip) {

        trips.add(trip);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trip);
    }

    // UPDATE trip status
    @PutMapping("/{id}/status")
    public ResponseEntity<Trip> updateTripStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        for (Trip trip : trips) {

            if (trip.getTripId().equals(id)) {

                trip.updateStatus(status);

                return ResponseEntity.ok(trip);
            }
        }

        return ResponseEntity.notFound().build();
    }


    // UPDATE trip location
    @PutMapping("/{id}/location")
    public ResponseEntity<Trip> updateTripLocation(
            @PathVariable Long id,
            @RequestParam double lat,
            @RequestParam double lng) {

        for (Trip trip : trips) {

            if (trip.getTripId().equals(id)) {

                trip.updateLocation(lat, lng);

                return ResponseEntity.ok(trip);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE trip
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id) {

        boolean removed = trips.removeIf(
                trip -> trip.getTripId().equals(id)
        );

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
