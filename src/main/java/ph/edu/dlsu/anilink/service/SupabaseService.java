package ph.edu.dlsu.anilink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ph.edu.dlsu.anilink.model.Route;

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

    public String createRoute(Route route) {
        return restClient.post()
                .uri("/routes?select=*")
                .body(route)
                .retrieve()
                .body(String.class);
    }

    public void updateRoute(Route route) {
        restClient.patch()
                .uri("/routes?route_id=eq." + route.getRouteId())
                .body(route)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteRoute(Long routeId) {
        restClient.delete()
                .uri("/routes?route_id=eq." + routeId)
                .retrieve()
                .toBodilessEntity();
    }
}