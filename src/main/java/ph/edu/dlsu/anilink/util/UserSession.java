package ph.edu.dlsu.anilink.util;

import org.springframework.stereotype.Component;
import ph.edu.dlsu.anilink.model.User;

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