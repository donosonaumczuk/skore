package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.UserSort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PremiumUserDao {

    Optional<PremiumUser> findByUserName(final String userName);

    Optional<PremiumUser> findById(final long userId);

    Optional<PremiumUser> create(final String firstName, final String lastName,
                                 final String email, final String userName,
                                 final String cellphone, final LocalDate birthday,
                                 final String country, final String state, final String city,
                                     final String street, final Integer reputation, final String password,
                                 final byte[] file);

    boolean remove(final String userName);

    Optional<byte[]> readImage(final String userName);

    Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                         final String newEmail, final String newUserName,
                                         final String newCellphone, final LocalDate newBirthday,
                                         final String newCountry, final String newState,
                                         final String newCity, final String newStreet,
                                         final Integer newReputation, final String newPassword,
                                         final byte[] file, final String oldUserName);


    Optional<PremiumUser> findByEmail(final String email);

    boolean addRole(final String username, final int roleId);

    boolean enableUser(final String username, final String code);

    Set<Role> getRoles(final String username);

    boolean removeRole(final String username, final int roleId);

    List<Sport> getSports(String username);

    boolean addSport(final String username, String sportName);

    boolean removeSport(final String username, String sportName);

    List<PremiumUser> findUsers(final List<String> usernames, final List<String> sportLiked,
                                final List<String> friendUsernames, final Integer minReputation,
                                final Integer maxReputation, final Integer minWinRate,
                                final Integer maxWinRate, final UserSort sort);

    boolean addLikedUser(String username, String usernameOfLiked);

    boolean removeLikedUser(String username, String usernameOfLiked);
}

