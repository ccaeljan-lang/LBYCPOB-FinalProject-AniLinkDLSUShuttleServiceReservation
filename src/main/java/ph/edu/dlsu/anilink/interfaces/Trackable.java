package ph.edu.dlsu.anilink.interfaces;

public interface Trackable {
    void updateLocation(double lat, double lng);
    void updateStatus(String status);
}