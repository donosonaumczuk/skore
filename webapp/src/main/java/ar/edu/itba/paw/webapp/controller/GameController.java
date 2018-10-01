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
        LOGGER.trace("Returning " + games.size() + " games that match the criteria");
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
            System.out.println("sportName: " + matchForm.getSportName() + "\n\n\n\n");
            return createMatchForm(matchForm);
        }

        PremiumUser loggedUser = loggedUser();

        LOGGER.debug("Match form completed, creating match...");
        Game game = gameService.createNoTeamGame(matchForm.getStartTime(), "2018-12-12 01:00:00",matchForm.getMode(),
                matchForm.getCountry(), matchForm.getState(), matchForm.getCity(), matchForm.getStreet(),
                null, matchForm.getDescription(),loggedUser.getUserName(),loggedUser.getUserId(),
                matchForm.getSportName());
        LOGGER.debug("Match created {}\n\n", game.getTeam1().getSport().getName());


        return new ModelAndView("index");
    }

}
