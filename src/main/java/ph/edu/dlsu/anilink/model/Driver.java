package ph.edu.dlsu.anilink.model;

public class Driver extends User {

    private String licenseNumber;
    private Trip assignedTrip;

    public Driver(
            Long userId,
            String name,
            String email,
            String password,
            String licenseNumber) {

        super(userId, name, email, password);
        setLicenseNumber(licenseNumber);
        this.assignedTrip = null;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty.");
        }

        this.licenseNumber = licenseNumber.trim();
    }

    @Override
    public String getRole() {
        return "DRIVER";
    }

    public void assignTrip(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip cannot be null.");
        }

        this.assignedTrip = trip;
    }

    public Trip getAssignedTrip() {
        return assignedTrip;
    }

    public boolean verifyPassenger(Reservation reservation) {
        if (reservation == null) {
            return false;
        }

        if (assignedTrip == null) {
            return false;
        }

        return reservation.getTrip() == assignedTrip;
    }
}