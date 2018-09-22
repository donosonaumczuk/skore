package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

public interface UserService {
    public User findById(final long id);

    public User create(final String firstName, final String lastName,
                       final String email);

    public boolean remove(final long userId);

    public User updateFirstName(final long userId, final String newFirstName);

    public User updateLastName(final long userId, final String newLastName);

    public User updateEmail(final long userId, final String newEmail);
}
