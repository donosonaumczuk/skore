package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.models.Game;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @RequestMapping(value="/match/filter", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGames(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPage(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sportNames"), json.getInt("minQuantity"),
                json.getInt("maxQuantity"), json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"), json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"));
        LOGGER.trace("Returning " + games.size() + " games that match the criteria");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }
}
