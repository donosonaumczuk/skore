package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.UserSort;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PremiumUserService {

    Optional<PremiumUser> findByUserName(final String userName);

    Optional<PremiumUser> findByEmail(final String email);

    Optional<PremiumUser> findById(final long userId);

    PremiumUser create(final String firstName, final String lastName,
					   final String email, final String username,
                       final String cellphone, final LocalDate birthday,
                       final String country, final String state, final String city,
                       final String street, final Integer reputation, final String password,
                       final byte[] file, final Locale locale);

    Optional<byte[]> readImage(final String userName);

    void remove(final String userName);

    PremiumUser updateUserInfo(final String username, final String newFirstName, final String newLastName,
                               final String newEmail, final String newCellphone, final LocalDate newBirthday,
                               final String newCountry, final String newState, final String newCity,
                               final String newStreet, final Integer newReputation, final String newPassword,
                               final String oldPassword, final byte[] file, final Locale locale);

    PremiumUser enableUser(final String username, final String code);

    Page<PremiumUser> findUsersPage(final List<String> usernames, final List<String> sportLiked,
                                    final List<String> friendUsernames, final Integer minReputation,
                                    final Integer maxReputation, final Integer minWinRate,
                                    final Integer maxWinRate, final UserSort sort, final Integer offset,
                                    final Integer limit);

    PremiumUser addLikedUser(String username, String username1);

    void removeLikedUser(String username, String usernameOfLiked);

    PremiumUser getLikedUser(String username, String usernameOfLiked);
}
