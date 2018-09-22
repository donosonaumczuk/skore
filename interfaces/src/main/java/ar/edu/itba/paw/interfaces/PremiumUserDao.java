package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PremiumUserDao {
    public Optional<PremiumUser> findByUserName(final String userName);

    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final LocalDateTime birthday,
                                        final Place home, final int reputation, final String password);

    public boolean remove(final String userName);

    public Optional<PremiumUser> updateUserInfo(final String userName, final String cellphone,
                                                final LocalDateTime birthday, final Place home,
                                                final int reputation, final String password);

}
