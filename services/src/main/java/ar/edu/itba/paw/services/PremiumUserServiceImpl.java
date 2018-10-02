package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
public class PremiumUserServiceImpl extends UserServiceImpl implements PremiumUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUserServiceImpl.class);

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        Optional<PremiumUser> user = premiumUserDao.findByUserName(userName);
            return user;
    }

    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        Optional<PremiumUser> user = premiumUserDao.findByEmail(email);
        return user;
    }

    @Override
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final String birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password) {
       final String encodedPassword = new BCryptPasswordEncoder().encode(password);

        final String formattedBirthday = formatDate(birthday);
        Optional<PremiumUser> user = premiumUserDao.create(firstName, lastName, email, userName,
                                        cellphone, formattedBirthday, country, state, city, street, reputation,
                                        encodedPassword);
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
                newCity, newStreet, newReputation, new BCryptPasswordEncoder().encode(newPassword), oldUserName);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UserNotFoundException("User with userName: " + oldUserName + "doesn't exist.");
        }
    }

    private static String formatDate(String birthday) {
        String month = "" + birthday.charAt(0) + birthday.charAt(1);
        String day = "" + birthday.charAt(3) + birthday.charAt(4);
        String year = "" + birthday.charAt(6) + birthday.charAt(7) + birthday.charAt(8) + birthday.charAt(9);
        String formattedDate = year + "-" + month + "-" + day;
        LOGGER.trace("birthday date of user formatted to: {}", formattedDate);
        return formattedDate;

    }

    @Override
    public void addRole(final String username, final int roleId) {
        Optional<Role> role = roleDao.findRoleById(roleId);
        Optional<PremiumUser> user;
        if(role.isPresent()) {
            user = premiumUserDao.findByUserName(username);
            if(user.isPresent()) {
                premiumUserDao.addRole(username, roleId);
            }
            else {
                throw new UserNotFoundException("Can't find user with username: " + username);
            }
        }
        else {
            throw new ar.edu.itba.paw.Exceptions.RoleNotFoundException("can't find role with id: " + roleId);
        }
    }

    @Override
    public void enableUser(final String username, final String code) {
        Optional<PremiumUser> user = findByUserName(username);
        if(!user.isPresent()) {
            throw new UserNotFoundException("Can't find user with username " + username + " to validate account");
        }
        PremiumUser currentUser = user.get();
        if(!premiumUserDao.enableUser(currentUser.getUserName(), code)) {
            throw new UserNotFoundException("Can't validate account with username : " + currentUser.getUserName());
        }
    }

}
