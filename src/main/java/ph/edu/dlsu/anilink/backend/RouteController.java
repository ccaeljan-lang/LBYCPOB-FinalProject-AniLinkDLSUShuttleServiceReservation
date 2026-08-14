package ph.edu.dlsu.anilink.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ph.edu.dlsu.anilink.model.Route;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/routes")

public class RouteController {
    private final List<Route> routes = new ArrayList<>();

    // GET all routes
    @GetMapping
    public List<Route> getAllRoutes() {
        return routes;
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
}
