package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;

import java.util.Optional;

public interface SessionService {

    boolean isLogged();

    Optional<PremiumUser> getLoggedUser();

    boolean isAdmin();

    String getUserName();
}
