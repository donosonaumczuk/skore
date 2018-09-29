package ar.edu.itba.paw.services;

import ar.edu.itba.paw.Exceptions.GameNotFoundException;
import ar.edu.itba.paw.interfaces.GameDao;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.models.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);


    @Autowired
    private GameDao gameDao;

    public GameServiceImpl() {

    }

    @Override
    public Game create(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description) {
        Optional<Game> game = gameDao.create(teamName1, teamName2, startTime, finishTime, type, result,
                country, state, city, street, tornamentName, description);
        if(!game.isPresent()) {
            LOGGER.error("Could not create this game: {} vs {} |starting at {} |finishing at {}",
                    teamName1, teamName2, startTime, finishTime);
            throw new GameNotFoundException("There is not a game of " + teamName1 + " vs " + teamName2
                    + " starting at " + startTime + "and finishing at " +finishTime);
        }
        return game.get();
    }

    @Override
    public Game findByKey(String teamName1, String startTime, String finishTime) {
        Optional<Game> game = gameDao.findByKey(teamName1, startTime, finishTime);
        if(!game.isPresent()) {
            LOGGER.error("Could not find a game: {} |starting at {} |finishing at {}",
                    teamName1, startTime, finishTime);
            throw new GameNotFoundException("There is not a game of " + teamName1
                    + " starting at " + startTime + "and finishing at " + finishTime);
        }
        return game.get();
    }

    @Override
    public List<Game> findGames(final String minStartTime, final String maxStartTime,
                                final String minFinishTime, final String maxFinishTime,
                                final List<String> types, final List<String> sportNames,
                                final Integer minQuantity, final Integer maxQuantity,
                                final List<String> countries, final List<String> states,
                                final List<String> cities, final Integer minFreePlaces,
                                final Integer maxFreePlaces) {
        return gameDao.findGames(minStartTime, maxStartTime, minFinishTime, maxFinishTime, types,
                sportNames, minQuantity,  maxQuantity, countries, states, cities, minFreePlaces,
                maxFreePlaces);
    }

    @Override
    public Game modify(final String teamName1, final String teamName2, final String startTime,
                       final String finishTime, final String type, final String result,
                       final String country, final String state, final String city,
                       final String street, final String tornamentName, final String description,
                       final String teamName1Old, final String teamName2Old,
                       final String startTimeOld, final String finishTimeOld) {
        Optional<Game> game = gameDao.modify(teamName1, teamName2, startTime, finishTime, type, result,
                country, state, city, street, tornamentName, description, teamName1Old, teamName2Old,
                startTimeOld, finishTimeOld);
        if(!game.isPresent()) {
            LOGGER.error("Could not modify this game:: {} vs {} |starting at {} |finishing at {}",
                    teamName1Old, teamName2Old, startTimeOld, finishTimeOld);
            throw new GameNotFoundException("There is not a game of " + teamName1 + " vs " + teamName2
                    + " starting at " + startTime + "and finishing at " + finishTime);
        }
        return game.get();
    }
}
