package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PremiumUserServiceImpl extends UserServiceImpl implements PremiumUserService{

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Override
    public PremiumUser findByUserName(final String userName) {
        Optional<PremiumUser> user = premiumUserDao.findByUserName(userName);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with userName: " + userName + "doesn't exist.");
        }
    }

    @Override
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final String birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password) {

        Optional<PremiumUser> user = premiumUserDao.create(firstName, lastName, email, userName,
                                        cellphone, birthday, country, state, city, street, reputation,
                                        password);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with userName: " + userName + "doesn't exist.");
        }
    }

    @Override
    public boolean remove(final String userName) {
        return premiumUserDao.remove(userName);
    }

    @Override
    public PremiumUser updateUserInfo(final String newFirstName, final String newLastName,
                                      final String newEmail,final String newUserName,
                                      final String newCellphone, final String newBirthday,
                                      final String newCountry, final String newState,
                                      final String newCity, final String newStreet,
                                      final int newReputation, final String newPassword,
                                      final String oldUserName) {

        Optional<PremiumUser> user = premiumUserDao.updateUserInfo(newFirstName, newLastName,
                newEmail, newUserName, newCellphone, newBirthday, newCountry, newState,
                newCity, newStreet, newReputation, newPassword, oldUserName);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with userName: " + oldUserName + "doesn't exist.");
        }
    }

}
