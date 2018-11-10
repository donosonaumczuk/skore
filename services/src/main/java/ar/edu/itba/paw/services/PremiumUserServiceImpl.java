package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.CannotCreateUserException;
import ar.edu.itba.paw.Exceptions.CannotValidateUserException;
import ar.edu.itba.paw.Exceptions.ImageNotFoundException;
import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import org.mockito.cglib.core.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PremiumUserServiceImpl extends UserServiceImpl implements PremiumUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUserServiceImpl.class);

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Autowired
    public EmailService emailSender;

    @Autowired
    private RoleDao roleDao;

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        LOGGER.trace("Looking for user with username: {}",userName);

        Optional<PremiumUser> user = premiumUserDao.findByUserName(userName);
        return user;
    }

    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        LOGGER.trace("Looking for user with email: {}",email);
        Optional<PremiumUser> user = premiumUserDao.findByEmail(email);

        if(!user.isPresent()) {
            LOGGER.error("Can't find user with email: {}", email);
        }
        return user;
    }

    @Override
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final String birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password,
                              final MultipartFile file) throws IOException {
       final String encodedPassword = new BCryptPasswordEncoder().encode(password);
        LOGGER.trace("Creating user");

        final String formattedBirthday = formatDate(birthday);
        Optional<PremiumUser> user = premiumUserDao.create(firstName, lastName, email, userName,
                                        cellphone, formattedBirthday, country, state, city, street, reputation,
                                        encodedPassword, file);
        if(user.isPresent()) {
            LOGGER.trace("Sending confirmation email to {}", email);
            sendConfirmationMail(user.get());
            return user.get();
        }
        else {
            LOGGER.error("Can't create user with username: {}", userName);

            throw new CannotCreateUserException("Can't create user with with userName: " + userName );
        }
    }

    @Override
    public boolean remove(final String userName) {
        LOGGER.trace("Looking for user with username: {} to remove", userName);
        boolean returnedValue = premiumUserDao.remove(userName);
        if(returnedValue) {
            LOGGER.trace("{} removed", userName);
        }
        else {
            LOGGER.trace("{} wasn't removed", userName);
        }
        return returnedValue;
    }

    @Override
    public byte[] readImage(final String userName) {
        Optional<byte[]> imagesOpt = premiumUserDao.readImage(userName);
        if(!imagesOpt.isPresent()) {
            LOGGER.error("Fail to read image from {}", userName);
            throw new ImageNotFoundException("Fail to read image from " + userName);
        }
        return imagesOpt.get();
    }

    @Override
    public PremiumUser updateUserInfo(final String newFirstName, final String newLastName,
                                      final String newEmail,final String newUserName,
                                      final String newCellphone, final String newBirthday,
                                      final String newCountry, final String newState,
                                      final String newCity, final String newStreet,
                                      final int newReputation, final String newPassword,
                                      final String oldUserName) {

        LOGGER.trace("Looking for user with username: {} to update", oldUserName);

        Optional<PremiumUser> user = premiumUserDao.updateUserInfo(newFirstName, newLastName,
                newEmail, newUserName, newCellphone, newBirthday, newCountry, newState,
                newCity, newStreet, newReputation, new BCryptPasswordEncoder().encode(newPassword), oldUserName);
        if(user.isPresent()) {
            LOGGER.trace("{] updated", oldUserName);
            return user.get();
        }
        else {
            LOGGER.error("Can't find user {}", oldUserName);

            throw new UserNotFoundException("User with userName: " + oldUserName + "doesn't exist.");
        }
    }

    private static String formatDate(String birthday) {
        DateTimeFormatter toParse = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter toFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsed = LocalDate.parse(birthday, toParse);
        String formattedDate = parsed.format(toFormat);
        LOGGER.trace("birthday date of user formatted to: {}", formattedDate);
        return formattedDate;
    }

    @Override
    public void addRole(final String username, final int roleId) {
        Optional<Role> role = roleDao.findRoleById(roleId);
        Optional<PremiumUser> user;
        LOGGER.trace("Looking for role with id: {}", roleId);

        if(role.isPresent()) {
            LOGGER.trace("Looking for user with username: {}", username);
            user = premiumUserDao.findByUserName(username);
            if(user.isPresent()) {
                LOGGER.trace("Adding role {} to user with username: {}", role.get().getName(), username);

                premiumUserDao.addRole(username, roleId);
            }
            else {
                LOGGER.error("Can't find user with username {}", username);
                throw new UserNotFoundException("Can't find user with username: " + username);
            }
        }
        else {
            LOGGER.error("Can't find role with id {}", roleId);
            throw new ar.edu.itba.paw.Exceptions.RoleNotFoundException("can't find role with id: " + roleId);
        }
    }

    @Override
    public boolean enableUser(final String username, final String code) {
        LOGGER.trace("Looking for user with username {} to enable", username);

        Optional<PremiumUser> user = findByUserName(username);
        if(!user.isPresent()) {
            LOGGER.error("Can't find user with username {}", username);
            return false;
        }
        PremiumUser currentUser = user.get();
        if(!premiumUserDao.enableUser(currentUser.getUserName(), code)) {
            LOGGER.error("Can't find user with username {}", username);
            return false;
        }
        LOGGER.trace("{} is now enabled", username);
        return true;
    }

    @Override
    public boolean confirmationPath(String path) {
        String dataPath = path.replace("/confirm/","");
        int splitIndex = dataPath.indexOf('&');
        String username = dataPath.substring(0, splitIndex);
        Optional<PremiumUser> premiumUser = findByUserName(username);
        if(!premiumUser.isPresent()) {
            return false;
        }
        String code = dataPath.substring(splitIndex + 1, dataPath.length());
        return enableUser(username, code);
    }

    private String generatePath(PremiumUser user) {
        return "confirm/" + user.getUserName() + "&" + user.getCode();
    }

    public void sendConfirmationMail(PremiumUser user) {
        emailSender.sendConfirmAccount(user, generatePath(user), LocaleContextHolder.getLocale()); //TODO: check if locale works here
    }

}
