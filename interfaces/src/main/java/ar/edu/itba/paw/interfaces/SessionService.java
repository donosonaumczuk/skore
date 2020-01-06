package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;

public interface SessionService {
    boolean isLogged();

    PremiumUser getLoggedUser();

    boolean isAdmin();

    String getUserName();
}
