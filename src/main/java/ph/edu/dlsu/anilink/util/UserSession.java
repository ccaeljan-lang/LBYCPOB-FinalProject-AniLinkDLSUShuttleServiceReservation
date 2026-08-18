package ph.edu.dlsu.anilink.util;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.model.User;

/**
 * In-memory session manager for tracking the active application user.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>State Management:</b> Holds a reference to the logged-in {@link User} instance across FX controllers.</li>
 *   <li><b>Lifecycle Control:</b> Provides methods to update and purge session state during login and logout workflows.</li>
 * </ul>
 * </p>
 */
@Component
public class UserSession {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clearSession() {
        this.currentUser = null;
    }
}