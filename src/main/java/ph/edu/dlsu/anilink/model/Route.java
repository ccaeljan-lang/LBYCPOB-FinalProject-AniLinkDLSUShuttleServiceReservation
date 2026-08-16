package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Route {

    @JsonProperty("id")
    private Long routeId;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    public Route() {}

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    // EQUALS & HASHCODE ARE REQUIRED FOR JAVAFX COMBOBOX MATCHING
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(routeId, route.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }

    @Override
    public String toString() {
        if (origin != null && destination != null) {
            return origin + " ↔ " + destination;
        }
        return "Unknown Route";
    }
}