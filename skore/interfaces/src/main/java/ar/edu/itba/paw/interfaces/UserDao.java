package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    public Optional<User> findById(final long id);
    public User create(String username);
}
