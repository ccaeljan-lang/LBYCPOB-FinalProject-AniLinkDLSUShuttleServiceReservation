package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver extends User {

    private String licenseNumber;

    public Driver() {
        super();
    }

    @JsonCreator
    public Driver(
            @JsonProperty("id") Long userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("license_number") String licenseNumber) {
        super(userId, name, email, password);
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public String getRole() {
        return "DRIVER";
    }

    @Override
    public String toString() {
        return getName() + " (Driver)";
    }
}