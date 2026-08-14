package ph.edu.dlsu.anilink.model;

public class Administrator extends User {

    private String adminLevel;

    public Administrator(
            Long userId,
            String name,
            String email,
            String password,
            String adminLevel) {

        super(userId, name, email, password);
        setAdminLevel(adminLevel);
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        if (adminLevel == null || adminLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin level cannot be empty.");
        }

        this.adminLevel = adminLevel.trim();
    }

    @Override
    public String getRole() {
        return "ADMINISTRATOR";
    }

    public void createRoute(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null.");
        }
    }

    public void createSchedule(DepartureSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null.");
        }
    }

    public void assignDriver(Driver driver, Trip trip) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null.");
        }

        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null.");
        }

        driver.assignTrip(trip);
    }
}