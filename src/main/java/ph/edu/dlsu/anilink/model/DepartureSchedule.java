package ph.edu.dlsu.anilink.model;

import java.time.LocalTime;

public class DepartureSchedule {
    private Long scheduleId;
    private Route route;
    private LocalTime departureTime;
    private int capacity;

    public DepartureSchedule(Long scheduleId, Route route, LocalTime departureTime, int capacity) {
        this.scheduleId = scheduleId;
        this.route = route;
        this.departureTime = departureTime;
        this.capacity = capacity;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public Route getRoute() {
        return route;
    }

    public int getCapacity() {
        return capacity;
    }
}