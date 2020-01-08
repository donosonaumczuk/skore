package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
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

import static ar.edu.itba.paw.webapp.controller.UserController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class UserController {

    public static final String BASE_PATH = "user";

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    public static String getProfileEndpoint(final String username) {
        return URLConstants.API_BASE_URL_BUILDER.path(BASE_PATH).path(username).path("profile").toTemplate();
    }

    @GET
    @Path("/{username}/profile")
    public Response getProfile(@PathParam("username") String username) {
        //TODO: if username must follow some condition, here we must put a validator! then the validator throws a 400 BAD REQUEST!
        PremiumUser premiumUser = premiumUserService.findByUserName(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User does not exist")); //TODO: I don't know if this exception must be thrown by the service instead returning Optional
        return Response.ok(new ProfileDto(premiumUser)).build();
    }
}