package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;

@Service
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {

    }

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(String id) {
        // TODO Auto-generated method stub
        return new User();
    }

    @Override
    public User create(final String firstName, final String lastName,
                       final String email) {
        //validate Optional
        return userDao.create(firstName, lastName, email).get();
    }

    @Override
    public boolean remove(final long userId) {
        //validate Optional
        return userDao.remove(userId);
    }

    @Override
    public User updateFirstName(final long userId, final String newFirstName){
        //validate Optional
        return userDao.updateFirstName(userId, newFirstName).get();
    }

    @Override
    public User updateLastName(final long userId, final String newLastName) {
        //validate Optional
        return userDao.updateLastName(userId, newLastName).get();
    }

    @Override
    public User updateEmail(final long userId, final String newEmail) {
        //validate Optional
        return userDao.updateEmail(userId, newEmail).get();
    }

}
