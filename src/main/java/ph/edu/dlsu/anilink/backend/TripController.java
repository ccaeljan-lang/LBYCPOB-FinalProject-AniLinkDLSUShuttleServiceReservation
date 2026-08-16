package ph.edu.dlsu.anilink.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.service.SupabaseService;

import java.util.List;

@RestController("apiTripController")
@RequestMapping("/api/trips")
public class TripController {

    private final SupabaseService supabaseService;
    private final ObjectMapper objectMapper;

    public TripController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // GET all trips
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        try {
            String json = supabaseService.getTripsWithDetails();
            List<Trip> tripsList = objectMapper.readValue(json, new TypeReference<List<Trip>>() {});
            return ResponseEntity.ok(tripsList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET trip by ID
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        try {
            String json = supabaseService.getTripDetails(id);
            List<Trip> tripsList = objectMapper.readValue(json, new TypeReference<List<Trip>>() {});

            if (tripsList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tripsList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // CREATE a new trip
    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody Trip trip) {
        if (trip.getRoute() == null || trip.getSchedule() == null) {
            return ResponseEntity.badRequest().body("Trip must include both a route and a schedule.");
        }

        try {
            String json = supabaseService.createTrip(
                    trip.getRoute().getRouteId(),
                    trip.getSchedule().getScheduleId(),
                    trip.getCapacity(),
                    trip.getStatus()
            );
            List<Trip> createdList = objectMapper.readValue(json, new TypeReference<List<Trip>>() {});

            if (createdList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create trip.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating trip in Supabase.");
        }
    }

    // UPDATE trip status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTripStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            supabaseService.updateTripStatus(id, status);

            String json = supabaseService.getTripDetails(id);
            List<Trip> tripsList = objectMapper.readValue(json, new TypeReference<List<Trip>>() {});

            if (tripsList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tripsList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update trip status.");
        }
    }

    // UPDATE trip location
    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateTripLocation(
            @PathVariable Long id,
            @RequestParam double lat,
            @RequestParam double lng) {

        try {
            supabaseService.updateTripLocation(id, lat, lng);

            String json = supabaseService.getTripDetails(id);
            List<Trip> tripsList = objectMapper.readValue(json, new TypeReference<List<Trip>>() {});

            if (tripsList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(tripsList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update trip location.");
        }
    }

    // DELETE trip
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        try {
            supabaseService.deleteTrip(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}