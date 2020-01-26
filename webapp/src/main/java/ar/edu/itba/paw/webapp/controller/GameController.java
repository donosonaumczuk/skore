package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamePageDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.utils.QueryParamsUtils;
import ar.edu.itba.paw.webapp.validators.GameValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.controller.GameController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    public static final String BASE_PATH = "matches";

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @Autowired
    @Qualifier("teamServiceImpl")
    private TeamService teamService;

    @Autowired
    @Qualifier("sessionServiceImpl")
    private SessionService sessionService;

    public static String getGameEndpoint(final String gameId) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(gameId).toTemplate();
    }

    @GET
    public Response getGames(@QueryParam("minStartTime") String minStartTime,
                             @QueryParam("maxStartTime") String maxStartTime,
                             @QueryParam("minFinishTime") String minFinishTime,
                             @QueryParam("maxFinishTime") String maxFinishTime,
                             @QueryParam("minQuantity") String minQuantity,
                             @QueryParam("maxQuantity") String maxQuantity,
                             @QueryParam("minFreePlaces") String minFreePlaces,
                             @QueryParam("maxFreePlaces") String maxFreePlaces,
                             @QueryParam("country") List<String> countries,
                             @QueryParam("state") List<String> states,
                             @QueryParam("city") List<String> cities,
                             @QueryParam("sport") List<String> sports,
                             @QueryParam("type") List<String> types,
                             @QueryParam("withPlayers") List<String> usernamesPlayersInclude,
                             @QueryParam("withoutPlayers") List<String> usernamesPlayersNotInclude,
                             @QueryParam("createdBy") List<String> usernamesCreatorsInclude,
                             @QueryParam("notCreatedBy") List<String> usernamesCreatorsNotInclude,
                             @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                             @QueryParam("sortBy") GameSort sort, @Context UriInfo uriInfo,
                             @QueryParam("hasResult") String hasResult) {
        Page<GameDto> page = gameService.findGamesPage(minStartTime, maxStartTime, minFinishTime, maxFinishTime,
                types, sports, QueryParamsUtils.positiveIntegerOrNull(minQuantity),
                QueryParamsUtils.positiveIntegerOrNull(maxQuantity), countries, states, cities,
                QueryParamsUtils.positiveIntegerOrNull(minFreePlaces),
                QueryParamsUtils.positiveIntegerOrNull(maxFreePlaces),
                usernamesPlayersInclude, usernamesPlayersNotInclude, usernamesCreatorsInclude,
                usernamesCreatorsNotInclude, QueryParamsUtils.positiveIntegerOrNull(limit),
                QueryParamsUtils.positiveIntegerOrNull(offset), sort, QueryParamsUtils.booleanOrNull(hasResult))
                .map((game) ->GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2())));

        LOGGER.trace("Matches successfully gotten");
        return Response.ok().entity(GamePageDto.from(page, uriInfo)).build();
    }

    private TeamDto getTeam(Team team) {
        if (team == null) {
            return null;
        }
        return TeamDto.from(teamService.getAccountsMap(team), team);
    }

    @POST
    public Response createGame(@RequestBody final String requestBody) {
        GameValidators.creationValidatorOf("Match creation fails, invalid creation JSON");
        final GameDto gameDto = JSONUtils.jsonToObject(requestBody, GameDto.class);
        Optional<Game> game;
        if (!gameDto.isIndividual()) {
            //TODO: validate that teams exist and maybe that logged user has permissions
            game = gameService.create(gameDto.getTeam1().getTeamName(), gameDto.getTeam2().getTeamName(),
                    getStartTimeFrom(gameDto), gameDto.getMinutesOfDuration(), gameDto.isCompetitive(),
                    gameDto.isIndividual(), gameDto.getLocation().getCountry(), gameDto.getLocation().getState(),
                    gameDto.getLocation().getCity(), gameDto.getLocation().getStreet(), gameDto.getTornamentName(),
                    gameDto.getDescription(), gameDto.getTitle());
        } else {
            PremiumUser creator = sessionService.getLoggedUser().get();
            game = gameService.createNoTeamGame(getStartTimeFrom(gameDto), gameDto.getMinutesOfDuration(),
                    gameDto.isCompetitive(), gameDto.getLocation().getCountry(), gameDto.getLocation().getState(),
                    gameDto.getLocation().getCity(), gameDto.getLocation().getStreet(), gameDto.getTornamentName(),
                    gameDto.getDescription(), creator.getUserName(), creator.getUser().getUserId(), gameDto.getSport(),
                    gameDto.getTitle());
        }

        return Response.status(HttpStatus.CREATED.value())
                .entity(GameDto.from(game.get(), getTeam(game.get().getTeam1()), getTeam(game.get().getTeam2())))
                .build();
    }

    private LocalDateTime getStartTimeFrom(GameDto gameDto) {
        return LocalDateTime.of(gameDto.getDate().getYear(), gameDto.getDate().getMonthNumber(),
                gameDto.getDate().getDayOfMonth(), gameDto.getTime().getHour(), gameDto.getTime().getMinute(),
                gameDto.getTime().getSecond());
    }

    @GET
    @Path("/{key}")
    public Response getGame(@PathParam("key") String key) {
        //Todo: Validate key values
        Game game = gameService.findByKey(key).orElseThrow(() -> {
            LOGGER.trace("Match '{}' does not exist", key);
            return new ApiException(HttpStatus.NOT_FOUND, "Match '" + key + "' does not exist");
        });
        LOGGER.trace("Match '{}' founded successfully", key);
        return Response.ok(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2()))).build();
    }

    @DELETE
    @Path("/{key}")
    public Response deleteGame(@PathParam("key") String key) {
        //Todo: Validate key values and that user is creator
        if (!gameService.remove(key)) {
            LOGGER.trace("Match '{}' does not exist", key);
            throw new ApiException(HttpStatus.NOT_FOUND, "Match '" + key + "' does not exist");
        }
        LOGGER.trace("Match '{}' deleted successfully", key);
        return Response.noContent().build();
    }



