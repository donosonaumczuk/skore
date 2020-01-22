package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Filters;
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

    private static final String userRole = "ROLE_USER";
    private static final int userRoleId = 0;

    public Optional<PremiumUser> findByUserName(final String userName) {
        PremiumUser premiumUser = em.find(PremiumUser.class, userName);

        if(premiumUser == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(premiumUser);
        }
    }


    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password,
                                        final byte[] file) {
        if(findByUserName(userName).isPresent()) {
            return Optional.empty();
        }

        final Optional<User> basicUser = userDao.create(firstName, lastName, email);

        if(!basicUser.isPresent()) {
            return Optional.empty();
        }

        final String code = new BCryptPasswordEncoder().encode(userName + email + LocalDateTime.now());
        final Role role = roleDao.findRoleById(userRoleId).get();//should never be empty
        final PremiumUser newUser = new PremiumUser(basicUser.get().getFirstName(), basicUser.get().getLastName(),
                basicUser.get().getEmail(), userName, cellphone, LocalDate.parse(birthday), new Place(country,
                state, city, street), reputation, password, code, file);
        newUser.addRole(role);
        em.persist(newUser);
        return Optional.of(newUser);
    }

    public boolean remove(final String userName) {
        Optional<PremiumUser> user = findByUserName(userName);

        if(user.isPresent()) {
            em.remove(user.get());
            return true;
        }
        return false;
    }

    public Optional<byte[]> readImage(final String userName) {
        Optional<PremiumUser> premiumUser = findByUserName(userName);
        if(premiumUser.isPresent()) {
            PremiumUser user = premiumUser.get();
            byte image[] = user.getImage();
            if(image != null) {
                return Optional.of(image);
            }
        }

        return  Optional.empty();
    }

    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                                final String newEmail,final String newUserName,
                                                final String newCellphone, final String newBirthday,
                                                final String newCountry, final String newState,
                                                final String newCity, final String newStreet,
                                                final int newReputation, final String newPassword,
                                                final byte[] file, final String oldUserName) {
        Optional<PremiumUser> currentUser = findByUserName(oldUserName);

        if(currentUser.isPresent()) {
            final PremiumUser user = currentUser.get();
            user.getUser().setFirstName(newFirstName);
            user.getUser().setLastName(newLastName);
            if(newEmail != null) {
                user.getUser().setEmail(newEmail);
                user.setEmail(newEmail);
                user.setEnabled(false);
            }
            if(newUserName != null) {
                user.setUserName(newUserName);
            }
            user.setCellphone(newCellphone);
            final Place newHome = new Place(newCountry, newState, newCity, newStreet);
            user.setHome(newHome);
            user.setReputation(newReputation);
            user.setBirthday(LocalDate.parse(newBirthday));

            if(newPassword != null) {
                user.setPassword(newPassword);
            }

            if(file != null) {
                user.setImage(file);
            }

            em.merge(user);
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }

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

    public Set<Role> getRoles(final String username) {
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(!premiumUser.isPresent()) {
            return null;
        }

        PremiumUser user = premiumUser.get();
        return user.getRoles();
    }



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


    public boolean addSport(final String username, String sportName) {
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

    public List<Sport> getSports(String username) {
        Optional<PremiumUser> premiumUser = findByUserName(username);
        if(premiumUser.isPresent()) {
            return premiumUser.get().getLikes();
        }
        else return null;
    }

    public boolean removeSport(final String username, String sportName) {
        Sport sport = em.find(Sport.class, sportName);
        Optional<PremiumUser> premiumUser = findByUserName(username);

        if(sport == null || !premiumUser.isPresent()) {
            return false;
        }

        PremiumUser user = premiumUser.get();
        if(user.getLikes().contains(sport)) {
            user.getLikes().remove(sport);
            em.merge(user);
            return true;
        }
        else {
            return false;
        }

    }

    public List<PremiumUser> findUsers(final List<String> usernames, final List<String> sportLiked,
                                       final List<String> friendUsernames, final Integer minReputation,
                                       final Integer maxReputation, final Integer minWinRate,
                                       final Integer maxWinRate, final UserSort sort) {
        StringBuilder queryStart = new StringBuilder("Select u From PremiumUser as u");
        if (friendUsernames != null && !friendUsernames.isEmpty()) {
            queryStart = queryStart.append(", PremiumUser as u2");
        }
        if (sportLiked != null && !sportLiked.isEmpty()) {
            queryStart = queryStart.append(", Sport s");
        }
        queryStart = queryStart.append(" WHERE u.userName = u.userName");

        Filters  filter = new Filters(queryStart.toString());
        filter.addFilter("u.reputation", "<", "minReputation", minReputation);
        filter.addFilter("u.reputation", ">", "maxReputation", maxReputation);
        //TODO: winrate filter and Sort, need base migration
        filter.addFilter("u.userName", "=", "username", usernames);
        filter.addFilter("elements(u.likes)", "= s.sportName AND s IN", "sport", sportLiked);
        filter.addFilter("elements(u.friends)", "= u2.userName AND u2 IN", "usernameFriends", friendUsernames);

        final TypedQuery<PremiumUser> query = em.createQuery(filter.toString() +
                (sort != null ? sort.toQuery() : ""), PremiumUser.class);
        List<String> valueName = filter.getValueNames();
        List<Object> values    = filter.getValues();

        for(int i = 0; i < valueName.size(); i++) {
            query.setParameter(valueName.get(i), values.get(i));
        }

        return query.getResultList();
    }
}
