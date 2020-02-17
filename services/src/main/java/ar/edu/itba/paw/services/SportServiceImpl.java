package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.alreadyexists.SportAlreadyExistException;
import ar.edu.itba.paw.exceptions.invalidstate.SportInvalidStateException;
import ar.edu.itba.paw.exceptions.notfound.SportNotFoundException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportServiceImpl.class);

    @Autowired
    private SportDao sportDao;

    @Autowired
    private GameService gameService;

    @Transactional
    @Override
    public Optional<Sport> findByName(final String sportName) {
        return sportDao.findByName(sportName);
    }


    @Transactional
    @Override
    public Sport create(final String sportName, final int playerQuantity, final String displayName,
                        final byte[] file) {
        return sportDao.create(sportName, playerQuantity, displayName, file).orElseThrow(() -> {
            LOGGER.error("Sport creation failed, sport '{}' already exist", sportName);
            return SportAlreadyExistException.ofId(sportName);
        });
    }

    @Transactional
    @Override
    public Sport modifySport(final String sportName, final String displayName, final Integer playerQuantity, final byte[] file) {
        LOGGER.trace("Trying to modify sport '{}'", sportName);
        ArrayList<String> sport = new ArrayList<>();
        sport.add(sportName);
        if (playerQuantity != null && !gameService.findGamesPage(null, null, null,
                null, null, sport, null, null, null, null ,
                null, null ,null, null ,null,
                null,null, null,null, null,
                null, false, false).getData().isEmpty()) {
            LOGGER.trace("Modify sport '{}' failed, is already used in a match", sportName);
            throw SportInvalidStateException.ofSportUsed(sportName);
        }
        return sportDao.modifySport(sportName, displayName, playerQuantity, file).orElseThrow(() -> {
            LOGGER.error("Modify sport failed, sport '{}' not found", sportName);
            return SportNotFoundException.ofId(sportName);
        });
    }

    @Transactional
    @Override
    public void remove(final String sportName) {
        LOGGER.trace("Looking for sport with name: {} to remove", sportName);
        ArrayList<String> sport = new ArrayList<>();
        sport.add(sportName);
        if (!gameService. findGamesPage(null, null, null, null,
                null, sport, null, null, null, null ,null,
                null ,null, null ,null,
                null,null, null,null, null,
                null, false, false).getData().isEmpty()) {
            LOGGER.trace("Remove sport '{}' failed, is already used in a match", sportName);
            throw SportInvalidStateException.ofSportUsed(sportName);
        }
        if (sportDao.remove(sportName)) {
            LOGGER.trace("{} removed", sportName);
        } else {
            LOGGER.error("{} wasn't removed", sportName);
            throw SportNotFoundException.ofId(sportName);
        }
    }

    @Transactional
    @Override
    public Page<Sport> findSportsPage(final List<String> sportNames, final Integer minQuantity,
                                      final Integer maxQuantity, final SportSort sort,
                                      final Integer limit, final Integer offset) {
        return new Page<>(sportDao.findSports(sportNames, minQuantity, maxQuantity, sort), offset, limit);
    }

    @Transactional
    @Override
    public Optional<byte[]> readImage(final String sportName) {
        return sportDao.readImage(sportName);
    }
}
