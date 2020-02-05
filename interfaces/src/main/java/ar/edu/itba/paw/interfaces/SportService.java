package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;

import java.util.List;
import java.util.Optional;

public interface SportService {

    public Optional<Sport> findByName(final String sportName);

    public Optional<Sport> create(final String sportName, final int playerQuantity,
                        final String displayName, final byte[] file);

    public Optional<Sport> modifySport(final String sportName, final String displayName,
                             final Integer playerQuantity, final byte[] file);

    public boolean remove(final String sportName);

    public Page<Sport> findSportsPage(final List<String> sportNames, final Integer minQuantity,
                                      final Integer maxQuantity, final SportSort sort,
                                      final Integer limit, final Integer offset);

    public Optional<byte[]> readImage(final String sportName);
}
