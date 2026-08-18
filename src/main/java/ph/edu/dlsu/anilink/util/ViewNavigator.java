package ph.edu.dlsu.anilink.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Utility component for handling JavaFX scene navigation and Spring dependency injection.
 */
@Component
public class ViewNavigator {

    private final ApplicationContext applicationContext;

    /**
     * Constructs the ViewNavigator with Spring application context.
     *
     * @param applicationContext the Spring context used for controller factory injection
     */
    public ViewNavigator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Navigates to a new view without returning its controller instance.
     *
     * @param event the event triggering the navigation
     * @param fxmlPath the resource path to the target FXML file
     * @param width the preferred window width
     * @param height the preferred window height
     */
    public void navigateTo(Event event, String fxmlPath, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load view: " + fxmlPath);
        }
    }

    /**
     * Navigates to a new view and returns its controller for data passing.
     *
     * @param <T> the type of the target controller
     * @param event the event triggering the navigation
     * @param fxmlPath the resource path to the target FXML file
     * @param width the preferred window width
     * @param height the preferred window height
     * @return the controller instance associated with the loaded view, or null if loading fails
     */
    public <T> T navigateToAndGetController(Event event, String fxmlPath, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.show();

            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load view with controller: " + fxmlPath);
            return null;
        }
    }
}