//    @RequestMapping(value="/addPlayerToMatch", method= RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody String addPlayerToMatch(@RequestParam final String teamName1,
//                                                 @RequestParam final String startTime,
//                                                 @RequestParam final String finishTime) throws IOException {
//        LOGGER.trace("Asking to add logged player from {}|{}|{}", teamName1,startTime,finishTime);
//        boolean ans;
//        try {
//            gameService.insertUserInGame(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
//            LOGGER.trace("insert user: {} success", loggedUser().getUser().getUserId());
//            ans = true;
//        }
//        catch (Exception e) {
//            LOGGER.trace("insert user: {} fail", loggedUser().getUser().getUserId());
//            ans = false;
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(ans);
//    }
//
//    @RequestMapping(value="/removePlayerFromMatch", method= RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody String removePlayerFromMatch(@RequestParam final String teamName1,
//                                                      @RequestParam final String startTime,
//                                                      @RequestParam final String finishTime) throws IOException {
//        LOGGER.trace("Asking to remove logged player from {}|{}|{}", teamName1,startTime,finishTime);
//        boolean ans;
//        try {
//            gameService.deleteUserInGame(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
//            LOGGER.trace("delete user: {} success", loggedUser().getUser().getUserId());
//            ans = true;
//        }
//        catch (Exception e) {
//            LOGGER.trace("insert user: {} fail", loggedUser().getUser().getUserId());
//            ans = false;
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(ans);
//    }
//
//    @RequestMapping(value="/deleteMatch", method= RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public @ResponseBody String deleteMatch(@RequestParam final String teamName1,
//                                            @RequestParam final String startTime,
//                                            @RequestParam final String finishTime) throws IOException {
//        LOGGER.trace("Asking to delete a match");
//        boolean ans = gameService.remove(teamName1, startTime, finishTime, loggedUser().getUser().getUserId());
//        ObjectMapper objectMapper = new ObjectMapper();
//        LOGGER.trace("The result for delete a match is {}", ans);
//        return objectMapper.writeValueAsString(ans);
//    }
//
//
//    @RequestMapping(value = "/match/*", method = {RequestMethod.GET})
//    public ModelAndView matchDetails(HttpServletRequest request) {
//        String matchURLKey = request.getServletPath().substring(7); /* /match/ .length */
//
//        Game game;
//        try {
//            game = gameService.findByKey(matchURLKey);
//        }
//        catch (GameNotFoundException e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindMatch").addObject("attribute", "");
//        }
//
//        int sportQuantity = game.getTeam1().getSport().getQuantity();
//        int team1Quantity = game.getTeam1().getPlayers().size();
//        int team2Quantity = game.getTeam2().getPlayers().size();
//        boolean isFull = sportQuantity == team1Quantity && sportQuantity == team2Quantity;
//        boolean isCreator = isLogged() && loggedUser().getUserName().equals(game.getTeam1().getLeader().getUserName());
//       // boolean hasFinished = game.getFinishTime().isAfter(LocalDateTime.now());
//        boolean hasResult = game.getResult() != null;
//        boolean hasFinished = game.getFinishTime().isBefore(LocalDateTime.now());
//        boolean canEdit = isFull && isCreator && hasFinished && !hasResult;
//        return new ModelAndView("match").addObject("match", game)
//                .addObject("canEdit", canEdit)
//                .addObject("matchURLKey", matchURLKey);
//    }
//
//    @RequestMapping(value= "/submitMatchResult/{matchKey:.+}", method = {RequestMethod.GET})
//    public ModelAndView submitMatchResultForm(@ModelAttribute("submitResultForm") SubmitResultForm submitResultForm,
//                                      HttpServletRequest request, @PathVariable String matchKey) {
//        //String sportName = request.getServletPath().replace("/admin/editSport/", "");
//        Game game = null;
//        try {
//            game = gameService.findByKey(matchKey);
//        }
//        catch (GameNotFoundException e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindMatch").addObject("attribute", "");
//        }
//
//        return new ModelAndView("submitMatchResult").addObject("game", game)
//                .addObject("matchKey", matchKey);
//    }
//
//    @RequestMapping(value="/submitMatchResult/*",  method = {RequestMethod.POST})
//    public ModelAndView submitMatchResult(@Valid @ModelAttribute("submitResultForm") final SubmitResultForm submitResultForm,
//                                  final BindingResult errors, HttpServletRequest request) {
//        if(errors.hasErrors()) {
//            return submitMatchResultForm(submitResultForm, request, submitResultForm.getMatchKey());
//        }
//        int team1Score = 0, team2Score = 0;
//
//        for(int i = 0; i< submitResultForm.getTeam1Points().length(); i++) {
//            team1Score = team1Score * 10 + (submitResultForm.getTeam1Points().charAt(i) - '0');
//        }
//
//        for(int i = 0; i< submitResultForm.getTeam2Points().length(); i++) {
//            team2Score = team2Score * 10 + (submitResultForm.getTeam2Points().charAt(i) - '0');
//        }
//
//
//
//        Game game;
//        try {
//            game = gameService.findByKey(submitResultForm.getMatchKey());
//        }
//        catch (GameNotFoundException e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindMatch").addObject("attribute", "");
//        }
//
//        //addResult
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String startTime = game.getStartTime().format(formatter);
//        String finishTime = game.getFinishTime().format(formatter);
//        try {
//            game = gameService.updateResultOfGame(game.team1Name(), startTime, finishTime,
//                    team1Score, team2Score);
//        }
//        catch(GameHasNotBeenPlayException e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "CanNotUpdateResultHasNotPlayed").addObject("attribute", "");
//        }
//
//        return new ModelAndView("redirect:/match/" + submitResultForm.getMatchKey());
////        return new ModelAndView("match").addObject("match", game)
////                .addObject("canEdit", false)
////                .addObject("matchURLKey", submitResultForm.getMatchKey());
//    }
//
//    @RequestMapping(value = "/confirmMatch/**")
//    public ModelAndView confirmMatch(HttpServletRequest request) {
//        LOGGER.trace("Match assistance confirmed");
//        String path = request.getServletPath().replace("/confirmMatch/", "");
//        System.out.println("\n\npath:\n\n");
////        for(int i = 0; i < 90; i ++) {
////            if(path.charAt(i) != '%') {
////                System.out.print(path.charAt(i));
////            }
////        }
////        System.out.println("end\n\n\n");
//        SimpleEncrypter encrypter = userService.getEncrypter();
//        String data = encrypter.decryptString(path);
//        System.out.println("\n\ndata=" + data + "\n\n");
//
//        String userData = data.substring(0, data.indexOf("$"));
//        String gameData = data.substring(data.indexOf("$") + 1, data.length());
//        System.out.println("userdata: " + userData + "\ngameData: " + gameData + "\n\n\n\n");
//        long userId = userService.getUserIdFromData(userData);
//        User user = userService.findById(userId);
//
//        if(user == null) {
//            return new ModelAndView("404UserNotFound").addObject("username", "");
//            //throw new UserNotFoundException("Can't find user");
//        }
//
//        final int URL_DATE_LENGTH =12;
//        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;
//        if(gameData.length() < MIN_LENGTH) {
//            throw new GameNotFoundException("path '" + gameData + "' is too short to be formatted to a key");
//        }
//
//        String startTime = gameService.urlDateToKeyDate(gameData.substring(0, URL_DATE_LENGTH));
//        String teamName1 = gameData.substring(URL_DATE_LENGTH, gameData.length() - URL_DATE_LENGTH);
//        String finishTime = gameService.urlDateToKeyDate(gameData.substring(gameData.length() - URL_DATE_LENGTH));
//        Game game = gameService.findByKey(teamName1, startTime, finishTime);
//        if(game == null) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindMatch").addObject("attribute", "");
//        //    throw new GameNotFoundException("Can't find game");
//        }
//
//        if(game.getResult() != null || LocalDateTime.now().isAfter(game.getFinishTime())) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "confirmMatchAlreadyFinished").addObject("attribute", "");
//        }
//
//        if(LocalDateTime.now().isAfter(game.getStartTime())) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "confirmMatchAlreadyStarted").addObject("attribute", "");
//        }
//        if(!isPlayerInTeam(game, user)) {
//            try {
//                game = gameService.insertUserInGame(teamName1, startTime, finishTime, user.getUserId());
//                LOGGER.trace("added to Match");
//            } catch (Exception e) {
//                LOGGER.error("Team is already full");
//
//                return new ModelAndView("teamFull");
//            }
//        }
//        else {
//            return new ModelAndView("alreadyInGame");
//        }
//        userService.sendCancelOptionMatch(user, game, gameData);
//        return new ModelAndView("confirmedMatchAssistance");
//    }
//
//    @RequestMapping(value = "/cancelMatch/*")
//    public ModelAndView cancelMatch(HttpServletRequest request) {
//        LOGGER.trace("Match assistance cancelled");
//        String path = request.getServletPath().replace("/cancelMatch/", "");
//        String userData = path.substring(0, path.indexOf("$"));
//        String gameData = path.substring(path.indexOf("$") + 1, path.length());
//        long userId = userService.getUserIdFromData(userData);
//        User user = userService.findById(userId);
//
//        if(user == null) {
//            return new ModelAndView("404UserNotFound").addObject("username", "");
//            //throw new UserNotFoundException("Can't find user");
//        }
//
//        final int URL_DATE_LENGTH =12;
//        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;
//
//        if(gameData.length() < MIN_LENGTH) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindGame").addObject("attribute", "");
//
//            //throw new GameNotFoundException("path '" + gameData + "' is too short to be formatted to a key");
//        }
//
//        String startTime = gameService.urlDateToKeyDate(gameData.substring(0, URL_DATE_LENGTH));
//        String teamName1 = gameData.substring(URL_DATE_LENGTH, gameData.length() - URL_DATE_LENGTH);
//        String finishTime = gameService.urlDateToKeyDate(gameData.substring(gameData.length() - URL_DATE_LENGTH));
//        Game game = gameService.findByKey(teamName1, startTime, finishTime);
//
//        if(game == null) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "canNotFindMatch").addObject("attribute", "");
//            //throw new GameNotFoundException("Can't find game");
//        }
//
//        if(game.getResult() != null || LocalDateTime.now().isAfter(game.getFinishTime())) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "cancelMatchHasAlreadyFinished");
//        }
//
//        if(LocalDateTime.now().isAfter(game.getStartTime())) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "cancelMatchHasAlreadyStarted");
//        }
//
//        if(isPlayerInTeam(game, user)) {
//            gameService.deleteUserInGame(teamName1, startTime, finishTime, userId);
//            return new ModelAndView("cancelledMatchAssistance");
//        }
//
//        return new ModelAndView("genericPageWithMessage").addObject("message",
//                "matchAlreadyCanceled").addObject("attribute","");
//
//    }
//
//    private boolean isPlayerInTeam(Game game, User user) {
//        Set<User> players = game.getTeam1().getPlayers();
//        if(players.contains(user)) {
//            return true;
//        }
//        if(game.getTeam2() != null) {
//            players = game.getTeam2().getPlayers();
//            if (players.contains(user)) {
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    private void setUpGame(List<Game> games) {
//        for (Game g: games) {
//            g.setQuantityOccupiedPlaces();
//            g.getTeam1().getLeader().setFriends(null);
//            g.getTeam1().getLeader().setLikes(null);
//            g.getTeam1().getLeader().setNotifications(null);
//            if(g.getTeam2() != null) {
//                g.getTeam2().setLeader(null);
//            }
//        }
//    }
}
