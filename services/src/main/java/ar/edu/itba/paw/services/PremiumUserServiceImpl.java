package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidParameterException;
import ar.edu.itba.paw.exceptions.InvalidUserCodeException;
import ar.edu.itba.paw.exceptions.LackOfPermissionsException;
import ar.edu.itba.paw.exceptions.alreadyexists.LikeSportAlreadyExistException;
import ar.edu.itba.paw.exceptions.notfound.LikeSportNotFoundException;
import ar.edu.itba.paw.exceptions.UnauthorizedException;
import ar.edu.itba.paw.exceptions.WrongOldUserPasswordException;
import ar.edu.itba.paw.exceptions.alreadyexists.LikeUserAlreadyExistException;
import ar.edu.itba.paw.exceptions.alreadyexists.UserAlreadyExistException;
import ar.edu.itba.paw.exceptions.invalidstate.UserInvalidStateException;
import ar.edu.itba.paw.exceptions.notfound.LikeUserNotFoundException;
import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.UserSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Service
public class PremiumUserServiceImpl implements PremiumUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUserServiceImpl.class);

    @Autowired
    private PremiumUserDao premiumUserDao;

    @Autowired
    private SportService sportService;

    @Autowired
    private GameService gameService;

    @Autowired
    public EmailService emailSender;

    @Autowired
    public SessionService sessionService;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private Environment environment;

    private final Function<PremiumUser, PremiumUser> loadUserGames = user -> {
        List<List<Game>> games = gameService.getGamesThatPlay(user.getUser().getUserId());
        user.setGamesInTeam1(games.get(0));
        user.setGamesInTeam2(games.get(1));
        user.setWinRate(PremiumUserServiceImpl.this.calculateWinRate(user));
        return user;
    };

    @Transactional
    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        LOGGER.trace("Looking for user with username: {}", userName);
        return premiumUserDao.findByUserName(userName).map(loadUserGames);
    }

    @Transactional
    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        LOGGER.trace("Looking for user with email: {}", email);
        return premiumUserDao.findByEmail(email).map(loadUserGames);
    }

    @Transactional
    @Override
    public Optional<PremiumUser> findById(final long userId) {
        LOGGER.trace("Looking for user with id: {}", userId);
        return premiumUserDao.findById(userId).map(loadUserGames);
    }

    @Transactional
    @Override
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final LocalDate birthday,
                              final String country, final String state, final String city,
                              final String street, final Integer reputation, final String password,
                              final byte[] file, final Locale locale) {
        LOGGER.trace("Attempting to create user: {}", userName);
        if (birthday.isAfter(LocalDate.now())) {
            LOGGER.trace("Birthday must happen in the past");
            throw new InvalidParameterException("Birthday must happen in the past");
        }

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
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> new UnauthorizedException("Must be logged"));
        if (!loggedUser.getUserName().equals(userName)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), userName);
            throw new LackOfPermissionsException("User '" + userName + "' deletion failed, unauthorized");
        }
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
            final String oldPassword, final byte[] file, final Locale locale, final String code
    ) {
        LOGGER.trace("Looking for user with username: {} to update", username);
        if (code == null) {
            return updateUserInfoWithOutCode(username, newFirstName, newLastName, newEmail, newCellphone, newBirthday,
                    newCountry, newState, newCity, newStreet, newReputation, newPassword, oldPassword, file, locale);
        }
        if (newFirstName != null || newLastName != null || newEmail != null || newCellphone != null
                || newBirthday != null || newCountry != null || newState != null || newCity != null || newStreet != null
                || newReputation != null || oldPassword != null || file != null || newPassword == null) {
            LOGGER.trace("If recovery code is send it must only have newPassword");
            throw new InvalidParameterException("If recovery code is send it must only have newPassword");
        }
        return updateUserInfoWithCode(username,newPassword, code);
    }

    private PremiumUser updateUserInfoWithCode(String username, String newPassword, String code) {
        final String encodedPassword = (newPassword == null) ? null : bcrypt.encode(newPassword);
        PremiumUser premiumUser = premiumUserDao.resetPassword(username, encodedPassword, code).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        if (!bcrypt.matches(newPassword, premiumUser.getPassword())) {
            LOGGER.error("Couldn't rest user {} password, invalid code {}", username, code);
            throw InvalidUserCodeException.of(username, code);
        }
        LOGGER.trace("User '{}' reset password successfully", username);
        return premiumUser;
    }

    private PremiumUser updateUserInfoWithOutCode(
            final String username, final String newFirstName, final String newLastName, final String newEmail,
            final String newCellphone, final LocalDate newBirthday, final String newCountry, final String newState,
            final String newCity, final String newStreet, final Integer newReputation, final String newPassword,
            final String oldPassword, final byte[] file, final Locale locale
    ) {
        if (newBirthday != null && newBirthday.isAfter(LocalDate.now())) {
            LOGGER.trace("Birthday must happen in the past");
            throw new InvalidParameterException("Birthday must happen in the past");
        }

        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(
                () -> new UnauthorizedException("Must be logged to perform this operation"));
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new LackOfPermissionsException("User '" + username + "' update failed, unauthorized");
        }

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

        if (newEmail != null && !loggedUser.getEmail().equals(newEmail)) {
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
            throw UserInvalidStateException.ofUserAlreadyEnable(username);
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
    public Page<PremiumUser> findUsersPage(final List<String> usernames, final List<String> sportLiked,
                                           final List<String> friendUsernames, final Integer minReputation,
                                           final Integer maxReputation, final Integer minWinRate,
                                           final Integer maxWinRate, final UserSort sort, final Integer offset,
                                           final Integer limit, final boolean exactMatchUsernames) {
        List<PremiumUser> users = premiumUserDao.findUsers(usernames, sportLiked, friendUsernames, minReputation,
                maxReputation, minWinRate, maxWinRate, sort, exactMatchUsernames);
        return new Page<>(users, offset, limit);
    }

    @Transactional
    @Override
    public PremiumUser addLikedUser(final String username, final String usernameOfLiked) {
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> new UnauthorizedException("Must be logged"));
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new LackOfPermissionsException("User '" + username + "' add liked user failed, unauthorized");
        }
        PremiumUser likedUser = premiumUserDao.findByUserName(usernameOfLiked).orElseThrow(() -> {
            LOGGER.error("Can't find like with id: {}|{}", username, usernameOfLiked);
            return LikeUserNotFoundException.ofUsernames(username, usernameOfLiked);
        });

        if (!premiumUserDao.addLikedUser(username, usernameOfLiked)) {
            LOGGER.error("Like already exist with id: {}|{}", username, usernameOfLiked);
            throw LikeUserAlreadyExistException.ofUsernames(username, usernameOfLiked);
        }
        return likedUser;
    }

    @Transactional
    @Override
    public void removeLikedUser(final String username, final String usernameOfLiked) {
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> new UnauthorizedException("Must be logged"));
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new LackOfPermissionsException("User '" + username + "' remove liked user failed, unauthorized");
        }

        if (!premiumUserDao.removeLikedUser(username, usernameOfLiked)) {
            LOGGER.error("Can't find like with id: {}|{}", username, usernameOfLiked);
            throw  LikeUserNotFoundException.ofUsernames(username, usernameOfLiked);
        }
    }

    @Transactional
    @Override
    public PremiumUser getLikedUser(final String username, final String usernameOfLiked) {
        PremiumUser premiumUser = findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        for (PremiumUser liked : premiumUser.getFriends()) {
            if (liked.getUserName().equals(usernameOfLiked)) {
                return liked;
            }
        }
        LOGGER.error("Can't find like with id: {}|{}", username, usernameOfLiked);
        throw LikeUserNotFoundException.ofUsernames(username, usernameOfLiked);
    }

    @Transactional
    @Override
    public Page<PremiumUser> getLikedUsers(final String username, final Integer offset, final Integer limit) {
        List<PremiumUser> likedPremiumUsers = premiumUserDao.getLikedPremiumUsers(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        return new Page<>(likedPremiumUsers, offset, limit);
    }

    @Transactional
    @Override
    public Sport addLikedSport(final String username, final String sportName) {
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> new UnauthorizedException("Must be logged"));
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new LackOfPermissionsException("User '" + username + "' add liked sport failed, unauthorized");
        }
        Sport likedSport = sportService.findByName(sportName).orElseThrow(() -> {
            LOGGER.error("Can't find like with id: {}|{}", username, sportName);
            return LikeSportNotFoundException.ofUsernameAndSportName(username, sportName);
        });

        if (!premiumUserDao.addLikedSport(username, sportName)) {
            LOGGER.error("Like already exist with id: {}|{}", username, sportName);
            throw LikeSportAlreadyExistException.ofUsernameAndSportName(username, sportName);
        }
        return likedSport;
    }

    @Transactional
    @Override
    public Sport getLikedSport(final String username, final String sportnameOfLiked) {
        PremiumUser premiumUser = findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        for (Sport liked : premiumUser.getLikes()) {
            if (liked.getName().equals(sportnameOfLiked)) {
                return liked;
            }
        }
        LOGGER.error("Can't find like with id: {}|{}", username, sportnameOfLiked);
        throw LikeSportNotFoundException.ofUsernameAndSportName(username, sportnameOfLiked);
    }

    @Transactional
    @Override
    public void removeLikedSport(final String username, final String sportnameOfLiked) {
        PremiumUser loggedUser = sessionService.getLoggedUser().orElseThrow(() -> new UnauthorizedException("Must be logged"));
        if (!loggedUser.getUserName().equals(username)) {
            LOGGER.trace("User '{}' is not user '{}'", loggedUser.getUserName(), username);
            throw new LackOfPermissionsException("User '" + username + "' remove liked sport failed, unauthorized");
        }

        if (!premiumUserDao.removeLikedSport(username, sportnameOfLiked)) {
            LOGGER.error("Can't find like with id: {}|{}", username, sportnameOfLiked);
            throw  LikeSportNotFoundException.ofUsernameAndSportName(username, sportnameOfLiked);
        }
    }

    @Transactional
    @Override
    public Page<Sport> getLikedSports(final String username, final Integer offset, final Integer limit) {
        List<Sport> likedSports = premiumUserDao.getLikedSports(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        return new Page<>(likedSports, offset, limit);
    }

    @Transactional
    @Override
    public void forgotPassword(final String username, final String email, final Locale locale) {
        PremiumUser premiumUser = findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });

        if (!premiumUser.getEmail().equals(email)) {
            LOGGER.trace("Email '{}' is invalid for '{}'", email, username);
            throw UserInvalidStateException.ofUserWithWrongMail(username, email);
        }

        emailSender.sendResetPassword(premiumUser, getResetPasswordUrl(premiumUser), locale);
    }

    private double calculateWinRate(final PremiumUser user) {
        double played = 0;
        double wins = 0;

        List<Game> gamesTeam = user.getGamesInTeam1();

        for (Game g : gamesTeam) {
            if (g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) > Integer.parseInt(value[1])) {
                    wins++;
                }
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) != Integer.parseInt(value[1])) {
                    played++;
                }
            }
        }

        gamesTeam = user.getGamesInTeam2();

        for (Game g : gamesTeam) {
            if (g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) < Integer.parseInt(value[1])) {
                    wins++;
                }
                if (g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) != Integer.parseInt(value[1])) {
                    played++;
                }
            }
        }

        if (played != 0) {
            return (wins / played) * 100;
        }

        return 0;
    }

    private String getConfirmationUrl(PremiumUser user) {
        return getAccountUrl(user, environment.getRequiredProperty(environment.getRequiredProperty("state")
                + ".url.frontend.confirm.account"));
    }

    private String getResetPasswordUrl(PremiumUser user) {
        return getAccountUrl(user, environment.getRequiredProperty(environment.getRequiredProperty("state")
                + ".url.frontend.reset.password"));
    }

    private String getAccountUrl(PremiumUser user, String template) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);
        formatter.format(template, user.getUserName(), user.getCode());
        return stringBuilder.toString();
    }
}
