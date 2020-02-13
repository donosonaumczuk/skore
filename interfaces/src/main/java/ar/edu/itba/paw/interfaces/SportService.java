package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;

import java.util.List;
import java.util.Optional;

public interface SportService {

    Optional<Sport> findByName(final String sportName);

    Sport create(final String sportName, final int playerQuantity,
                 final String displayName, final byte[] file);

    Sport modifySport(final String sportName, final String displayName,
                      final Integer playerQuantity, final byte[] file);

    void remove(final String sportName);

    Page<Sport> findSportsPage(final List<String> sportNames, final Integer minQuantity,
                               final Integer maxQuantity, final SportSort sort,
                               final Integer limit, final Integer offset);

    Optional<byte[]> readImage(final String sportName);
}
