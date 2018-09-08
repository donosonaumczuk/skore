package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;

public interface UserService {
    public User findById(String id);

    public User create(final String firstName, final String lastName,
                       final String email);
}
