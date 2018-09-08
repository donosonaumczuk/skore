package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    public Optional<User> findById(final long userId);

    public Optional<User> findByFirstName(final String firstName);
    public Optional<User> create(final String firstName, final String lastName, final String email);
}
