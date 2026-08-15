package ph.edu.dlsu.anilink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SupabaseService {
    private final RestClient restClient;

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

    public String getRoutes() {
        return restClient.get()
                .uri("/routes?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getTrips() {
        return restClient.get()
                .uri("/trips?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getReservations() {
        return restClient.get()
                .uri("/reservations?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getUsers() {
        return restClient.get()
                .uri("/users?select=*")
                .retrieve()
                .body(String.class);
    }

    public String getSchedules() {
        return restClient.get()
                .uri("/departure_schedules?select=*")
                .retrieve()
                .body(String.class);
    }
}