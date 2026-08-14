package ph.edu.dlsu.anilink.model;

public class Route {
    private Long routeId;
    private String origin;
    private String destination;

    public Route(Long routeId, String origin, String destination) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getRouteName() {
        return origin + " -> " + destination;
    }
}