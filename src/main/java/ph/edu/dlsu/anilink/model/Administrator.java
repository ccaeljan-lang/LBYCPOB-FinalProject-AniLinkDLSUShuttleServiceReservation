package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Administrator extends User {

    private String department;

    public Administrator() {
        super();
    }

    @JsonCreator
    public Administrator(
            @JsonProperty("id") Long userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("department") String department) {
        super(userId, name, email, password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public String toString() {
        return getName() + " (Admin)";
    }
}