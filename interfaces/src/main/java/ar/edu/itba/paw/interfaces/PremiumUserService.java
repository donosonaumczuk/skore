package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PremiumUserService {

    public PremiumUser findById(final String userName);

    public PremiumUser create(final String userName, final String cellphone,
                              final LocalDateTime birthday, final Place home,
                              final int reputation, final String password);

    public boolean remove(final long userId);

    public PremiumUser updateUserInfo(final String userName, final String cellphone,
                                      final LocalDateTime birthday, final Place home,
                                      final int reputation, final String password);

}
