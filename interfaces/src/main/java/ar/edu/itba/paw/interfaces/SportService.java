package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Sport;

import java.util.List;
import java.util.Optional;

public interface SportService {
    public Sport findByName(final String sportName);

    public Sport create(final String sportName, final int playerQuantity, final String displayName);

    public boolean remove(final String sportName);

    public List<Sport> getAllSports();
}
