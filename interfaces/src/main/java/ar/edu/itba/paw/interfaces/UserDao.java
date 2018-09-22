package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    public Optional<User> findById(final long userId);

    public Optional<User> create(final String firstName, final String lastName, final String email);

    public boolean remove(final long userId);

    public Optional<User> updateFirstName(final long userId, final String newFirstName);

    public Optional<User> updateLastName(final long userId, final String newLastName);

    public Optional<User> updateEmail(final long userId, final String newEmail);
}
