package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    public User findById(final long id);

    @Transactional
    public User create(final String firstName, final String lastName,
                       final String email);

    @Transactional
    public boolean remove(final long userId);

    @Transactional
    public User updateFirstName(final long userId, final String newFirstName);

    @Transactional
    public User updateLastName(final long userId, final String newLastName);

    @Transactional
    public User updateEmail(final long userId, final String newEmail);

    public void sendConfirmMatchAssistance(User user, Game game, String data);

    public long getUserIdFromData (String data);

    public void sendCancelOptionMatch(User user, Game game, String data);
}
