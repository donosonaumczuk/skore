package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.UserSort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PremiumUserService {

    @Transactional
    public  Optional<PremiumUser> findByUserName(final String userName);

    @Transactional
    public  Optional<PremiumUser> findByEmail(final String email);

    @Transactional
    public  Optional<PremiumUser> findById(final long userId);

    @Transactional
    public Optional<PremiumUser> create(
            final String firstName, final String lastName, final String email, final String userName,
            final String cellphone, final String birthday, final String country, final String state, final String city,
            final String street, final int reputation, final String password, final byte[] file
    );

    @Transactional
    public Optional<byte[]> readImage(final String userName);

    @Transactional
    public boolean remove(final String userName);

    @Transactional
    public Optional<PremiumUser> updateUserInfo(
            final String username, final String newFirstName, final String newLastName, final String newEmail,
            final String newCellphone, final String newBirthday, final String newCountry, final String newState,
            final String newCity, final String newStreet, final Integer newReputation, final String newPassword,
            final String oldPassword, final byte[] file);

    @Transactional
    public void addRole(final String username, final int roleId);

    @Transactional
    public Optional<Boolean> enableUser(final String username, final String code);

    @Transactional
    public boolean confirmationPath(String path);

    @Transactional
    public Page<PremiumUser> findUsersPage(
            final List<String> usernames, final List<String> sportLiked, final List<String> friendUsernames,
            final Integer minReputation, final Integer maxReputation, final Integer minWinRate,
            final Integer maxWinRate, final UserSort sort, final Integer offset, final Integer limit
    );
}
