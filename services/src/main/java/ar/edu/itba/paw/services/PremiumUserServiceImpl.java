package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.UserSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        LOGGER.trace("Looking for user with username: {}", userName);

        Optional<PremiumUser> user = premiumUserDao.findByUserName(userName);
        if (user.isPresent()) {
            List<List<Game>> listsOfgames = gameService.getGamesThatPlay(user.get().getUser().getUserId());
            user.get().setGamesInTeam1(listsOfgames.get(0));
            user.get().setGamesInTeam2(listsOfgames.get(1));
            user.get().setWinRate(calculateWinRate(user.get()));
        }
        return user;
    }

    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        LOGGER.trace("Looking for user with email: {}", email);
        Optional<PremiumUser> user = premiumUserDao.findByEmail(email);

        if (!user.isPresent()) {
            LOGGER.error("Can't find user with email: {}", email);
        }
        return user;
    }

    @Override
    public Optional<PremiumUser> findById(final long userId) {
        LOGGER.trace("Looking for user with id: {}", userId);
        Optional<PremiumUser> user = premiumUserDao.findById(userId);

        if (!user.isPresent()) {
            LOGGER.error("Can't find user with id: {}", userId);
        }
        return user;
    }

    @Override
    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password,
                                        final byte[] file) {
        final String encodedPassword = bcrypt.encode(password);
        LOGGER.trace("Creating user");

        Optional<PremiumUser> user = premiumUserDao.create(firstName, lastName, email, userName,
                cellphone, birthday, country, state, city, street, reputation,
                encodedPassword, file);
        LOGGER.trace("Sending confirmation email to {}", email);
        user.ifPresent(this::sendConfirmationMail);
        return user;
    }

    @Override
    public boolean remove(final String userName) {
        LOGGER.trace("Looking for user with username: {} to remove", userName);
        boolean returnedValue = premiumUserDao.remove(userName);
        if (returnedValue) {
            LOGGER.trace("{} removed", userName);
        } else {
            LOGGER.trace("{} wasn't removed", userName);
        }
        return returnedValue;
    }

    @Override
    public Optional<byte[]> readImage(final String userName) {
        return premiumUserDao.readImage(userName);
    }

    @Override
    public Optional<PremiumUser> updateUserInfo(
            final String username, final String newFirstName, final String newLastName, final String newEmail,
            final String newCellphone, final String newBirthday, final String newCountry, final String newState,
            final String newCity, final String newStreet, final Integer newReputation, final String newPassword,
            final String oldPassword, final byte[] file
    ) {

        LOGGER.trace("Looking for user with username: {} to update", username);

        if (newPassword != null) {
            //TODO: validations like != ""
            PremiumUser premiumUser = findByUserName(username).orElseThrow(() -> UserNotFoundException.ofUsername(username)); //TODO: this will be improved in other PR
            if (oldPassword != null && !bcrypt.matches(oldPassword, premiumUser.getPassword())) {
                throw new IllegalArgumentException("Wrong old password"); //TODO: discuss! Will be improved in other PR
            }
        }

        final String encodedPassword = (newPassword == null) ? null : bcrypt.encode(newPassword);
        Optional<PremiumUser> user = premiumUserDao.updateUserInfo(newFirstName, newLastName,
                newEmail, username, newCellphone, newBirthday, newCountry, newState,
                newCity, newStreet, newReputation, encodedPassword, file, username);

        if (user.isPresent() && newEmail != null) {
            sendConfirmationMail(user.get());
        }

        return user;
    }

    @Override
    public Optional<Boolean> enableUser(final String username, final String code) {
        LOGGER.trace("Looking for user with username {} to enable", username);

        Optional<PremiumUser> user = findByUserName(username);
        if (!user.isPresent()) {
            LOGGER.error("Can't find user with username {}", username);
            return Optional.empty();
        }

        PremiumUser currentUser = user.get();

        if (!premiumUserDao.enableUser(currentUser.getUserName(), code)) {
            LOGGER.error("Can't find user with username {} and code {}", username, code);
            return Optional.of(false);
        }
        LOGGER.trace("{} is now enabled", username);
        return Optional.of(true);
    }

    @Override
    public boolean confirmationPath(String path) { //TODO: move to front
        String dataPath = path.replace("/confirm/", "");
        int splitIndex = dataPath.indexOf('&');
        String username = dataPath.substring(0, splitIndex);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if (!premiumUser.isPresent()) {
            return false;
        }

        String code = dataPath.substring(splitIndex + 1, dataPath.length());
        return enableUser(username, code).get();
    }

    private String generatePath(PremiumUser user) {
        return "confirm/" + user.getUserName() + "&" + user.getCode();
    }

    public void sendConfirmationMail(PremiumUser user) {
        emailSender.sendConfirmAccount(user, generatePath(user), LocaleContextHolder.getLocale());
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
}
