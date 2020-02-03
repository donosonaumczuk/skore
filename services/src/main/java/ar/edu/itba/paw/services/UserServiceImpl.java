package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.alreadyexists.UserAlreadyExistException;
import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.SimpleEncrypter;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailSender;

    private SimpleEncrypter encrypter = new SimpleEncrypter();

    public UserServiceImpl() {

    }

    @Override
    public User findById(final long id) {
        if(id < 0 ) {
            LOGGER.error("Attempted to find a user with negative id.");
            throw new IllegalArgumentException("id must be positive");
        }

        LOGGER.trace("Looking up user with id {}", id);
        return userDao.findById(id).orElseThrow(() -> UserNotFoundException.ofId(id));
    }

    @Override
    public User create(final String firstName, final String lastName,
                       final String email) {

        Optional<User> user = userDao.create(firstName, lastName, email);

        return user.orElseThrow(() -> {
            LOGGER.trace("Creation fails, user '{}' already exist", email);
            return UserAlreadyExistException.ofEmail(email);
        });
    }

    @Override
    public boolean remove(final long userId) {
        return userDao.remove(userId);
    }

    @Override
    public User updateFirstName(final long userId, final String newFirstName){
        Optional<User> user = userDao.updateFirstName(userId, newFirstName);

        return user.orElseThrow(() -> UserNotFoundException.ofId(userId));
    }

    @Override
    public User updateLastName(final long userId, final String newLastName) {
        Optional<User> user = userDao.updateLastName(userId, newLastName);

        return user.orElseThrow(() -> UserNotFoundException.ofId(userId));
    }

    @Override
    public User updateEmail(final long userId, final String newEmail) {
        Optional<User> user = userDao.updateEmail(userId, newEmail);

        return user.orElseThrow(() -> UserNotFoundException.ofId(userId));
    }

    @Override
    public void sendConfirmMatchAssistance(final User user, final Game game, final String data) {
        String phrase = user.getUserId() + user.getFirstName() + "$" + data;
        phrase = encrypter.encryptString(phrase);
        emailSender.sendConfirmMatch(user, game, data + "/confirmMatch/" + phrase , LocaleContextHolder.getLocale());
    }

    @Override
    public User getUserFromData(final String data, final String gameData) {
        String[] datas = encrypter.decryptString(data).split("\\$");
        String userDataDecrypted = datas[0];
        String gameDataDecrypted = datas[1];

        if (!gameDataDecrypted.equals(gameData)) {
            throwAndLogCodeError(data);
        }

        long id = 0;
        int i;
        for(i = 0; i < userDataDecrypted.length() && (userDataDecrypted.charAt(i) >= '0'
                && userDataDecrypted.charAt(i) <= '9'); i++) {
            id = id * 10 + (userDataDecrypted.charAt(i) - '0');
        }

        User user = findById(id);
        if (!user.getFirstName().equals(userDataDecrypted.substring(i))) {
            throwAndLogCodeError(data);
        }

        return user;
    }

    @Override
    public void sendCancelOptionMatch(final User user, final Game game, final String data) {
        String phrase = user.getUserId() + user.getFirstName() + "$" + data;
        phrase = encrypter.encryptString(phrase);
        emailSender.sendCancelMatch(user, game, data + "/cancelMatch/" + phrase, LocaleContextHolder.getLocale());
    }

    private void throwAndLogCodeError(String code) {
        LOGGER.trace("The code '{}' is invalid", code);
        throw new IllegalArgumentException("The code '" + code + "' is invalid");
    }
}
