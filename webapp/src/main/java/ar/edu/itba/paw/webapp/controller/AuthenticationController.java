package ar.edu.itba.paw.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static ar.edu.itba.paw.webapp.controller.AuthenticationController.BASE_PATH;

@Controller
@Path(BASE_PATH)
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    public static final String BASE_PATH = "auth";

    @POST
    @Path("/logout")
    public Response logout() {
        return Response.ok().build();
    }

    //TODO mayde add login endpoint
}
