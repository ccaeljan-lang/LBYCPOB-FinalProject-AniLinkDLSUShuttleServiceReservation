package ph.edu.dlsu.anilink.interfaces;

/**
 * Interface defining contract behavior for domain entities capable of real-time tracking.
 */
public interface Trackable {

    /**
     * Updates the geographic location coordinates of the trackable entity.
     *
     * @param lat latitude coordinate
     * @param lng longitude coordinate
     */
    void updateLocation(double lat, double lng);

    /**
     * Updates the current operational status of the trackable entity.
     *
     * @param status string representation of the new status
     */
    void updateStatus(String status);
}