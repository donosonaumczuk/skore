package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.InvalidUserCodeException;
import ar.edu.itba.paw.exceptions.UserAlreadyIsEnableException;
import ar.edu.itba.paw.exceptions.WrongOldUserPasswordException;
import ar.edu.itba.paw.exceptions.alreadyexists.UserAlreadyExistException;
import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.UserSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PremiumUserServiceImpl implements PremiumUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUserServiceImpl.class);

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Autowired
    private GameService gameService;

    @Autowired
    public EmailService emailSender;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private Environment environment;

    @Transactional
    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        LOGGER.trace("Looking for user with username: {}", userName);
        return premiumUserDao.findByUserName(userName).map(user -> {
            List<List<Game>> games = gameService.getGamesThatPlay(user.getUser().getUserId());
            user.setGamesInTeam1(games.get(0));
            user.setGamesInTeam2(games.get(1));
            user.setWinRate(calculateWinRate(user));
            return user;
        });
    }

    @Transactional
    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        LOGGER.trace("Looking for user with email: {}", email);
        return premiumUserDao.findByEmail(email); //TODO: why does not include games/matches?
    }

    @Transactional
    @Override
    public Optional<PremiumUser> findById(final long userId) {
        LOGGER.trace("Looking for user with id: {}", userId);
        return premiumUserDao.findById(userId); //TODO: why does not include games/matches?
    }

    @Transactional
    @Override
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final LocalDate birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password,
                              final byte[] file, final Locale locale) {
        LOGGER.trace("Attempting to create user: {}", userName);
        if (premiumUserDao.findByEmail(email).isPresent()) {
            LOGGER.trace("User with email {} already exist", email);
            throw UserAlreadyExistException.ofEmail(email);
        }
        if (premiumUserDao.findByUserName(userName).isPresent()) {
            LOGGER.trace("User with username {} already exist", userName);
            throw UserAlreadyExistException.ofUsername(userName);
        }

        final String encodedPassword = bcrypt.encode(password);
        PremiumUser user = premiumUserDao.create(
                firstName, lastName, email, userName, cellphone, birthday, country, state, city, street, reputation,
                encodedPassword, file
        ).orElseThrow(() -> {
            LOGGER.error("User with username {} already exist", userName);
            return UserAlreadyExistException.ofUsername(userName);
        });
        LOGGER.trace("Sending confirmation email to {}", email);
        emailSender.sendConfirmAccount(user, getConfirmationUrl(user), locale);
        return user;
    }

    @Transactional
    @Override
    public void remove(final String userName) {
        LOGGER.trace("Looking for user with username: {} to remove", userName);
        if (premiumUserDao.remove(userName)) {
            LOGGER.trace("{} removed", userName);
        } else {
            LOGGER.error("{} wasn't removed", userName);
            throw UserNotFoundException.ofUsername(userName);
        }
    }

    @Transactional
    @Override
    public Optional<byte[]> readImage(final String userName) {
        return premiumUserDao.readImage(userName);
    }

    @Transactional
    @Override
    public PremiumUser updateUserInfo(
            final String username, final String newFirstName, final String newLastName, final String newEmail,
            final String newCellphone, final LocalDate newBirthday, final String newCountry, final String newState,
            final String newCity, final String newStreet, final Integer newReputation, final String newPassword,
            final String oldPassword, final byte[] file, final Locale locale
    ) {
        LOGGER.trace("Looking for user with username: {} to update", username);
        if (premiumUserDao.findByEmail(newEmail).isPresent()) {
            LOGGER.trace("User with email {} already exist", newEmail);
            throw UserAlreadyExistException.ofEmail(newEmail);
        }

        if (newPassword != null) {
            PremiumUser premiumUser = findByUserName(username).orElseThrow(() -> {
                LOGGER.error("Can't find user with username: {}", username);
                return UserNotFoundException.ofUsername(username);
            });
            if (oldPassword != null && !bcrypt.matches(oldPassword, premiumUser.getPassword())) {
                throw WrongOldUserPasswordException.ofUsername(username);
            }
        }

        final String encodedPassword = (newPassword == null) ? null : bcrypt.encode(newPassword);
        PremiumUser user = premiumUserDao.updateUserInfo(
                newFirstName, newLastName, newEmail, username, newCellphone, newBirthday, newCountry, newState, newCity,
                newStreet, newReputation, encodedPassword, file, username
        ).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });

        if (newEmail != null) {
            emailSender.sendConfirmAccount(user, getConfirmationUrl(user), locale);
        }
        LOGGER.trace("User '{}' modified successfully", username);
        return user;
    }

    @Transactional
    @Override
    public PremiumUser enableUser(final String username, final String code) {
        LOGGER.trace("Looking for user with username {} to enable", username);

        PremiumUser user = findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });

        if (user.getEnabled()) {
            LOGGER.error("Can't enable user with username {}, is already enable", username);
            throw UserAlreadyIsEnableException.ofUsername(username);
        }

        if (!premiumUserDao.enableUser(user.getUserName(), code)) {
            LOGGER.error("Couldn't enable user {}, invalid code {}", username, code);
            throw InvalidUserCodeException.of(username, code);
        }
        LOGGER.trace("{} is now enabled", username);
        return user;
    }

    @Transactional
    @Override
    public boolean confirmationPath(String path) { //TODO: move to front
        String dataPath = path.replace("/confirm/", "");
        int splitIndex = dataPath.indexOf('&');
        String username = dataPath.substring(0, splitIndex);
        String code = dataPath.substring(splitIndex + 1);
        enableUser(username, code);
        return true;
    }

    @Transactional
    @Override
    public Page<PremiumUser> findUsersPage(final List<String> usernames, final List<String> sportLiked,
                                           final List<String> friendUsernames, final Integer minReputation,
                                           final Integer maxReputation, final Integer minWinRate,
                                           final Integer maxWinRate, final UserSort sort, final Integer offset,
                                           final Integer limit) {
        List<PremiumUser> users = premiumUserDao.findUsers(usernames, sportLiked, friendUsernames, minReputation,
                maxReputation, minWinRate, maxWinRate, sort);
        return new Page<>(users, offset, limit);
    }

    private double calculateWinRate(final PremiumUser user) {
        double played = 0;
        double wins = 0;
        double ties = 0;

        List<Game> gamesTeam = user.getGamesInTeam1();

        for (Game g : gamesTeam) {
            if (g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) > Integer.parseInt(value[1])) {
                    wins++;
                } else if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) == Integer.parseInt(value[1])) {
                    ties++;
                }
                played++;
            }
        }

        gamesTeam = user.getGamesInTeam2();

        for (Game g : gamesTeam) {
            if (g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) < Integer.parseInt(value[1])) {
                    wins++;
                } else if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) == Integer.parseInt(value[1])) {
                    ties++;
                }
                played++;
            }
        }

        if (played != 0) {
            return ((wins + 0.5 * ties) / played) * 100;
        }

        return -1;
    }

    private String getConfirmationUrl(PremiumUser user) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        formatter.format(environment.getRequiredProperty("url.frontend.confirm.account"),
                user.getUserName(), user.getCode());
        return stringBuilder.toString();
    }
}
