package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(final long userId);

    Optional<User> create(final String firstName, final String lastName, final String email);

    boolean remove(final long userId);

    Optional<User> updateBasicUserInfo(final long userId, final String newFirstName,
                                       final String newLastName, final String newEmail);

    Optional<User> updateFirstName(final long userId, final String newFirstName);

    Optional<User> updateLastName(final long userId, final String newLastName);

    Optional<User> updateEmail(final long userId, final String newEmail);
}
