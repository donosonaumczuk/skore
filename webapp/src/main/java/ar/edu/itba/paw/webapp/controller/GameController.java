package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.Exceptions.GameNotFoundException;
import ar.edu.itba.paw.Exceptions.TeamFullException;
import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.SimpleEncrypter;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MatchForm;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
public class GameController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @Autowired
    @Qualifier("sportServiceImpl")
    private SportService sportService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping(value="/filterMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGames(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPage(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sports"), json.getInt("minQuantity"),
                (json.getInt("maxQuantity")==0)?null:json.getInt("maxQuantity"),
                json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"),
                (json.getInt("maxFreePlaces")==0)?null:json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"));
        LOGGER.trace("Returning {} games that match the criteria", games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        setUpGame(games);
        return objectMapper.writeValueAsString(games);
    }


    @RequestMapping(value="/joinedMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedIsPart(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageThatIsAPartOf(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sports"), json.getInt("minQuantity"),
                (json.getInt("maxQuantity")==0)?null:json.getInt("maxQuantity"),
                json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"),
                (json.getInt("maxFreePlaces")==0)?null:json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria", games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        setUpGame(games);
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value="/toJoinMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedIsNotPart(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageThatIsNotAPartOf(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sports"), json.getInt("minQuantity"),
                (json.getInt("maxQuantity")==0)?null:json.getInt("maxQuantity"),
                json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"),
                (json.getInt("maxFreePlaces")==0)?null:json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria",games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        setUpGame(games);
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
            gameService.insertUserInGame(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
            LOGGER.trace("insert user: {} success", loggedUser().getUser().getUserId());
            ans = true;
        }
        catch (Exception e) {
            LOGGER.trace("insert user: {} fail", loggedUser().getUser().getUserId());
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
            gameService.deleteUserInGame(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
            LOGGER.trace("delete user: {} success", loggedUser().getUser().getUserId());
            ans = true;
        }
        catch (Exception e) {
            LOGGER.trace("insert user: {} fail", loggedUser().getUser().getUserId());
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
        boolean ans = gameService.remove(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.trace("The result for delete a match is {}", ans);
        return objectMapper.writeValueAsString(ans);
    }

    @RequestMapping(value="/createdMatch", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String filterGamesLoggedCreateBy(@RequestParam final String body) throws IOException {
        JSONObject json = new JSONObject(body);
        LOGGER.trace("Asking the service for the games by a criteria");
        List<Game> games = gameService.findGamesPageCreateBy(json.getString("minStartTime"),
                json.getString("maxStartTime"), json.getString("minFinishTime"),
                json.getString("maxFinishTime"), json.getJSONArray("types"),
                json.getJSONArray("sports"), json.getInt("minQuantity"),
                (json.getInt("maxQuantity")==0)?null:json.getInt("maxQuantity"),
                json.getJSONArray("countries"),
                json.getJSONArray("states"), json.getJSONArray("cities"),
                json.getInt("minFreePlaces"),
                (json.getInt("maxFreePlaces")==0)?null:json.getInt("maxFreePlaces"),
                json.getInt("pageNumber"), loggedUser());
        LOGGER.trace("Returning {} games that match the criteria",games.size());
        ObjectMapper objectMapper = new ObjectMapper();
        setUpGame(games);
        return objectMapper.writeValueAsString(games);
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.GET })
    public ModelAndView createMatchForm(@ModelAttribute("createMatchForm") MatchForm matchForm) {
        return new ModelAndView("createMatch").addObject("sports", sportService.getAllSports());
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
                matchForm.getDuration(), "Individual" + "-" + matchForm.getCompetitivity(), matchForm.getCountry(),
                matchForm.getState(), matchForm.getCity(), matchForm.getStreet()+" "+matchForm.getStreetNumber(),
                null, matchForm.getDescription(), loggedUser.getUserName(),
                loggedUser.getUser().getUserId(), matchForm.getSportName(), matchForm.getMatchName());
        LOGGER.debug("Match created \n\n");

        return new ModelAndView("index");
    }

    @RequestMapping(value = "/match/*", method = {RequestMethod.GET})
    public ModelAndView matchDetails(HttpServletRequest request) {
        String matchURLKey = request.getServletPath().substring(7); /* /match/ .length */

        Game game;
        try {
            game = gameService.findByKeyFromURL(matchURLKey);
        }
        catch (GameNotFoundException e) {
            return new ModelAndView("404");
        }

        int sportQuantity = game.getTeam1().getSport().getQuantity();
        int team1Quantity = game.getTeam1().getPlayers().size();
        int team2Quantity = game.getTeam2().getPlayers().size();
        boolean isFull = sportQuantity == team1Quantity && sportQuantity == team2Quantity;
        boolean isCreator = isLogged() && loggedUser().getUserName().equals(game.getTeam1().getLeader().getUserName());
        //boolean hasFinished = game.getFinishTime().isAfter(LocalDateTime.now());
        boolean hasFinished = game.getFinishTime().isBefore(LocalDateTime.now());
        boolean canEdit = isFull && isCreator && hasFinished;
        return new ModelAndView("match").addObject("match", game)
                .addObject("canEdit", canEdit);
    }

    @RequestMapping(value = "/confirmMatch/**")
    public ModelAndView confirmMatch(HttpServletRequest request) {
        LOGGER.trace("Match assistance confirmed");
        String path = request.getServletPath().replace("/confirmMatch/", "");
        System.out.println("\n\npath:\n\n");
//        for(int i = 0; i < 90; i ++) {
//            if(path.charAt(i) != '%') {
//                System.out.print(path.charAt(i));
//            }
//        }
//        System.out.println("end\n\n\n");
        SimpleEncrypter encrypter = userService.getEncrypter();
        String data = encrypter.decryptString(path);
        System.out.println("\n\ndata=" + data + "\n\n");

        String userData = data.substring(0, data.indexOf("$"));
        String gameData = data.substring(data.indexOf("$") + 1, data.length());
        System.out.println("userdata: " + userData + "\ngameData: " + gameData + "\n\n\n\n");
        long userId = userService.getUserIdFromData(userData);
        User user = userService.findById(userId);
        if(user == null) {
            throw new UserNotFoundException("Can't find user");
        }
        final int URL_DATE_LENGTH =12;
        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;
        if(gameData.length() < MIN_LENGTH) {
            throw new GameNotFoundException("path '" + gameData + "' is too short to be formatted to a key");
        }

        String startTime = gameService.urlDateToKeyDate(gameData.substring(0, URL_DATE_LENGTH));
        String teamName1 = gameData.substring(URL_DATE_LENGTH, gameData.length() - URL_DATE_LENGTH);
        String finishTime = gameService.urlDateToKeyDate(gameData.substring(gameData.length() - URL_DATE_LENGTH));
        Game game = gameService.findByKey(teamName1, startTime, finishTime);
        if(game == null) {
            throw new GameNotFoundException("Can't find game");
        }
        if(!isPlayerInTeam(game, user)) {
            try {
                game = gameService.insertUserInGame(teamName1, startTime, finishTime, user.getUserId());
                LOGGER.trace("added to Match");
            } catch (Exception e) {
                LOGGER.error("Team is already full");

                return new ModelAndView("teamFull");
            }
        }
        else {
            return new ModelAndView("alreadyInGame");
        }
        userService.sendCancelOptionMatch(user, game, gameData);
        return new ModelAndView("confirmedMatchAssistance");
    }

    @RequestMapping(value = "/cancelMatch/*")
    public ModelAndView cancelMatch(HttpServletRequest request) {
        LOGGER.trace("Match assistance cancelled");
        String path = request.getServletPath().replace("/cancelMatch/", "");
        String userData = path.substring(0, path.indexOf("$"));
        String gameData = path.substring(path.indexOf("$") + 1, path.length());
        long userId = userService.getUserIdFromData(userData);
        User user = userService.findById(userId);

        if(user == null) {
            return new ModelAndView("404UserNotFound").addObject("username", "");
            //throw new UserNotFoundException("Can't find user");
        }

        final int URL_DATE_LENGTH =12;
        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;

        if(gameData.length() < MIN_LENGTH) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "canNotFindGame").addObject("attribute", "");

            //throw new GameNotFoundException("path '" + gameData + "' is too short to be formatted to a key");
        }

        String startTime = gameService.urlDateToKeyDate(gameData.substring(0, URL_DATE_LENGTH));
        String teamName1 = gameData.substring(URL_DATE_LENGTH, gameData.length() - URL_DATE_LENGTH);
        String finishTime = gameService.urlDateToKeyDate(gameData.substring(gameData.length() - URL_DATE_LENGTH));
        Game game = gameService.findByKey(teamName1, startTime, finishTime);

        if(game == null) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "canNotFindMatch").addObject("attribute", "");
            //throw new GameNotFoundException("Can't find game");
        }

        if(isPlayerInTeam(game, user)) {
            gameService.deleteUserInGame(teamName1, startTime, finishTime, userId);
            return new ModelAndView("cancelledMatchAssistance");
        }

        return new ModelAndView("genericPageWithMessage").addObject("message",
                "matchAlreadyCanceled").addObject("attribute","");

    }

    private boolean isPlayerInTeam(Game game, User user) {
        Set<User> players = game.getTeam1().getPlayers();
        if(players.contains(user)) {
            return true;
        }
        if(game.getTeam2() != null) {
            players = game.getTeam2().getPlayers();
            if (players.contains(user)) {
                    return true;
            }
        }
        return false;
    }

    private void setUpGame(List<Game> games) {
        for (Game g: games) {
            g.setQuantityOccupiedPlaces();
            g.getTeam1().getLeader().setFriends(null);
            g.getTeam1().getLeader().setLikes(null);
            g.getTeam1().getLeader().setNotifications(null);
            if(g.getTeam2() != null) {
                g.getTeam2().setLeader(null);
            }
        }
    }
}
