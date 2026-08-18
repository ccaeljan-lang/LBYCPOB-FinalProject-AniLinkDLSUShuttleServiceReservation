package ph.edu.dlsu.anilink;

/**
 * Bootstrap launcher for starting the JavaFX application.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>Module System Bypass:</b> Provides a standard Java main entry point that avoids JavaFX module library loading conflicts when packaged as a JAR.</li>
 *   <li><b>Delegation:</b> Forwards execution directly to the primary {@link Main} JavaFX application class.</li>
 * </ul>
 * </p>
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}