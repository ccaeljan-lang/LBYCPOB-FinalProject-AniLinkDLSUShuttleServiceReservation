package ph.edu.dlsu.anilink.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ph.edu.dlsu.anilink.model.*;

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
                    .uri("/users?email=eq." + email + "&select=*")
                    .retrieve()
                    .body(String.class);

            JsonNode users = objectMapper.readTree(response);

            if (!users.isArray() || users.isEmpty()) {
                return null;
            }

            JsonNode user = users.get(0);
            Long userId = user.get("id").asLong();
            String name = user.get("name").asText();
            String userEmail = user.get("email").asText();
            String password = user.get("password").asText();
            String role = user.get("role").asText();

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
                    throw new IllegalArgumentException(
                            "Unknown user role: " + role
                    );
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to retrieve user from Supabase.",
                    e
            );
        }
    }

    public String getUsers() {
        return restClient.get()
                .uri("/users?select=*")
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

    public String getRoutes() {
        return restClient.get()
                .uri("/routes?select=*")
                .retrieve()
                .body(String.class);
    }

    public String createRoute(Route route) {
        return restClient.post()
                .uri("/routes")
                .body(
                        java.util.Map.of(
                                "id", route.getRouteId(),
                                "origin", route.getOrigin(),
                                "destination", route.getDestination()
                        )
                )
                .retrieve()
                .body(String.class);
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

    public void deleteRoute(Long routeId) {
        restClient.delete()
                .uri("/routes?id=eq." + routeId)
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
}