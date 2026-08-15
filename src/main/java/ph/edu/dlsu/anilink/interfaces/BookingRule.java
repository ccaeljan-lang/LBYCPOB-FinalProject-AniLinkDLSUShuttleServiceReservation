package ph.edu.dlsu.anilink.interfaces;

import ph.edu.dlsu.anilink.model.Trip;
import ph.edu.dlsu.anilink.model.User;

public interface BookingRule {
    boolean validate(User user, Trip trip);
    String getErrorMessage();
}