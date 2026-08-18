package ph.edu.dlsu.anilink.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.anilink.model.Route;
import ph.edu.dlsu.anilink.service.SupabaseService;

import java.util.List;

/**
 * REST controller for managing routes.
 * <p>
 * This class exposes API endpoints under {@code /api/routes} to handle client
 * requests related to {@link Route} data. It provides CRUD operations to retrieve all routes,
 * fetch a specific route by its ID, create new routes, and delete existing routes.
 * The controller integrates with {@link SupabaseService} to perform the necessary
 * database interactions.
 * </p>
 */
@RestController("apiRouteController")
@RequestMapping("/api/routes")
public class RouteController {

    private final SupabaseService supabaseService;
    private final ObjectMapper objectMapper;

    public RouteController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
        this.objectMapper = new ObjectMapper();
    }

    // GET all routes
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        try {
            String json = supabaseService.getRoutes();
            List<Route> routesList = objectMapper.readValue(json, new TypeReference<List<Route>>() {});
            return ResponseEntity.ok(routesList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET route by ID
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        try {
            String json = supabaseService.getRouteById(id);
            List<Route> routesList = objectMapper.readValue(json, new TypeReference<List<Route>>() {});

            if (routesList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(routesList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // CREATE a new route
    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody Route route) {
        if (route.getOrigin() == null || route.getDestination() == null) {
            return ResponseEntity.badRequest().body("Origin and destination are required.");
        }

        try {
            String json = supabaseService.createRoute(route.getOrigin(), route.getDestination());
            List<Route> createdList = objectMapper.readValue(json, new TypeReference<List<Route>>() {});

            if (createdList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create route.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating route in Supabase.");
        }
    }

    // DELETE route by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        try {
            supabaseService.deleteRoute(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}