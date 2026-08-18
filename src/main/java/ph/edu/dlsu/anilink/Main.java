package ph.edu.dlsu.anilink;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ph.edu.dlsu.anilink.backend.AniLinkApplication;

/**
 * Primary JavaFX application entry point integrated with Spring Boot initialization.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>Context Bootstrapping:</b> Initializes {@link ConfigurableApplicationContext} via {@link SpringApplicationBuilder} with non-headless configuration.</li>
 *   <li><b>Dependency Injection Integration:</b> Bridges Spring DI with JavaFX by wiring {@link FXMLLoader#setControllerFactory} to the application context.</li>
 *   <li><b>Lifecycle Management:</b> Controls application startup, primary window setup, and clean context closure during teardown.</li>
 * </ul>
 * </p>
 */
public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        // Disable Spring's default headless mode to support JavaFX desktop execution
        this.applicationContext = new SpringApplicationBuilder(AniLinkApplication.class)
                .headless(false)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));

        // Allow Spring to inject dependencies into JavaFX Controllers
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setTitle("AniLink Shuttle Service");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (applicationContext != null && applicationContext.isRunning()) {
            applicationContext.close();
        }
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}