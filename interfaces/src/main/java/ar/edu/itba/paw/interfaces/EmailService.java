package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

public interface EmailService {
    public void sendConfirmAccount(PremiumUser user, String url);

    public void sendConfirmMatch(User user, Game game, String url);

    public void sendCancelOptionMatch(User user, Game game, String url);

    }
