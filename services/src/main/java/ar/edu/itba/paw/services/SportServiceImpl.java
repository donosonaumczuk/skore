package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportServiceImpl.class);

    @Autowired
    private SportDao sportDao;

    @Override
    public Optional<Sport> findByName(final String sportName) {
        return sportDao.findByName(sportName);
    }


    @Override
    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName,
                        final byte[] file) {
        return sportDao.create(sportName, playerQuantity, displayName, file);
    }

    @Override
    public Optional<Sport> modifySport(final String sportName, final String displayName, final int playerQuantity,
                             final byte[] file) {
        return sportDao.modifySport(sportName, displayName, playerQuantity, file);
    }

    @Override
    public boolean remove(final String sportName) {
        return sportDao.remove(sportName);
    }


    @Override
    public Page<Sport> findSportsPage(final List<String> sportNames, final Integer minQuantity,
                                      final Integer maxQuantity, final SportSort sort,
                                      final Integer limit, final Integer offset) {
        return new Page<>(sportDao.findSports(sportNames, minQuantity, maxQuantity, sort), offset, limit);
    }

    @Override
    public Optional<byte[]> readImage(final String sportName) {
        return sportDao.readImage(sportName);
    }
}
