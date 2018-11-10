package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

import java.util.Locale;

public interface EmailService {
    public void sendConfirmAccount(PremiumUser user, String url, Locale locale);

    public void sendConfirmMatch(User user, Game game, String url, Locale locale);

    public void sendCancelMatch(User user, Game game, String url, Locale locale);
}
