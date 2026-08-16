package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Passenger extends User {

    private String idNumber; // e.g., DLSU ID Number (121XXXXX)

    public Passenger() {
        super();
    }

    @JsonCreator
    public Passenger(
            @JsonProperty("id") Long userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("id_number") String idNumber) {
        super(userId, name, email, password);
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String getRole() {
        return "PASSENGER";
    }

    @Override
    public String toString() {
        return getName() + " (Passenger - " + getEmail() + ")";
    }
}