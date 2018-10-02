package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.form.MatchForm;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class GameController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @RequestMapping(value="/filterMatch", method= RequestMethod.POST)
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
        LOGGER.trace("Returning {} games that match the criteria", games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value="/filterMatchLoggedIsPart", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedIsPart(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageThatIsAPartOf(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sportNames"), json.getInt("minQuantity"),
                json.getInt("maxQuantity"), json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"), json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria", games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value="/filterMatchLoggedIsNotPart", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedIsNotPart(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageThatIsNotAPartOf(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sportNames"), json.getInt("minQuantity"),
                json.getInt("maxQuantity"), json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"), json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria",games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value="/addPlayerToMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String addPlayerToMatch(@RequestParam final String teamName1,
                                                 @RequestParam final String startTime,
                                                 @RequestParam final String finishTime) throws IOException {
        LOGGER.trace("Asking to add logged player from {}|{}|{}", teamName1,startTime,finishTime);
        boolean ans;
        try {
            gameService.insertUserInGame(teamName1, startTime, finishTime, loggedUser().getUserId());
            LOGGER.trace("insert user: {} success", loggedUser().getUserId());
            ans = true;
        }
        catch (Exception e) {
            LOGGER.trace("insert user: {} fail", loggedUser().getUserId());
            ans = false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ans);
    }

    @RequestMapping(value="/removePlayerFromMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String removePlayerFromMatch(@RequestParam final String teamName1,
                                                      @RequestParam final String startTime,
                                                      @RequestParam final String finishTime) throws IOException {
        LOGGER.trace("Asking to remove logged player from {}|{}|{}", teamName1,startTime,finishTime);
        boolean ans;
        try {
            gameService.deleteUserInGame(teamName1, startTime, finishTime, loggedUser().getUserId());
            LOGGER.trace("delete user: {} success", loggedUser().getUserId());
            ans = true;
        }
        catch (Exception e) {
            LOGGER.trace("insert user: {} fail", loggedUser().getUserId());
            ans = false;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ans);
    }

    @RequestMapping(value="/deleteMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteMatch(@RequestParam final String teamName1,
                                            @RequestParam final String startTime,
                                            @RequestParam final String finishTime) throws IOException {
        LOGGER.trace("Asking to delete a match");
        boolean ans = gameService.remove(teamName1, startTime, finishTime, loggedUser().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.trace("The result for delete a match is {}", ans);
        return objectMapper.writeValueAsString(ans);
    }

    @RequestMapping(value="/filterMatchLoggedCreateBy", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedCreateBy(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageCreateBy(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sportNames"), json.getInt("minQuantity"),
                json.getInt("maxQuantity"), json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"), json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria",games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.GET })
    public ModelAndView createMatchForm(@ModelAttribute("createMatchForm") MatchForm matchForm) {
        return new ModelAndView("createMatch");
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.POST })
    public ModelAndView createMatch(@Valid @ModelAttribute("createMatchForm") final MatchForm matchForm,
                                    final BindingResult errors) {
        if(errors.hasErrors()) {
            return createMatchForm(matchForm);
        }

        PremiumUser loggedUser = loggedUser();

        LOGGER.debug("Match form completed, creating match...");
        Game game = gameService.createNoTeamGame(matchForm.getDate() + " " + matchForm.getStartTime(),
                matchForm.getDuration(), matchForm.getMode(), matchForm.getCountry(), matchForm.getState(),
                matchForm.getCity(), matchForm.getStreet(), null, matchForm.getDescription(),
                loggedUser.getUserName(),loggedUser.getUserId(), matchForm.getSportName(),
                matchForm.getMatchName());
        LOGGER.debug("Match created \n\n");


        return new ModelAndView("index");
    }

}
