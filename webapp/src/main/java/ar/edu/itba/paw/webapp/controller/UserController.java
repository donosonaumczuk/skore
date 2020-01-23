package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.UserSort;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamePageDto;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.UserPageDto;
import ar.edu.itba.paw.webapp.utils.QueryParamsUtils;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private SessionService sessionService;

    public static String getUserProfileEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("profile").toTemplate();
    }

    public static String getUserEndpoint(String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).toTemplate();
    }

    public static String getUserGamesEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("matches").toTemplate();
    }

    public static String getUserSportsEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("sports").toTemplate();
    }

    public static String getUserImageEndpoint(final String username) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username).path("image").toTemplate();
    }

    @GET
    public Response getUsers(@QueryParam("minReputation") String minReputation,
                             @QueryParam("maxReputation") String maxReputation,
                             @QueryParam("withPlayers") List<String> usernamesPlayersInclude,
                             @QueryParam("friends") List<String> friendsUsernames,
                             @QueryParam("sports") List<String> sportsLiked,
                             @QueryParam("usernames") List<String> usernames,
                             @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                             @QueryParam("sortBy") UserSort sort, @Context UriInfo uriInfo) { //TODO: winrate
        Page<UserDto> userPage = premiumUserService.findUsersPage(usernames, sportsLiked, friendsUsernames,
                QueryParamsUtils.positiveIntegerOrNull(minReputation),
                QueryParamsUtils.positiveIntegerOrNull(maxReputation), null, null, sort,
                QueryParamsUtils.positiveIntegerOrNull(offset), QueryParamsUtils.positiveIntegerOrNull(limit))
                .map(UserDto::from);

        LOGGER.trace("Users successfully gotten");
        return Response.ok().entity(UserPageDto.from(userPage, uriInfo)).build();
    }

    @GET
    @Path("/{username}/matches")
    public Response getUserGames(@PathParam("username") String username, @QueryParam("limit") String limit,
                                 @QueryParam("offSet") String offset, @Context UriInfo uriInfo) {
        List<String> usernames = new ArrayList<>();
        usernames.add(username);
        Page<GameDto> page = gameService.findGamesPage(null, null,null, null,
            null, null, null,null, null, null, null,
                null, null, usernames, null, null,
                null, QueryParamsUtils.positiveIntegerOrNull(limit),
                QueryParamsUtils.positiveIntegerOrNull(offset), null)
                .map((game) ->GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2())));

        LOGGER.trace("'{}' matches successfully gotten", username);
        return Response.ok().entity(GamePageDto.from(page, uriInfo)).build();
    }

    private TeamDto getTeam(Team team) {
        if (team == null) {
            return null;
        }
        return TeamDto.from(teamService.getAccountsMap(team), team);
    }

    @GET
    @Path("/{username}/profile")
    public Response getUserProfile(@PathParam("username") String username) {
        Optional<PremiumUser> premiumUserOptional = premiumUserService.findByUserName(username);
        UserValidators.existenceValidatorOf(username, "Can't get '" + username + "' profile").validate(premiumUserOptional);
        LOGGER.trace("'{}' profile successfully gotten", username);
        return Response.ok(ProfileDto.from(premiumUserOptional.get())).build();
    }

    @GET
    @Path("/{username}/image")
    public Response getUserImage(@PathParam("username") String username) {
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
    public Response deleteUser(@PathParam("username") String username) {
        UserValidators.isAuthorizedForUpdateValidatorOf(username, "User '" + username
                + "' deletion failed, unauthorized").validate(sessionService.getLoggedUser());
        if (!premiumUserService.remove(username)) {
            LOGGER.trace("User '{}' does not exist", username);
            throw new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        }
        LOGGER.trace("User '{}' deleted successfully", username);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{username}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUser(@PathParam("username") String username, @RequestBody final String requestBody) {
        UserValidators.isAuthorizedForUpdateValidatorOf(username, "User '" + username
                + "' update failed, unauthorized").validate(sessionService.getLoggedUser());
        UserValidators.updateValidatorOf("User '" + username + "' update failed, invalid update JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final UserDto userDto = JSONUtils.jsonToObject(requestBody, UserDto.class);
        byte[] image = Validator.getValidator().validateAndProcessImage(userDto.getImage()); //TODO: maybe separate validating from obtaining
        //TODO: check what to do with UserDto and nullable fields, userDto.getHome().getCountry() -> NPE if getHome() returns null!
        Optional<PremiumUser> newPremiumUser = premiumUserService.updateUserInfo(userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), userDto.getUsername(), userDto.getCellphone(), userDto.getBirthDay(),
                userDto.getHome().getCountry(), userDto.getHome().getState(), userDto.getHome().getCity(),
                userDto.getHome().getStreet(), userDto.getReputation(), userDto.getPassword(), image, username);
        UserValidators.existenceValidatorOf(username,"User update fails, user '" + username + "' does not exist")
                .validate(newPremiumUser);
        LOGGER.trace("User '{}' modified successfully", username);
        return Response.ok(UserDto.from(newPremiumUser.get())).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(@RequestBody final String requestBody) {
        UserValidators.creationValidatorOf("User creation fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final UserDto userDto = JSONUtils.jsonToObject(requestBody, UserDto.class);
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
    public Response getUser(@PathParam("username") String username) {
        PremiumUser premiumUser = premiumUserService.findByUserName(username).orElseThrow(() -> {
            LOGGER.trace("User '{}' does not exist", username);
            return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
        });
        LOGGER.trace("User '{}' founded successfully", username);
        return Response.ok(UserDto.from(premiumUser)).build();
    }

    @POST
    @Path("/{username}/verification")
    public Response verifyUser(@PathParam("username") String username, String code) {
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
