package ph.edu.dlsu.anilink.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ph.edu.dlsu.anilink.model.Route;

import java.util.ArrayList;
import java.util.List;

@RestController("apiRouteController")
@RequestMapping("/api/routes")
public class RouteController {
    private final List<Route> routes = new ArrayList<>();

    // GET all routes
    @GetMapping
    public List<Route> getAllRoutes() {
        return routes;
    }

    // GET route by ID
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(
            @PathVariable Long id) {

        for (Route route : routes) {

            if (route.getRouteId().equals(id)) {

                return ResponseEntity.ok(route);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // CREATE a new route
    @PostMapping
    public ResponseEntity<Route> createRoute(
            @RequestBody Route route) {

        routes.add(route);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(route);
    }

    // DELETE route by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(
            @PathVariable Long id) {

        boolean removed = routes.removeIf(
                route -> route.getRouteId().equals(id)
        );

        if (removed) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
