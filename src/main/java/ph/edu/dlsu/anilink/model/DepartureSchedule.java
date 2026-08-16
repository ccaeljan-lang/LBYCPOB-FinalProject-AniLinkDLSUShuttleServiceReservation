package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DepartureSchedule {

    @JsonProperty("id")
    private Long scheduleId;

    @JsonProperty("route")
    private Route route;

    @JsonProperty("departure_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    @JsonProperty("capacity")
    private int capacity;

    // Default constructor required for Jackson reflection
    public DepartureSchedule() {
    }

    public DepartureSchedule(Long scheduleId, Route route, LocalTime departureTime, int capacity) {
        this.scheduleId = scheduleId;
        this.route = route;
        this.departureTime = departureTime;
        this.capacity = capacity;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        String formattedTime = (departureTime != null)
                ? departureTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                : "N/A";

        if (route != null) {
            return String.format("[%s] %s (Capacity: %d)", formattedTime, route.toString(), capacity);
        }
        return String.format("[%s] (Capacity: %d)", formattedTime, capacity);
    }
}