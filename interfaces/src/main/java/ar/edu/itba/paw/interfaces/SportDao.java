package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Sport;

import java.util.List;
import java.util.Optional;

public interface SportDao {

    public Optional<Sport> findByName(final String sportName);

    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName);

    public boolean remove(final String sportName);

    public List<Sport> getAllSports();
}
