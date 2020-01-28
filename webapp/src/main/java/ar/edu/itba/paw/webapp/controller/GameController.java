package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamePageDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.TeamPlayerDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.utils.QueryParamsUtils;
import ar.edu.itba.paw.webapp.validators.GameValidators;
import ar.edu.itba.paw.webapp.validators.PlayerValidators;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import ar.edu.itba.paw.webapp.validators.ValidatorFactory;
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
import javax.ws.rs.PUT;
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
    @Qualifier("userServiceImpl")
    private UserService userService;

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
        GameValidators.creationValidatorOf("Match creation fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
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
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
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
        GameValidators.keyValidator("Invalid '" + key + "' key for a match").validate(key);
        Optional<Game> game = gameService.findByKey(key);
        GameValidators.existenceValidatorOf(key, "Can't get '" + key + "' match").validate(game);
        GameValidators.isCreatorValidatorOf(key, game.get().getTeam1().getLeader().getUserName(),
                "User is not creator of '" + key + "' match").validate(sessionService.getLoggedUser().get());
        if (!gameService.remove(key)) {
            LOGGER.trace("Match '{}' does not exist", key);
            throw new ApiException(HttpStatus.NOT_FOUND, "Match '" + key + "' does not exist");
        }
        LOGGER.trace("Match '{}' deleted successfully", key);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{key}")
    public Response updateGame(@PathParam("key") String key, @RequestBody final String requestBody) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
        GameValidators.updateValidatorOf("Match update fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final GameDto gameDto = JSONUtils.jsonToObject(requestBody, GameDto.class);
        Optional<Game> newGame;
        try {
            newGame = gameService.modify(gameDto.getTeam1().getTeamName(), gameDto.getTeam2().getTeamName(),
                    getStartTimeFrom(gameDto).toString(), gameDto.getMinutesOfDuration(), null, null,
                    gameDto.getLocation().getCountry(), gameDto.getLocation().getState(), gameDto.getLocation().getCity(),
                    gameDto.getLocation().getStreet(), null, gameDto.getDescription(), gameDto.getTitle(), key);
        }
        catch (TeamNotFoundException | IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        GameValidators.existenceValidatorOf(key, "Match update fails, match '" + key + "' does not exist")
                .validate(newGame);
        LOGGER.trace("Match '{}' modified successfully", key);
        return Response.ok(GameDto.from(newGame.get(), getTeam(newGame.get().getTeam1()),
                getTeam(newGame.get().getTeam2()))).build();
    }

    @POST
    @Path("/{key}/players")
    public Response addUserToGame(@PathParam("key") String key, @RequestBody final String requestBody) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
        PlayerValidators.creationValidatorOf("Add player to match fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final TeamPlayerDto playerDto = JSONUtils.jsonToObject(requestBody, TeamPlayerDto.class);
        long userId;
        if (playerDto.getUsername() != null) {//TODO maybe move logic to service
            Optional<PremiumUser> premiumUserOptional = sessionService.getLoggedUser();
            UserValidators.isAuthorizedForUpdateValidatorOf(playerDto.getUsername(), "User '" +
                    playerDto.getUsername() + "' addition to a match failed, unauthorized")
                    .validate(premiumUserOptional);
            userId = premiumUserOptional.get().getUser().getUserId();
        }
        else {
            userId = userService.create(playerDto.getFirstName(), playerDto.getLastName(),
                    playerDto.getEmail()).getUserId();//TODO map exception
        }
        Optional<Game> gameOptional = gameService.insertUserInGame(key, userId);
        GameValidators.existenceValidatorOf(key, "Add player to match fails, match '" + key + "' does not exist")
                .validate(gameOptional);
        LOGGER.trace("User '{}' added successfully to match '{}'", userId, key);
        return Response.ok(GameDto.from(gameOptional.get(), getTeam(gameOptional.get().getTeam1()),
                getTeam(gameOptional.get().getTeam2()))).build();
    }

    @DELETE
    @Path("/{key}/players/{id}")
    public Response removeUserFromGame(@PathParam("key") String key, @PathParam("id") long userId) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
        //TODO: maybe validate user id is positive
        if (!gameService.deleteUserInGame(key, userId)) {
            LOGGER.trace("User with id '{}' does not exist in match '{}'", userId, key);
            throw new ApiException(HttpStatus.NOT_FOUND, "User with id '" + userId + "' does not exist in match '" +
                    key + "'");
        }
        LOGGER.trace("User with id '{}' in match '{}' deleted successfully", userId, key);
        return Response.noContent().build();
    }
}
