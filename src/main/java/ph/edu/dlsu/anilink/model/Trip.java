package ph.edu.dlsu.anilink.model;

import ph.edu.dlsu.anilink.interfaces.Trackable;

public class Trip implements Trackable {

    public static final String SCHEDULED = "SCHEDULED";
    public static final String ARRIVING = "ARRIVING";
    public static final String BOARDING = "BOARDING";
    public static final String DEPARTED = "DEPARTED";
    public static final String COMPLETED = "COMPLETED";

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
        this.status = SCHEDULED;
    }

    @Override
    public void updateLocation(double lat, double lng) {
        this.currentLat = lat;
        this.currentLng = lng;
    }

    @Override
    public void updateStatus(String status) {
        this.status = status;
    }

    public int getAvailableSeats() {
        return capacity - seatsTaken;
    }

    public boolean addPassenger() {
        if (!isFull()) {
            seatsTaken++;
            return true;
        }
        return false;
    }

    public boolean removePassenger() {
        if (seatsTaken > 0) {
            seatsTaken--;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return seatsTaken >= capacity;
    }

    public Long getTripId() {
        return tripId;
    }

    public Route getRoute() {
        return route;
    }

    public DepartureSchedule getSchedule() {
        return schedule;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSeatsTaken() {
        return seatsTaken;
    }

    public String getStatus() {
        return status;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }
}