package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.TeamService;
import ar.edu.itba.paw.models.GameSort;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.QueryList;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.Team;
import ar.edu.itba.paw.models.UserSort;
import ar.edu.itba.paw.webapp.auth.token.JWTUtility;
import ar.edu.itba.paw.webapp.constants.MessageConstants;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.AuthDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GamePageDto;
import ar.edu.itba.paw.webapp.dto.LikeSportDto;
import ar.edu.itba.paw.webapp.dto.LikeSportPageDto;
import ar.edu.itba.paw.webapp.dto.LikeUserDto;
import ar.edu.itba.paw.webapp.dto.LikeUserPageDto;
import ar.edu.itba.paw.webapp.dto.PlaceDto;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.TeamDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserPageDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.JSONUtils;
import ar.edu.itba.paw.webapp.utils.LocaleUtils;
import ar.edu.itba.paw.webapp.utils.QueryParamsUtils;
import ar.edu.itba.paw.webapp.validators.ImageValidators;
import ar.edu.itba.paw.webapp.validators.SportValidators;
import ar.edu.itba.paw.webapp.validators.UserValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.constants.HeaderConstants.CODE_HEADER;
import static ar.edu.itba.paw.webapp.constants.HeaderConstants.TOKEN_HEADER;
import static ar.edu.itba.paw.webapp.controller.UserController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public static final String BASE_PATH = "users";
    private static final int ONE_HOUR = 3600;

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
    private JWTUtility jwtUtility;

    private static Resource defaultImage = new ClassPathResource("user-default.png");

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

    public static String getLikedUserEndpoint(final String username, final String usernameLiked) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username)
                .path("likedUsers").path(usernameLiked).toTemplate();
    }

    public static String getLikedSportEndpoint(String username, String sportName) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(username)
                .path("likedSport").path(sportName).toTemplate();
    }

    @GET
    public Response getUsers(@QueryParam("minReputation") String minReputation,
                             @QueryParam("maxReputation") String maxReputation,
                             @QueryParam("likedUsers") QueryList likedUsernames,
                             @QueryParam("sports") QueryList sportsLiked,
                             @QueryParam("usernames") QueryList usernames,
                             @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                             @QueryParam("sortBy") UserSort sort, @Context UriInfo uriInfo) { //TODO: winrate
        Page<UserDto> userPage = premiumUserService.findUsersPage( QueryParamsUtils.getQueryListOrNull(usernames),
                QueryParamsUtils.getQueryListOrNull(sportsLiked),  QueryParamsUtils.getQueryListOrNull(likedUsernames),
                QueryParamsUtils.positiveIntegerOrNull(minReputation),
                QueryParamsUtils.positiveIntegerOrNull(maxReputation), null, null, sort,
                QueryParamsUtils.positiveIntegerOrNull(offset), QueryParamsUtils.positiveIntegerOrNull(limit),
                false).map(UserDto::from);

        LOGGER.trace("Users successfully gotten");
        return Response.ok().entity(UserPageDto.from(userPage, uriInfo)).build();
    }

    @GET
    @Path("/{username}/matches")
    public Response getUserGames(@PathParam("username") String username,
                                 @QueryParam("minStartTime") String minStartTime,
                                 @QueryParam("maxStartTime") String maxStartTime,
                                 @QueryParam("minFinishTime") String minFinishTime,
                                 @QueryParam("maxFinishTime") String maxFinishTime,
                                 @QueryParam("minQuantity") String minQuantity,
                                 @QueryParam("maxQuantity") String maxQuantity,
                                 @QueryParam("minFreePlaces") String minFreePlaces,
                                 @QueryParam("maxFreePlaces") String maxFreePlaces,
                                 @QueryParam("country") QueryList countries,
                                 @QueryParam("state") QueryList states,
                                 @QueryParam("city") QueryList cities,
                                 @QueryParam("sport") QueryList sports,
                                 @QueryParam("type") QueryList types,
                                 @QueryParam("withPlayers") QueryList usernamesPlayersInclude,
                                 @QueryParam("withoutPlayers") QueryList usernamesPlayersNotInclude,
                                 @QueryParam("createdBy") QueryList usernamesCreatorsInclude,
                                 @QueryParam("notCreatedBy") QueryList usernamesCreatorsNotInclude,
                                 @QueryParam("limit") String limit, @QueryParam("offset") String offset,
                                 @QueryParam("sortBy") GameSort sort, @Context UriInfo uriInfo,
                                 @QueryParam("hasResult") String hasResult, @QueryParam("onlyLikedUsers") String onlyLikedUsers,
                                 @QueryParam("onlyLikedSports") String onlyLikedSports) {
        if (usernamesPlayersInclude == null) {
            usernamesPlayersInclude = new QueryList(new ArrayList<>());
        }
        usernamesPlayersInclude.getQueryValues().add(username);
        Page<GameDto> page = gameService.findGamesPage(QueryParamsUtils.localDateTimeOrNull(minStartTime),
                QueryParamsUtils.localDateTimeOrNull(maxStartTime), QueryParamsUtils.localDateTimeOrNull(minFinishTime),
                QueryParamsUtils.localDateTimeOrNull(maxFinishTime),  QueryParamsUtils.getQueryListOrNull(types),
                QueryParamsUtils.getQueryListOrNull(sports), QueryParamsUtils.positiveIntegerOrNull(minQuantity),
                QueryParamsUtils.positiveIntegerOrNull(maxQuantity), QueryParamsUtils.getQueryListOrNull(countries),
                QueryParamsUtils.getQueryListOrNull(states), QueryParamsUtils.getQueryListOrNull(cities),
                QueryParamsUtils.positiveIntegerOrNull(minFreePlaces), QueryParamsUtils.positiveIntegerOrNull(maxFreePlaces),
                QueryParamsUtils.getQueryListOrNull(usernamesPlayersInclude),  QueryParamsUtils.getQueryListOrNull(usernamesPlayersNotInclude),
                QueryParamsUtils.getQueryListOrNull(usernamesCreatorsInclude),  QueryParamsUtils.getQueryListOrNull(usernamesCreatorsNotInclude),
                QueryParamsUtils.positiveIntegerOrNull(limit), QueryParamsUtils.positiveIntegerOrNull(offset), sort,
                QueryParamsUtils.booleanOrNull(hasResult), QueryParamsUtils.booleanOrElse(onlyLikedUsers, false),
                QueryParamsUtils.booleanOrElse(onlyLikedSports, false))
                .map((game) ->GameDto.from(game, getTeam(game.getTeam1()), getTeam(game.getTeam2())));

        LOGGER.trace("'{}' matches successfully gotten", username);
        return Response.ok().entity(GamePageDto.from(page, uriInfo)).build();
    }

    @GET
    @Path("/{username}/profile")
    public Response getUserProfile(@PathParam("username") String username) {
        PremiumUser premiumUser = premiumUserService.findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        LOGGER.trace("'{}' profile successfully gotten", username);
        return Response.ok(ProfileDto.from(premiumUser)).build();
    }

    @GET
    @Path("/{username}/image")
    public Response getUserImage(@PathParam("username") String username) {
        Optional<byte[]> media = premiumUserService.readImage(username);
        CacheControl cache = CacheUtils.getCacheControl(ONE_HOUR);
        Date expireDate = CacheUtils.getExpire(ONE_HOUR);
        if (!media.isPresent()) {
            LOGGER.trace("Returning default image: {} has not set an image yet", username);
            return Response.ok(getDefaultImage()).header(HttpHeaders.CONTENT_TYPE, "image/*")
                    .cacheControl(cache).expires(expireDate).build();
        }
        LOGGER.trace("Returning image for {}", username);
        return Response.ok(media.get()).header(HttpHeaders.CONTENT_TYPE, "image/*").cacheControl(cache)
                .expires(expireDate).build();
    }

    @PUT
    @Path("/{username}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUser(@PathParam("username") String username, @RequestBody final String requestBody,
                               @Context HttpServletRequest request) {
        LOGGER.trace("Trying to update '{}' user", username);
        UserValidators.updateValidatorOf("User '" + username + "' update failed, invalid update JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final UserDto userDto = JSONUtils.jsonToObject(requestBody, UserDto.class);
        Locale locale = LocaleUtils.validateLocale(request.getLocales());
        String code = request.getHeader(CODE_HEADER);
        byte[] image = ImageValidators.validateAndProcessImage(userDto.getImage());
        PremiumUser updatedPremiumUser = premiumUserService.updateUserInfo(
                username, userDto.getFirstName(), userDto.getLastName(),
                userDto.getEmail(), userDto.getCellphone(), getBirthDay(userDto),
                userDto.getHome().map(PlaceDto::getCountry).orElse(null),
                userDto.getHome().map(PlaceDto::getState).orElse(null),
                userDto.getHome().map(PlaceDto::getCity).orElse(null),
                userDto.getHome().map(PlaceDto::getStreet).orElse(null),
                userDto.getReputation(), userDto.getPassword(), userDto.getOldPassword(),image, locale, code
        );
        return Response.ok(UserDto.from(updatedPremiumUser)).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createUser(@RequestBody final String requestBody, @Context HttpServletRequest request) {
        UserValidators.creationValidatorOf("User creation fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final UserDto userDto = JSONUtils.jsonToObject(requestBody, UserDto.class);
        byte[] image = ImageValidators.validateAndProcessImage(userDto.getImage());
        Locale locale = LocaleUtils.validateLocale(request.getLocales());
        PremiumUser newPremiumUser = premiumUserService.create(
                userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
                userDto.getUsername(), userDto.getCellphone(), getBirthDay(userDto),
                userDto.getHome().map(PlaceDto::getCountry).orElse(null),
                userDto.getHome().map(PlaceDto::getState).orElse(null),
                userDto.getHome().map(PlaceDto::getCity).orElse(null),
                userDto.getHome().map(PlaceDto::getStreet).orElse(null),
                userDto.getReputation(), userDto.getPassword(), image, locale
        );
        LOGGER.trace("User '{}' created successfully", userDto.getUsername());
        return Response.status(HttpStatus.CREATED.value()).entity(UserDto.from(newPremiumUser))
                .header("Accept-Language", locale.toString()).build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        PremiumUser premiumUser = premiumUserService.findByUserName(username).orElseThrow(() -> {
            LOGGER.error("Can't find user with username: {}", username);
            return UserNotFoundException.ofUsername(username);
        });
        LOGGER.trace("User '{}' found successfully", username);
        return Response.ok(UserDto.from(premiumUser)).build();
    }

    @POST
    @Path("/{username}/verification")
    public Response verifyUser(@PathParam("username") String username, @RequestBody String code) {
        LOGGER.trace("Trying to verify '{}' user", username);
        PremiumUser premiumUser = premiumUserService.enableUser(username, code);
        return Response.ok(AuthDto.from(premiumUser)).header(TOKEN_HEADER, jwtUtility.createToken(premiumUser)).build();
    }

    @GET
    @Path("/{username}/likedUsers")
    public Response getLikedUser(@PathParam("username") String username,  @QueryParam("limit") String limit,
                                 @QueryParam("offset") String offset, @Context UriInfo uriInfo) {
        Page<LikeUserDto> page = premiumUserService.getLikedUsers(username,
                QueryParamsUtils.positiveIntegerOrNull(offset), QueryParamsUtils.positiveIntegerOrNull(limit))
                .map((p) -> LikeUserDto.from(p, username));
        LOGGER.trace("'{}' liked users successfully gotten", username);
        return Response.ok().entity(LikeUserPageDto.from(page, uriInfo)).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{username}/likedUsers")
    public Response addLikedUser(@PathParam("username") String username, @RequestBody final String requestBody) {
        UserValidators.likedUserCreationValidator("User like fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final LikeUserDto likeUserDto = JSONUtils.jsonToObject(requestBody, LikeUserDto.class);
        PremiumUser userLiked = premiumUserService.addLikedUser(username, likeUserDto.getUsername());
        LOGGER.trace("'{}' liked '{}' successfully created", username, likeUserDto.getUsername());
        return Response.status(HttpStatus.CREATED.value()).entity(LikeUserDto.from(userLiked, username)).build();
    }

    @GET
    @Path("/{username}/likedUsers/{usernameOfLiked}")
    public Response getLikedUser(@PathParam("username") String username,
                                 @PathParam("usernameOfLiked") String usernameOfLiked) {
        PremiumUser likedUser = premiumUserService.getLikedUser(username, usernameOfLiked);
        LOGGER.trace("Liked user '{}' get successfully", usernameOfLiked);
        return Response.ok().entity(LikeUserDto.from(likedUser, username)).build();
    }

    @DELETE
    @Path("/{username}/likedUsers/{usernameOfLiked}")
    public Response removeLikedUser(@PathParam("username") String username,
                                    @PathParam("usernameOfLiked") String usernameOfLiked) {
        premiumUserService.removeLikedUser(username, usernameOfLiked);
        LOGGER.trace("User '{}' deleted successfully", username);
        return Response.noContent().build();
    }

    @GET
    @Path("/{username}/likedSports")
    public Response getLikedSport(@PathParam("username") String username,  @QueryParam("limit") String limit,
                                 @QueryParam("offset") String offset, @Context UriInfo uriInfo) {
        Page<LikeSportDto> page = premiumUserService.getLikedSports(username,
                QueryParamsUtils.positiveIntegerOrNull(offset), QueryParamsUtils.positiveIntegerOrNull(limit))
                .map((s) -> LikeSportDto.from(s, username));
        LOGGER.trace("'{}' liked sports successfully gotten", username);
        return Response.ok().entity(LikeSportPageDto.from(page, uriInfo)).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{username}/likedSports")
    public Response addLikedSport(@PathParam("username") String username, @RequestBody final String requestBody) {
        SportValidators.likedSportCreationValidator("Sport like fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final LikeSportDto likeSportDto = JSONUtils.jsonToObject(requestBody, LikeSportDto.class);
        Sport sportLiked = premiumUserService.addLikedSport(username, likeSportDto.getSportName());
        LOGGER.trace("'{}' liked '{}' successfully created", username, likeSportDto.getSportName());
        return Response.status(HttpStatus.CREATED.value()).entity(LikeSportDto.from(sportLiked, username)).build();
    }

    @GET
    @Path("/{username}/likedSports/{sportnameOfLiked}")
    public Response getLikedSport(@PathParam("username") String username,
                                 @PathParam("sportnameOfLiked") String sportnameOfLiked) {
        Sport likedSport = premiumUserService.getLikedSport(username, sportnameOfLiked);
        LOGGER.trace("Sport like '{}' from user '{}' get successfully", sportnameOfLiked, username);
        return Response.ok().entity(LikeSportDto.from(likedSport, username)).build();
    }

    @DELETE
    @Path("/{username}/likedSports/{sportnameOfLiked}")
    public Response removeLikedSport(@PathParam("username") String username,
                                    @PathParam("sportnameOfLiked") String sportnameOfLiked) {
        premiumUserService.removeLikedSport(username, sportnameOfLiked);
        LOGGER.trace("Sport like '{}' from user '{}' deleted successfully", sportnameOfLiked, username);
        return Response.noContent().build();
    }

    @POST
    @Path("/{username}/forgotPassword")
    public Response forgotPassword(@PathParam("username") String username, @RequestBody final String requestBody,
                                  @Context HttpServletRequest request) {
        UserValidators.resetPasswordPost("User password reset fails, invalid creation JSON")
                .validate(JSONUtils.jsonObjectFrom(requestBody));
        final UserDto userDto = JSONUtils.jsonToObject(requestBody, UserDto.class);
        Locale locale = LocaleUtils.validateLocale(request.getLocales());
        premiumUserService.forgotPassword(username, userDto.getEmail(), locale);
        LOGGER.trace("User '{}' password reset process started successfully");
        return  Response.ok().build();
    }

    private byte[] getDefaultImage() {
        ByteArrayOutputStream bos;
        try {
            BufferedImage bImage = ImageIO.read(defaultImage.getFile());
            bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
        }
        catch (IOException e) {
            throw ApiException.of(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.SERVER_ERROR_GENERIC_MESSAGE);
        }
        return bos.toByteArray();
    }

    private LocalDate getBirthDay(UserDto userDto) {
        if (userDto.getBirthday() == null) {
            return null;
        }
        return LocalDate.of(userDto.getBirthday().getYear(), userDto.getBirthday().getMonthNumber(),
                userDto.getBirthday().getDayOfMonth());
    }

    private TeamDto getTeam(Team team) {
        return Optional.ofNullable(team)
                .map(it -> TeamDto.from(teamService.getAccountsMap(it), it))
                .orElse(null);
    }
}
