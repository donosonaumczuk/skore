package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.TeamNotFoundException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamePageDto;
import ar.edu.itba.paw.webapp.dto.ResultDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.TeamPlayerDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.utils.QueryParamsUtils;
import ar.edu.itba.paw.webapp.validators.GameValidators;
import ar.edu.itba.paw.webapp.validators.PlayerValidators;
import ar.edu.itba.paw.webapp.validators.ResultValidators;
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
        Game game = gameService.create(gameDto.getTeamName1(), gameDto.getTeamName2(),
                    getStartTimeFrom(gameDto), gameDto.getMinutesOfDuration(), gameDto.isCompetitive(),
                    gameDto.isIndividual(), gameDto.getLocation().getCountry(), gameDto.getLocation().getState(),
                    gameDto.getLocation().getCity(), gameDto.getLocation().getStreet(), gameDto.getTornamentName(),
                    gameDto.getDescription(), gameDto.getTitle(), gameDto.getSport());
        //TODO catch exception TeamNotFoundException, GameAlreadyExist, InvalidGameKeyException (should never happend)

        return Response.status(HttpStatus.CREATED.value())
                .entity(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2())))
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
        Game game = gameService.findByKey(key);
        //TODO catch exception GameNotFound, TeamNotFoundException, InvalidGameKeyException
        LOGGER.trace("Match '{}' founded successfully", key);
        return Response.ok(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2()))).build();
    }

    @DELETE
    @Path("/{key}")
    public Response deleteGame(@PathParam("key") String key) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a match").validate(key);
        if (!gameService.remove(key)) {
            LOGGER.trace("Match '{}' does not exist", key);
            throw new ApiException(HttpStatus.NOT_FOUND, "Match '" + key + "' does not exist");
        }
        //TODO catch UnauthorizedExecption, GameNotFound, TeamNotFoundException, InvalidGameKeyException
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
        Game newGame;
        try {
            newGame = gameService.modify(gameDto.getTeamName1(), gameDto.getTeamName2(),
                    getStartTimeFrom(gameDto).toString(), gameDto.getMinutesOfDuration(), null, null,
                    gameDto.getLocation().getCountry(), gameDto.getLocation().getState(), gameDto.getLocation().getCity(),
                    gameDto.getLocation().getStreet(), null, gameDto.getDescription(), gameDto.getTitle(), key);
        }
        catch (TeamNotFoundException | IllegalArgumentException e) {//TODO catch GameNotFound, InvalidGameKeyException
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        LOGGER.trace("Match '{}' modified successfully", key);
        return Response.ok(GameDto.from(newGame, getTeam(newGame.getTeam1()),
                getTeam(newGame.getTeam2()))).build();
    }

    @POST
    @Path("/{key}/players")
    public Response addUserToGame(@PathParam("key") String key, @RequestBody final String requestBody) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
        PlayerValidators.updateValidatorOf("Add player to match fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final TeamPlayerDto playerDto = JSONUtils.jsonToObject(requestBody, TeamPlayerDto.class);
        Game game = gameService.insertUserInGame(key, playerDto.getUserId());
        //TODO catch TeamNotFoundException, InvalidGameKeyException, UserNotFoundException (should never happend),
        //TODO AlreadyJoinedToMatchException, TeamFullException
        //TODO maybe delete not premium user if it fails
        LOGGER.trace("User '{}' added successfully to match '{}'", playerDto.getUserId(), key);
        return Response.ok(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2()))).build();
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
        //TODO catch TeamNotFoundException, InvalidGameKeyException
        LOGGER.trace("User with id '{}' in match '{}' deleted successfully", userId, key);
        return Response.noContent().build();
    }

    @POST
    @Path("/{key}/result")
    public Response addResultToAGame(@PathParam("key") String key, @RequestBody final String requestBody) {
        GameValidators.keyValidator("Invalid '" + key + "' key for a game").validate(key);
        ResultValidators.creationValidatorOf("Add result to match fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final ResultDto resultDto = JSONUtils.jsonToObject(requestBody, ResultDto.class);

        Game game = gameService.updateResultOfGame(key, resultDto.getScoreTeam1(),
                resultDto.getScoreTeam2());
        //TODO catch GameHasNotBeenPlayException, InvalidGameKeyException
        LOGGER.trace("Match '{}' result added successfully", key);
        return Response.ok(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2()))).build();
    }
}
