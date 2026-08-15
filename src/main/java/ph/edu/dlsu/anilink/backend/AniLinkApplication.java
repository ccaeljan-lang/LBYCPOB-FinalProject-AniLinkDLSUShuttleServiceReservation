package ph.edu.dlsu.anilink.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ph.edu.dlsu.anilink")
public class AniLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(AniLinkApplication.class, args);
    }
}