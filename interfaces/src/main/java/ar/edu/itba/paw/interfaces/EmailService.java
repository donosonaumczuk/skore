package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

import java.util.Locale;

public interface EmailService {

    void sendConfirmAccount(final PremiumUser user, final String url, final Locale locale);

    void sendConfirmMatch(final User user, final Game game, final String url, final Locale locale);

    void sendCancelMatch(final User user, final Game game, final String url, final Locale locale);

    void sendResetPassword(final PremiumUser user, final String url, final Locale locale);
}
