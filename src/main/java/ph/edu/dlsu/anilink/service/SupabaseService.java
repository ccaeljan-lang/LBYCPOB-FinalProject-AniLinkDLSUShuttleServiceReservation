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