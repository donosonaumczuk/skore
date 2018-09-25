package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private UserDao userDao;

    public UserServiceImpl() {

    }

    @Override
    public User findById(final long id) {
        if(id < 0 ) {
            LOGGER.error("Attempted to find a user with negative id.");
            throw new IllegalArgumentException("id must be positive");
        }

        LOGGER.trace("Looking up user with id {}", id);
        Optional<User> user = userDao.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with id: " + id + " doesn't exist.");
        }
    }

    @Override
    public User create(final String firstName, final String lastName,
                       final String email) {

        Optional<User> user = userDao.create(firstName, lastName, email);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("Couldn't create user.");
        }
    }

    @Override
    public boolean remove(final long userId) {
        return userDao.remove(userId);
    }

    @Override
    public User updateFirstName(final long userId, final String newFirstName){

        Optional<User> user = userDao.updateFirstName(userId, newFirstName);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with id: " + userId + " doesn't exist.");
        }
    }

    @Override
    public User updateLastName(final long userId, final String newLastName) {

        Optional<User> user = userDao.updateLastName(userId, newLastName);
        if(user.isPresent()) {
            return  user.get();
        }
        else {
            throw new UserNotFoundException("User with id: " + userId + " doesn't exist.");
        }
    }

    @Override
    public User updateEmail(final long userId, final String newEmail) {

        Optional<User> user = userDao.updateEmail(userId, newEmail);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with id: " + userId + " doesn't exist.");
        }
    }

}
