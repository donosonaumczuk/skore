package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.User;

import java.util.Locale;

public interface UserService {

    User findById(final long id);

    User create(final String firstName, final String lastName, final String email);

    boolean remove(final long userId);

    User updateFirstName(final long userId, final String newFirstName);

    User updateLastName(final long userId, final String newLastName);

    User updateEmail(final long userId, final String newEmail);

    void sendConfirmMatchAssistance(final User user, final Game game, final String data, final Locale locale);

    User getUserFromData(final String data, final String gameData);

    void sendCancelOptionMatch(final User user, final Game game, final String data, final Locale locale);
}
