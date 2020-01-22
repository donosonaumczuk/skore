package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GameListDto;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.TeamPlayerDto;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ar.edu.itba.paw.webapp.controller.UserController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String BASE_PATH = "users";

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @Autowired
    @Qualifier("teamServiceImpl")
    private TeamService teamService;

    public static String getProfileEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("profile").toTemplate();
    }

    public static String getUserEndpoint(String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).toTemplate();
    }

    public static String getMatchesEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("matches").toTemplate();
    }

    public static String getSportsEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("sports").toTemplate();
    }

    public static String getUserImageEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("image").toTemplate();
    }

    @GET
    @Path("/{username}/profile")
    public Response getProfile(@PathParam("username") String username) {
        Optional<PremiumUser> premiumUserOptional = premiumUserService.findByUserName(username);
        UserValidators.existenceValidatorOf(username, "Can't get '" + username + "' profile").validate(premiumUserOptional);
        LOGGER.trace("'{}' profile successfully gotten", username);
        return Response.ok(ProfileDto.from(premiumUserOptional.get())).build();
    }

    @GET
    @Path("/{username}/matches")
    public Response getGames(@PathParam("username") String username) {
        Optional<PremiumUser> premiumUserOptional = premiumUserService.findByUserName(username);
        UserValidators.existenceValidatorOf(username, "Can't get '" + username + "' matches").validate(premiumUserOptional);
        PremiumUser premiumUser = premiumUserOptional.get();
        List<List<Game>> gamesResult;
        gamesResult = gameService.getGamesThatPlay(premiumUser.getUser().getUserId());
        List<GameDto> games = new LinkedList<>();
        gamesResult.forEach(gameList -> gameList.forEach(game -> games.add(GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2())))));
        LOGGER.trace("'{}' matches successfully gotten", username);
        return Response.ok(GameListDto.from(games)).build();
    }

    private TeamDto getTeam(Team team) {
        teamService.getAccountsList(team);
        Map<User, PremiumUser> userMap = team.getAccountsPlayers();
        Set<User> teamusers = team.getPlayers();
        List<TeamPlayerDto> teamPlayers = new LinkedList<>();
        teamusers.forEach(user -> {
            if(userMap.containsKey(user)) {
                teamPlayers.add(TeamPlayerDto.from(userMap.get(user)));
            }
            else {
                teamPlayers.add(TeamPlayerDto.from(user));
            }
        });
        return TeamDto.from(teamPlayers, team.getName());//TODO add a check to see if name is created by user or autoasigned
    }

    @GET
    @Path("/{username}/image")
    public Response getImageUser(@PathParam("username") String username) {
        UserValidators.existenceValidatorOf(username, "Can't get '" + username + "' image").validate(premiumUserService.findByUserName(username));
        Optional<byte[]> media = premiumUserService.readImage(username);
        if(!media.isPresent()) {
            LOGGER.trace("Returning default image: {} has not set an image yet", username);
            return Response.status(HttpStatus.TEMPORARY_REDIRECT.value())
                    .header("Location", URLConstants.DEFAULT_USER_IMAGE_URL).build();
        }
        LOGGER.trace("Returning image for {}", username);
        return Response.ok(media.get()).header("Content-Type", "image/*").build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteAUser(@PathParam("username") String username) {
        /*TODO| Validate that te user to be delete is the same as the one logged*/
        if (!premiumUserService.remove(username)) {
            LOGGER.trace("User '{}' does not exist", username);
            throw new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        }
        LOGGER.trace("User '{}' deleted successfully", username);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{username}")
    public Response modifyAUser(@PathParam("username") String username, final UserDto userDto) {
        /*TODO| Validate userDto only image and password can be null to indicate that they do
          TODO|not change. UserName should be null because it cant change. The rest
          TODO|should not be null. Check if it the user logged is the same as the username receive*/
        byte[] image = Validator.getValidator().validateAndProcessImage(userDto.getImage());
        PremiumUser newPremiumUser = premiumUserService.updateUserInfo(userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), userDto.getUsername(), userDto.getCellphone(), userDto.getBirthday(),
                userDto.getHome().getCountry(), userDto.getHome().getState(), userDto.getHome().getCity(),
                userDto.getHome().getStreet(), userDto.getReputation(), userDto.getPassword(), image, username)
                .orElseThrow(() -> {
                    LOGGER.trace("User '{}' does not exist", username);
                    return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
                });
        LOGGER.trace("User '{}' modified successfully", username);
        return Response.ok(UserDto.from(newPremiumUser)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAUser(final UserDto userDto) {
        /*TODO| Validate*/
        byte[] image = Validator.getValidator().validateAndProcessImage(userDto.getImage());
        PremiumUser newPremiumUser = premiumUserService.create(userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), userDto.getUsername(), userDto.getCellphone(), userDto.getBirthday(),
                userDto.getHome().getCountry(), userDto.getHome().getState(), userDto.getHome().getCity(),
                userDto.getHome().getStreet(), userDto.getReputation(), userDto.getPassword(), image)
                .orElseThrow(() -> {
                    LOGGER.trace("User '{}' already exist", userDto.getUsername());
                    return new ApiException(HttpStatus.CONFLICT, "User '" + userDto.getUsername() + "' already exist");
                });
        LOGGER.trace("User '{}' created successfully", userDto.getUsername());
        return Response.status(HttpStatus.CREATED.value()).entity(UserDto.from(newPremiumUser)).build();
    }

    @GET
    @Path("/{username}")
    public Response getAUser(@PathParam("username") String username) {
        PremiumUser premiumUser = premiumUserService.findByUserName(username).orElseThrow(() -> {
            LOGGER.trace("User '{}' does not exist", username);
            return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        });
        LOGGER.trace("User '{}' founded successfully", username);
        return Response.ok(UserDto.from(premiumUser)).build();
    }

    @POST
    @Path("/{username}/verification")
    public Response verifyAUser(@PathParam("username") String username, String code) {
        /*TODO| Validate that te user to be delete is the same as the one logged. Maybe it is not need, because
        * TODO|the code is receive in the mail.*/
        Boolean result = premiumUserService.enableUser(username, code).orElseThrow(() -> {
            LOGGER.trace("User '{}' does not exist", username);
            return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        });
        if(!result) {
            LOGGER.trace("User '{}' with code '{}' does not exist", username, code);
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid verification code for user '" + username + "'");
        }
        LOGGER.trace("User '{}' verified successfully", username);
        PremiumUser premiumUser = premiumUserService.findByUserName(username).orElseThrow(() -> {
            LOGGER.trace("User '{}' does not exist", username);
            return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        });
        return Response.ok(UserDto.from(premiumUser)).build();
    }
}
