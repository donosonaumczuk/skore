package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<PremiumUser> findByUserName(final String userName) {
        final TypedQuery<PremiumUser> query = em.createQuery("FROM PremiumUser AS user WHERE user.userName = :username", PremiumUser.class);
        query.setParameter("username", userName);
        final List<PremiumUser> list = query.getResultList();
        PremiumUser user = list.isEmpty() ? null : list.get(0);
        if(user == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(user);
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

        final String code = new BCryptPasswordEncoder().encode(userName + email + LocalDateTime.now());
        final PremiumUser newUser = new PremiumUser(firstName, lastName, email, userName,
                cellphone, LocalDate.parse(birthday), new Place(country, state, city, street), reputation,
                password, code, ((file==null)?null:file.getBytes()));
        em.merge(newUser);
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
                                                final String oldUserName) {
        Optional<PremiumUser> currentUser = findByUserName(oldUserName);

        if(currentUser.isPresent()) {
            userDao.updateBasicUserInfo(currentUser.get().getUserId(), newFirstName, newLastName, newEmail);
            final PremiumUser user = currentUser.get();
            user.setFirstName(newFirstName);
            user.setLastName(newLastName);
            user.setEmail(newEmail);
            user.setUserName(newUserName);
            user.setCellphone(newCellphone);
            final Place newHome = new Place(newCountry, newState, newCity, newStreet);
            user.setHome(newHome);
            user.setReputation(newReputation);
            user.setPassword(newPassword);
            em.merge(user);
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }




    public Optional<PremiumUser> findByEmail(final String email) {
        final TypedQuery<PremiumUser> query = em.createQuery("from PremiumUser as user where user.email = :email", PremiumUser.class);
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

}
