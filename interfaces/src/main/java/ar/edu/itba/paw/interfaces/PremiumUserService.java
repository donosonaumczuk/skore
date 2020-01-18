package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PremiumUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

public interface PremiumUserService {

    @Transactional
    public  Optional<PremiumUser> findByUserName(final String userName);

    @Transactional
    public  Optional<PremiumUser> findByEmail(final String email);

    @Transactional
    public  Optional<PremiumUser> findById(final long userId);

    @Transactional
    public Optional<PremiumUser> create(final String firstName, final String lastName,
                              final String email, final String userName,
                              final String cellphone, final String birthday,
                              final String country, final String state, final String city,
                              final String street, final int reputation, final String password,
                              final byte[] file);

    @Transactional
    public Optional<byte[]> readImage(final String userName);

    @Transactional
    public boolean remove(final String userName);

    @Transactional
    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                      final String newEmail,final String newUserName,
                                      final String newCellphone, final String newBirthday,
                                      final String newCountry, final String newState,
                                      final String newCity, final String newStreet,
                                      final int newReputation, final String newPassword,
                                      final byte[] file, final String oldUserName);

    @Transactional
    public Optional<PremiumUser> changePassword(final String newPassword, final String userName);

    @Transactional
    public void addRole(final String username, final int roleId);

    @Transactional
    public Optional<Boolean> enableUser(final String username, final String code);

    @Transactional
    public boolean confirmationPath(String path);
}
