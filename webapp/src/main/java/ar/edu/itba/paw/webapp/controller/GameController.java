package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.Team;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @RequestMapping(value="/match/filter", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGames(
            @RequestParam final String minStartTime, @RequestParam final String maxStartTime,
            @RequestParam final String minFinishTime, @RequestParam final String maxFinishTime,
            @RequestParam final ArrayList<String> types, @RequestParam final List<String> sportNames,
            @RequestParam final Integer minQuantity, @RequestParam final Integer maxQuantity,
            @RequestParam final List<String> countries, @RequestParam final List<String> states,
            @RequestParam final List<String> cities, @RequestParam final Integer minFreePlaces,
            @RequestParam final Integer maxFreePlaces) throws IOException {
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGames(minStartTime, maxStartTime, minFinishTime,
                maxFinishTime, types, sportNames, minQuantity,  maxQuantity, countries, states,
                cities, minFreePlaces, maxFreePlaces);
        LOGGER.trace("Returning " + games.size() + " games that match the criteria");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }
}
