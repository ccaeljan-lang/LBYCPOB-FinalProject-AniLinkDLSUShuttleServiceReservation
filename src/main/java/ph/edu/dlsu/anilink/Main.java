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

public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        applicationContext = new SpringApplicationBuilder(AniLinkApplication.class).run(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setTitle("AniLink Shuttle Service");
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}