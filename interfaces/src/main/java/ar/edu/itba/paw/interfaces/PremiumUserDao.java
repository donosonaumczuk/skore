package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PremiumUserDao {

    public Optional<PremiumUser> findByUserName(final String userName);

    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password);

    public boolean remove(final String userName);

    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                                final String newEmail,final String newUserName,
                                                final String newCellphone, final String newBirthday,
                                                final String newCountry, final String newState,
                                                final String newCity, final String newStreet,
                                                final int newReputation, final String newPassword,
                                                final String oldUserName);


    public Optional<PremiumUser> findByEmail(final String email);

    public boolean addRole(final String username, final int roleId);

    public boolean enableUser(final String username, final String code);

    public List<Role> getRoles(final String username);

    }

