package ph.edu.dlsu.anilink.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ph.edu.dlsu.anilink.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SupabaseService(
            @Value("${app.supabase.url}") String supabaseUrl,
            @Value("${app.supabase.secret-key}") String secretKey) {
        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl + "/rest/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("apikey", secretKey)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .build();
    }

    public User findUserByEmail(String email) {
        try {
            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/users")
                            .queryParam("email", "eq." + email.trim())
                            .queryParam("select", "*")
                            .build())
                    .retrieve()
                    .body(String.class);

            System.out.println("Supabase query response: " + response);

            JsonNode users = objectMapper.readTree(response);

            if (!users.isArray() || users.isEmpty()) {
                return null;
            }

            JsonNode user = users.get(0);

            // Safely parse ID whether Supabase returns a Number or a UUID String
            Long userId = 1L;
            if (user.has("id") && !user.get("id").isNull()) {
                if (user.get("id").isNumber()) {
                    userId = user.get("id").asLong();
                } else {
                    // If ID is a UUID string, hash it to a positive Long for Model compatibility
                    userId = Math.abs((long) user.get("id").asText().hashCode());
                }
            }

            String name = user.has("name") ? user.get("name").asText() : "";
            String userEmail = user.has("email") ? user.get("email").asText() : "";
            String password = user.has("password") ? user.get("password").asText() : "";
            String role = user.has("role") ? user.get("role").asText() : "PASSENGER";

            switch (role.toUpperCase()) {
                case "PASSENGER":
                    return new Passenger(
                            userId,
                            name,
                            userEmail,
                            password,
                            getOptionalText(user, "category")
                    );
                case "DRIVER":
                    return new Driver(
                            userId,
                            name,
                            userEmail,
                            password,
                            getOptionalText(user, "license_number")
                    );
                case "ADMINISTRATOR":
                    return new Administrator(
                            userId,
                            name,
                            userEmail,
                            password,
                            getOptionalText(user, "admin_level")
                    );
                default:
                    throw new IllegalArgumentException("Unknown user role: " + role);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching user: " + e.getMessage(), e);
        }
    }

    public String getUsers() {
        return restClient.get()
                .uri("/users?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getUsersByRole(String role) {
        return restClient.get()
                .uri("/users?role=eq." + role + "&select=*")
                .retrieve()
                .body(String.class);
    }

    private String getOptionalText(
            JsonNode node,
            String fieldName) {
        JsonNode field = node.get(fieldName);

        if (field == null || field.isNull()) {
            return "";
        }

        return field.asText();
    }

    public void updateRoute(Route route) {
        restClient.patch()
                .uri("/routes?id=eq." + route.getRouteId())
                .body(
                        java.util.Map.of(
                                "origin", route.getOrigin(),
                                "destination", route.getDestination()
                        )
                )
                .retrieve()
                .toBodilessEntity();
    }

    public String createReservation(Reservation reservation) {
        return restClient.post()
                .uri("/reservations")
                .body(
                        java.util.Map.of(
                                "id", reservation.getReservationId(),
                                "passenger_id", reservation.getPassenger().getUserId(),
                                "trip_id", reservation.getTrip().getTripId(),
                                "status", reservation.getStatus(),
                                "qr_payload", reservation.getQrPayload()
                        )
                )
                .retrieve()
                .body(String.class);
    }

    public String getReservations() {
        return restClient.get()
                .uri("/reservations?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getReservationsByPassenger(Long passengerId) {
        return restClient.get()
                .uri("/reservations?passenger_id=eq." + passengerId + "&select=*,trip:trips(*,route:routes(*),schedule:departure_schedules(*))")
                .retrieve()
                .body(String.class);
    }

    public void cancelReservation(Long reservationId) {
        restClient.patch()
                .uri("/reservations?id=eq." + reservationId)
                .body(
                        java.util.Map.of(
                                "status", "CANCELLED"
                        )
                )
                .retrieve()
                .toBodilessEntity();
    }

    public String createSchedule(DepartureSchedule schedule) {
        return restClient.post()
                .uri("/departure_schedules")
                .body(
                        java.util.Map.of(
                                "id", schedule.getScheduleId(),
                                "route_id", schedule.getRoute().getRouteId(),
                                "departure_time", schedule.getDepartureTime().toString(),
                                "capacity", schedule.getCapacity()
                        )
                )
                .retrieve()
                .body(String.class);
    }

    public String getRoutes() {
        return restClient.get()
                .uri("/routes?select=*")
                .retrieve()
                .body(String.class);
    }


    // REPLACES THE OLD registerPassenger METHOD
    public String registerAccount(String name, String email, String password, String role, String extraDetail) {
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("password", password);
        payload.put("role", role);


        // Map the extra detail to the correct column so findUserByEmail doesn't crash later
        if ("PASSENGER".equalsIgnoreCase(role)) {
            payload.put("category", extraDetail); // e.g., "STUDENT"
        } else if ("DRIVER".equalsIgnoreCase(role)) {
            payload.put("license_number", "PENDING-" + System.currentTimeMillis()); // Default placeholder license
        } else if ("ADMINISTRATOR".equalsIgnoreCase(role)) {
            payload.put("admin_level", "STANDARD"); // Default admin level
        }


        return restClient.post()
                .uri("/users")
                .header("Prefer", "return=representation")
                .body(payload)
                .retrieve()
                .body(String.class);
    }


    public String getTrips() {
        return restClient.get()
                .uri("/trips?select=*")
                .retrieve()
                .body(String.class);
    }


    public void updateTripStatus(Long tripId, String status) {
        restClient.patch()
                .uri("/trips?id=eq." + tripId)
                .body(java.util.Map.of("status", status))
                .retrieve()
                .toBodilessEntity();
    }


    public String getTripsByDriver(Long driverId) throws Exception {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/trips")
                        .queryParam("driver_id", "eq." + driverId)
                        .queryParam("limit", "1")
                        .queryParam("select", "*,route:routes(*),schedule:departure_schedules(*)")
                        .build())
                .retrieve()
                .body(String.class);
    }

    public void deleteSchedule(Long scheduleId) {
        restClient.delete()
                .uri("/departure_schedules?id=eq." + scheduleId)
                .retrieve()
                .toBodilessEntity();
    }
}