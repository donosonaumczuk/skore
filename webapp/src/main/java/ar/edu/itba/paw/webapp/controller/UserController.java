package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamesDto;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.TeamPlayerDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ar.edu.itba.paw.webapp.controller.UserController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String BASE_PATH = "user";

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    public static String getProfileEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("profile").toTemplate();
    }

    public static String getMatchesEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("matches").toTemplate();
    }

    public static String getSportsEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("sports").toTemplate();
    }

    @GET
    @Path("/{username}/profile")
    public Response getProfile(@PathParam("username") String username) {
        Optional<PremiumUser> premiumUserOptional = premiumUserService.findByUserName(username);
        if (premiumUserOptional.isPresent()) {
            LOGGER.trace("'{}' profile successfully gotten", username);
            return Response.ok(ProfileDto.from(premiumUserOptional.get())).build();
        }
        LOGGER.error("Can't get '{}' profile, user not found", username);
        throw new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
    }

    @GET
    @Path("/{username}/matches")
    public Response getGames(@PathParam("username") String username) {
        Optional<PremiumUser> premiumUserOptional = premiumUserService.findByUserName(username);
        List<List<Game>> gamesResult;
        if (premiumUserOptional.isPresent()) {
            gamesResult = gameService.getGamesThatPlay(premiumUserOptional.get().getUser().getUserId());
            List<GameDto> games = new LinkedList<>();
            gamesResult.forEach(gameList -> {
                gameList.forEach(game -> games.add(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2()))));
            });
            LOGGER.trace("'{}' matches successfully gotten", username);
            return Response.ok(GamesDto.from(games)).build();
        }
        LOGGER.error("Can't get '{}' profile, user not found", username);
        throw new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
    }

    private TeamDto getTeam(Team team) {
        Set<User> teamusers = team.getPlayers();
        List<TeamPlayerDto> teamPlayers = new LinkedList<>();
        teamusers.forEach(user -> {
            Optional<PremiumUser> currentPlayer = premiumUserService.findById(user.getUserId());
            if (currentPlayer.isPresent()) {
                teamPlayers.add(TeamPlayerDto.from(currentPlayer.get()));
            }
            else {
                teamPlayers.add(TeamPlayerDto.from(user));
            }
        });

        return TeamDto.from(teamPlayers, team.getName());//TODO add a check to see if name is created by user or autoasigned
    }

}
