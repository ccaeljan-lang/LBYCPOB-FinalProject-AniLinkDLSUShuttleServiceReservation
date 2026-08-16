package ph.edu.dlsu.anilink.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.anilink.model.Administrator;
import ph.edu.dlsu.anilink.model.Driver;
import ph.edu.dlsu.anilink.model.Passenger;
import ph.edu.dlsu.anilink.model.User;
import ph.edu.dlsu.anilink.service.SupabaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("apiUserController")
@RequestMapping("/api/users")
public class UserController {

    private final SupabaseService supabaseService;
    private final ObjectMapper objectMapper;

    public UserController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
        this.objectMapper = new ObjectMapper();
    }

    // GET all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            String json = supabaseService.getAllUsers();
            List<User> usersList = objectMapper.readValue(json, new TypeReference<List<User>>() {});
            return ResponseEntity.ok(usersList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            String json = supabaseService.getUserById(id);
            List<User> usersList = objectMapper.readValue(json, new TypeReference<List<User>>() {});

            if (usersList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(usersList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET user by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            String json = supabaseService.getUserByEmail(email);
            List<User> usersList = objectMapper.readValue(json, new TypeReference<List<User>>() {});

            if (usersList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(usersList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // CREATE new user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getName() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Name, email, and password are required.");
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("name", user.getName());
            payload.put("email", user.getEmail());
            payload.put("password", user.getPassword());
            payload.put("role", user.getRole());

            // Add subclass specific properties
            if (user instanceof Passenger passenger) {
                payload.put("id_number", passenger.getIdNumber());
            } else if (user instanceof Driver driver) {
                payload.put("license_number", driver.getLicenseNumber());
            } else if (user instanceof Administrator admin) {
                payload.put("department", admin.getDepartment());
            }

            String json = supabaseService.createUser(payload);
            List<User> createdList = objectMapper.readValue(json, new TypeReference<List<User>>() {});

            if (createdList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdList.get(0));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user in Supabase.");
        }
    }

    // DELETE user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            supabaseService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}