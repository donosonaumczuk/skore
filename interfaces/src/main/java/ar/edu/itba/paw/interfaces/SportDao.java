package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;

import java.util.List;
import java.util.Optional;

public interface SportDao {

    Optional<Sport> findByName(final String sportName);

    Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName,
                           final byte[] file);

    Optional<Sport> modifySport(final String sportName, final String displayName, final Integer playerQuantity,
                                final byte[] file);

    boolean remove(final String sportName);

    Optional<byte[]> readImage(final String sportName);

    List<Sport> findSports(final List<String> sportNames, final Integer minQuantity,
                           final Integer maxQuantity, final SportSort sort);
}
