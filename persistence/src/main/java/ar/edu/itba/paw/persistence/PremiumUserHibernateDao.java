package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.*;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
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
    UserHibernateDao userDao;

    @Autowired
    RoleHibernateDao roleDao;

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
                                        final MultipartFile file) throws IOException {
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
                state, city, street), reputation, password, code, ((file==null)?null:file.getBytes()));
        newUser.addRole(role);
        //em.persist(basicUser.get());
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
                                                final MultipartFile file, final String oldUserName) throws IOException {
        Optional<PremiumUser> currentUser = findByUserName(oldUserName);

        if(currentUser.isPresent()) {
            final PremiumUser user = currentUser.get();
            user.getUser().setFirstName(newFirstName);
            user.getUser().setLastName(newLastName);
            user.getUser().setEmail(newEmail);
            user.setUserName(newUserName);
            user.setCellphone(newCellphone);
            final Place newHome = new Place(newCountry, newState, newCity, newStreet);
            user.setHome(newHome);
            user.setReputation(newReputation);
            user.setPassword(newPassword);

            if(file != null) {
                user.setImage(file.getBytes());
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

}
