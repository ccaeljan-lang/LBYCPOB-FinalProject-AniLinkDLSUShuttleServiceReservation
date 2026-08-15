package ph.edu.dlsu.anilink.model;

public class Route {

    private Long routeId;
    private String origin;
    private String destination;

    public Route() {
    }

    public Route(Long routeId, String origin, String destination) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
    }

    public Route(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRouteName() {
        return origin + " -> " + destination;
    }

    @Override
    public String toString() {
        return getRouteName();
    }
}