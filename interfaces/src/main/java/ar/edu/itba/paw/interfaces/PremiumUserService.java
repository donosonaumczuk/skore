package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PremiumUserService {
    @Transactional
    public  Optional<PremiumUser> findByUserName(final String userName);

    @Transactional
    public  Optional<PremiumUser> findByEmail(final String email);

    @Transactional
    public PremiumUser create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final String birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password);

    @Transactional
    public boolean remove(final String userName);

    @Transactional
    public PremiumUser updateUserInfo(final String newFirstName, final String newLastName,
                                      final String newEmail,final String newUserName,
                                      final String newCellphone, final String newBirthday,
                                      final String newCountry, final String newState,
                                      final String newCity, final String newStreet,
                                      final int newReputation, final String newPassword,
                                      final String oldUserName);
    @Transactional
    public void addRole(final String username, final int roleId);

    @Transactional
    public void enableUser(final String username, final String code);

    }
