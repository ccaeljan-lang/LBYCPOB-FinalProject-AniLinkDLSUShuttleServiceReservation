package ph.edu.dlsu.anilink.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Passenger.class, name = "PASSENGER"),
        @JsonSubTypes.Type(value = Driver.class, name = "DRIVER"),
        @JsonSubTypes.Type(value = Administrator.class, name = "ADMIN")
})
public abstract class User {

    @JsonProperty("id")
    private Long userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    // Default constructor for Jackson reflection/deserialization
    public User() {
    }

    public User(Long userId, String name, String email, String password) {
        this.userId = userId;
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.toLowerCase().endsWith("@dlsu.edu.ph")) {
            throw new IllegalArgumentException("Email must be a valid DLSU email.");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    public String extractUsername() {
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            return email;
        }
        return email.substring(0, atIndex);
    }

    public abstract String getRole();
}