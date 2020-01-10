package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PremiumUserServiceImpl implements PremiumUserService{

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
        LOGGER.trace("Looking for user with username: {}",userName);

        Optional<PremiumUser> user = premiumUserDao.findByUserName(userName);
        if(user.isPresent()) {
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

        if(!user.isPresent()) {
            LOGGER.error("Can't find user with email: {}", email);
        }
        return user;
    }

    @Override
    public  Optional<PremiumUser> findById(final long userId) {
        LOGGER.trace("Looking for user with id: {}", userId);
        Optional<PremiumUser> user = premiumUserDao.findById(userId);

        if(!user.isPresent()) {
            LOGGER.error("Can't find user with id: {}", userId);
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
        final String encodedPassword = bcrypt.encode(password);
        LOGGER.trace("Creating user");

        final String formattedBirthday = formatDate(birthday);
        Optional<PremiumUser> user = premiumUserDao.create(firstName, lastName, email, userName,
                cellphone, formattedBirthday, country, state, city, street, reputation,
                encodedPassword, file);
        PremiumUser ans = user
                .orElseThrow(() -> new CannotCreateUserException("Can't create user with with userName: " + userName ));
        LOGGER.trace("Sending confirmation email to {}", email);
        sendConfirmationMail(ans);
        return ans;
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
    public Optional<byte[]> readImage(final String userName) {
        return premiumUserDao.readImage(userName);
    }

    @Override
    public PremiumUser updateUserInfo(final String newFirstName, final String newLastName,
                                      final String newEmail,final String newUserName,
                                      final String newCellphone, final String newBirthday,
                                      final String newCountry, final String newState,
                                      final String newCity, final String newStreet,
                                      final int newReputation, final String newPassword,
                                      final MultipartFile file, final String oldUserName) throws IOException{

        LOGGER.trace("Looking for user with username: {} to update", oldUserName);

        Optional<PremiumUser> user = premiumUserDao.updateUserInfo(newFirstName, newLastName,
                newEmail, newUserName, newCellphone, newBirthday, newCountry, newState,
                newCity, newStreet, newReputation, newPassword, file, oldUserName);

        return user.orElseThrow(() -> new UserNotFoundException("User with userName: " + oldUserName + "doesn't exist."));
    }

    @Override
    public PremiumUser changePassword(final String newPassword, final String username) throws IOException{
        Optional <PremiumUser> premiumUser = findByUserName(username);
        PremiumUser currentUser = premiumUser.orElseThrow(() -> new UserNotFoundException("Can't find user" +
                "with username:" + username));
        final String encodedPassword = bcrypt.encode(newPassword);
        DateTimeFormatter expectedFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Optional<PremiumUser> user = premiumUserDao.updateUserInfo(currentUser.getUser().getFirstName(),
                currentUser.getUser().getLastName(), currentUser.getEmail(), currentUser.getUserName(),
                currentUser.getCellphone(), currentUser.getBirthday().format(expectedFormat), currentUser.getHome().getCountry(),
                currentUser.getHome().getState(),currentUser.getHome().getCity(), currentUser.getHome().getStreet(),
                currentUser.getReputation(), encodedPassword, null, username);

        return user.orElseThrow(() -> new CannotModifyUserException("Can't modify user with username:" + username));
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

        Role ans = role.orElseThrow(() ->
                new ar.edu.itba.paw.exceptions.RoleNotFoundException("can't find role with id: " + roleId));
        LOGGER.trace("Looking for user with username: {}", username);
        user = premiumUserDao.findByUserName(username);
        user.orElseThrow(() -> new UserNotFoundException("Can't find user with username: " + username));

        LOGGER.trace("Adding role {} to user with username: {}", ans.getName(), username);
        premiumUserDao.addRole(username, roleId);
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
        emailSender.sendConfirmAccount(user, generatePath(user), LocaleContextHolder.getLocale());
    }

    private double calculateWinRate(final PremiumUser user) {
        double played = 0;
        double wins = 0;
        double ties = 0;

        List<Game> gamesTeam = user.getGamesInTeam1();

        for(Game g : gamesTeam) {
            if(g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if(g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) > Integer.parseInt(value[1])) {
                    wins++;
                }
                else if(g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) == Integer.parseInt(value[1])) {
                    ties++;
                }
                played++;
            }
        }

        gamesTeam = user.getGamesInTeam2();

        for(Game g : gamesTeam) {
            if(g.getResult() != null) {
                String[] value = g.getResult().split("-");
                if(g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) < Integer.parseInt(value[1])) {
                    wins++;
                }
                else if(g.getType().split("-")[1].equals("Competitive") &&
                        Integer.parseInt(value[0]) == Integer.parseInt(value[1])) {
                    ties++;
                }
                played++;
            }
        }

        if(played != 0) {
            return ((wins + 0.5 * ties)/played) * 100;
        }

        return -1;
    }
}
