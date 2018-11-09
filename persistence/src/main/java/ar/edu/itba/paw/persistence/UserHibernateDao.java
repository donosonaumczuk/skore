package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserHibernateDao {

    @PersistenceContext
    private EntityManager em;

    public Optional<User> findById(final long userId) {
        final User returnedUser = em.find(User.class, userId);

        if(returnedUser != null) {
            return Optional.of(returnedUser);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<User> create(final String firstName, final String lastName, final String email) {
        final User newUser = new User(firstName, lastName, email);
        em.persist(newUser);
        return Optional.of(newUser);
    }

    public boolean remove(final long userId) {
        Optional<User> user = findById(userId);

        if(user.isPresent()) {
            em.remove(user.get());
            return true;
        }
        return false;
    }

    public Optional<User> updateBasicUserInfo(final long userId, final String newFirstName,
                                              final String newLastName, final String newEmail) {
       Optional<User> user = findById(userId);

       if(user.isPresent()) {
           User currentUser = user.get();
           currentUser.setFirstName(newFirstName);
           currentUser.setLastName(newLastName);
           currentUser.setEmail(newEmail);
           em.merge(currentUser);
           return Optional.of(currentUser);
       }

       return Optional.empty();

    }

    public Optional<User> updateFirstName(final long userId, final String newFirstName) {
        Optional<User> user = findById(userId);

        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setFirstName(newFirstName);

            em.merge(currentUser);
            return Optional.of(currentUser);
        }

        return Optional.empty();
    }

    public Optional<User> updateLastName(final long userId, final String newLastName) {
        Optional<User> user = findById(userId);

        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setLastName(newLastName);

            em.merge(currentUser);
            return Optional.of(currentUser);
        }

        return Optional.empty();
    }

    public Optional<User> updateEmail(final long userId, final String newEmail) {
        Optional<User> user = findById(userId);

        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setEmail(newEmail);

            em.merge(currentUser);
            return Optional.of(currentUser);
        }

        return Optional.empty();
    }

}
