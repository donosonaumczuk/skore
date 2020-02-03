package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.UserSort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PremiumUserService {

    public  Optional<PremiumUser> findByUserName(final String userName);

    public  Optional<PremiumUser> findByEmail(final String email);

    public  Optional<PremiumUser> findById(final long userId);

    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password,
                                        final byte[] file, final Locale locales);

    public Optional<byte[]> readImage(final String userName);

    public boolean remove(final String userName);

    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                                final String newEmail,final String newUserName,
                                                final String newCellphone, final String newBirthday,
                                                final String newCountry, final String newState,
                                                final String newCity, final String newStreet,
                                                final int newReputation, final String newPassword,
                                                final byte[] file, final String oldUserName,
                                                final Locale locales);

    public Optional<PremiumUser> changePassword(final String newPassword, final String userName);

    public void addRole(final String username, final int roleId);

    public Optional<Boolean> enableUser(final String username, final String code);

    public boolean confirmationPath(String path);

    public Page<PremiumUser> findUsersPage(final List<String> usernames, final List<String> sportLiked,
                                           final List<String> friendUsernames, final Integer minReputation,
                                           final Integer maxReputation, final Integer minWinRate,
                                           final Integer maxWinRate, final UserSort sort, final Integer offset,
                                           final Integer limit);
}
