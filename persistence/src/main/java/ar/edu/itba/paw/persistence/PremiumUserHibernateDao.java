package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PremiumUserHibernateDao implements PremiumUserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    private static final String QUERY_PART_1          = "Select u From PremiumUser as u";
    private static final String QUERY_PART_2          = ", PremiumUser as u2";
    private static final String QUERY_PART_3          = ", Sport s";
    private static final String QUERY_PART_4          = " WHERE u.userName = u.userName";
    private static final String QUERY_REPUTATION_NAME = "u.reputation";
    private static final String LESS_THAN             = "<";
    private static final String GREATER_THAN          = ">";
    private static final String MIN_REPUTATION        = "minReputation";
    private static final String MAX_REPUTATION        = "maxReputation";
    private static final String EQUALS                = "=";
    private static final String QUERY_USERNAME_NAME   = "u.userName";
    private static final String QUERY_LIKES_NAME      = "elements(u.likes)";
    private static final String QUERY_FRIENDS_NAME    = "elements(u2.friends)";
    private static final String LIKES_OPERATOR        = "= s.sportName AND s IN";
    private static final String FRIENDS_OPERATOR      = "= u2.userName AND u IN";
    private static final String USERNAME              = "username";
    private static final String SPORT                 = "sport";
    private static final String USERNAME_FRIENDS      = "usernameFriends";
    private static final String userRole              = "ROLE_USER";
    private static final int userRoleId               = 0;

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        PremiumUser premiumUser = em.find(PremiumUser.class, userName);

        if(premiumUser == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(premiumUser);
        }
    }

    @Override
    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final LocalDate birthday,
                                        final String country, final String state, final String city,
                                        final String street, final Integer reputation, final String password,
                                        final byte[] file) {
        if (findByUserName(userName).isPresent()) {
            return Optional.empty();
        }

        final Optional<User> basicUser = userDao.create(firstName, lastName, email);

        if (!basicUser.isPresent()) {
            return Optional.empty();
        }

        final String code = new BCryptPasswordEncoder().encode(userName + email + LocalDateTime.now());
        final Role role = roleDao.findRoleById(userRoleId).get();//should never be empty
        final PremiumUser newUser = new PremiumUser(basicUser.get().getFirstName(), basicUser.get().getLastName(),
                basicUser.get().getEmail(), userName, cellphone, birthday, new Place(country,
                state, city, street), reputation, password, code, file);
        newUser.addRole(role);
        em.persist(newUser);
        return Optional.of(newUser);
    }

    @Override
    public boolean remove(final String userName) {
        final Optional<PremiumUser> userOptional = findByUserName(userName);
        userOptional.ifPresent(user -> em.remove(user));
        return userOptional.isPresent();
    }

    @Override
    public Optional<byte[]> readImage(final String userName) {
        return Optional.ofNullable(
                findByUserName(userName)
                        .orElseThrow(() -> UserNotFoundException.ofUsername(userName))
                        .getImage()
        );
    }

    @Override
    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                                final String newEmail,final String newUserName,
                                                final String newCellphone, final LocalDate newBirthday,
                                                final String newCountry, final String newState,
                                                final String newCity, final String newStreet,
                                                final Integer newReputation, final String newPassword,
                                                final byte[] file, final String oldUserName) {
        Optional<PremiumUser> currentUser = findByUserName(oldUserName);

        if (currentUser.isPresent()) {
            final PremiumUser user = currentUser.get();
            if (newFirstName != null) {
                user.getUser().setFirstName(newFirstName);
            }
            if (newLastName != null) {
                user.getUser().setLastName(newLastName);
            }
            if (newEmail != null) {
                user.getUser().setEmail(newEmail);
                user.setEmail(newEmail);
                user.setEnabled(false);
            }
            if (newUserName != null) {
                user.setUserName(newUserName);
            }
            if (newCellphone != null) {
                user.setCellphone(newCellphone);
            }
            if (newCountry != null) {
                user.getHome().setCountry(newCountry);
            }
            if (newState != null) {
                user.getHome().setState(newState);
            }
            if (newCity != null) {
                user.getHome().setCity(newCity);
            }
            if (newStreet != null) {
                user.getHome().setStreet(newStreet);
            }
            if (newReputation != null) {
                user.setReputation(newReputation);
            }
            if (newBirthday != null) {
                user.setBirthday(newBirthday);
            }
            if (newPassword != null) {
                user.setPassword(newPassword);
            }
            if (file != null) {
                user.setImage(file);
            }
            em.merge(user);
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        final TypedQuery<PremiumUser> query = em.createQuery("from PremiumUser as u where " +
                                                "u.email = :email", PremiumUser.class);
        query.setParameter("email", email);
        final List<PremiumUser> list = query.getResultList();
        PremiumUser user = list.isEmpty() ? null : list.get(0);
        if(user == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(user);
        }
    }

    @Override
    public Optional<PremiumUser> findById(final long userId) {
        final TypedQuery<PremiumUser> query = em.createQuery("from PremiumUser as u where " +
                "u.user.userId = :userId", PremiumUser.class);
        query.setParameter("userId", userId);
        final List<PremiumUser> list = query.getResultList();
        PremiumUser user = list.isEmpty() ? null : list.get(0);
        if(user == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(user);
        }
    }

    @Override
    public boolean enableUser(final String username, final String code) {
        Optional<PremiumUser> currentUser = findByUserName(username);

        if(currentUser.isPresent() && currentUser.get().getCode().equals(code)) {
            final PremiumUser user = currentUser.get();
            user.setEnabled(true);
            em.merge(user);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean addRole(final String username, final int roleId) {
        Optional<Role> role = roleDao.findRoleById(roleId);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(!role.isPresent() || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        user.getRoles().add(role.get());
        em.merge(user);
        return true;
    }

    @Override
    public Set<Role> getRoles(final String username) {
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(!premiumUser.isPresent()) {
            return null;
        }

        PremiumUser user = premiumUser.get();
        return user.getRoles();
    }

    @Override
    public boolean removeRole(final String username, final int roleId) {
        Role role = em.find(Role.class, roleId);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(role == null || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            em.merge(user);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<PremiumUser> findUsers(final List<String> usernames, final List<String> sportLiked,
                                       final List<String> friendUsernames, final Integer minReputation,
                                       final Integer maxReputation, final Integer minWinRate,
                                       final Integer maxWinRate, final UserSort sort, final boolean exactMatchUsernames) {
        StringBuilder queryStart = new StringBuilder(QUERY_PART_1);
        if (friendUsernames != null && !friendUsernames.isEmpty()) {
            queryStart = queryStart.append(QUERY_PART_2);
        }
        if (sportLiked != null && !sportLiked.isEmpty()) {
            queryStart = queryStart.append(QUERY_PART_3);
        }
        queryStart = queryStart.append(QUERY_PART_4);

        DaoHelper daoHelper = new DaoHelper(queryStart.toString());
        daoHelper.addFilter(QUERY_REPUTATION_NAME, LESS_THAN, MIN_REPUTATION, minReputation);
        daoHelper.addFilter(QUERY_REPUTATION_NAME, GREATER_THAN, MAX_REPUTATION, maxReputation);
        //TODO: winrate filter and Sort, need base migration
        if (exactMatchUsernames) {
            daoHelper.addFilter(QUERY_USERNAME_NAME, EQUALS, USERNAME, usernames);
        }
        else {
            daoHelper.addListFilters(false, false, QUERY_USERNAME_NAME, USERNAME, usernames);
        }
        daoHelper.addFilter(QUERY_LIKES_NAME, LIKES_OPERATOR, SPORT, sportLiked);
        daoHelper.addFilter(QUERY_FRIENDS_NAME, FRIENDS_OPERATOR, USERNAME_FRIENDS, friendUsernames);

        final TypedQuery<PremiumUser> query = em.createQuery(daoHelper.getQuery() +
                (sort != null ? sort.toQuery() : ""), PremiumUser.class);
        List<String> valueName = daoHelper.getValueNames();
        List<Object> values    = daoHelper.getValues();

        for(int i = 0; i < valueName.size(); i++) {
            query.setParameter(valueName.get(i), values.get(i));
        }

        return query.getResultList();
    }

    @Override
    public boolean addLikedUser(String username, String usernameOfLiked) {
        Optional<PremiumUser> premiumUser = findByUserName(username);
        Optional<PremiumUser> premiumUserOfLiked = findByUserName(usernameOfLiked);

        if(!premiumUserOfLiked.isPresent() || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getFriends().contains(premiumUserOfLiked.get())) {
            return false;
        }
        else {
            user.getFriends().add(premiumUserOfLiked.get());
            em.merge(user);
            return true;
        }
    }

    @Override
    public boolean removeLikedUser(String username, String usernameOfLiked) {
        Optional<PremiumUser> premiumUser = findByUserName(username);
        Optional<PremiumUser> premiumUserOfLiked = findByUserName(usernameOfLiked);

        if(!premiumUserOfLiked.isPresent() || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getFriends().contains(premiumUserOfLiked.get())) {
            user.getFriends().remove(premiumUserOfLiked.get());
            em.merge(user);
        }
        return true;
    }

    @Override
    public boolean addLikedSport(final String username, final String sportName) {
        Sport sport = em.find(Sport.class, sportName);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(sport == null || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getLikes().contains(sport)) {
            return false;
        }
        else {
            user.getLikes().add(sport);
            em.merge(user);
            return true;
        }
    }

    @Override
    public boolean removeLikedSport(final String username, final String sportnameOfLiked) {
        Sport sport = em.find(Sport.class, sportnameOfLiked);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(sport == null || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getLikes().contains(sport)) {
            user.getLikes().remove(sport);
            em.merge(user);
        }
        return true;
    }

    @Override
    public Optional<List<Sport>> getLikedSports(final String username) {
        Optional<PremiumUser> premiumUser = findByUserName(username);
        return premiumUser.map(PremiumUser::getLikes);
    }
}
