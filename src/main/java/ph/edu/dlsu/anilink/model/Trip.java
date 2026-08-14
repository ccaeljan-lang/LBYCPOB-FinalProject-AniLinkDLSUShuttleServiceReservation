package ph.edu.dlsu.anilink.model;

import ph.edu.dlsu.anilink.interfaces.Trackable;

public class Trip implements Trackable {
    private Long tripId;
    private Route route;
    private DepartureSchedule schedule;
    private Driver driver;
    private int capacity;
    private int seatsTaken;
    private String status;
    private double currentLat;
    private double currentLng;

    public Trip(Long tripId, Route route, DepartureSchedule schedule, int capacity) {
        this.tripId = tripId;
        this.route = route;
        this.schedule = schedule;
        this.capacity = capacity;
        this.seatsTaken = 0;
        this.status = "SCHEDULED";
    }

    @Override
    public void updateLocation(double lat, double lng) {
        this.currentLat = lat;
        this.currentLng = lng;
    }

}