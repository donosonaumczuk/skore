package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.webapp.constants.URLConstants;
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
import javax.ws.rs.core.Response;

import static ar.edu.itba.paw.webapp.controller.SportController.BASE_PATH;

@Controller
@Path(BASE_PATH)
public class SportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportController.class);

    public static final String BASE_PATH = "sports";

    @Autowired
    @Qualifier("sportServiceImpl")
    private SportService sportService;

    public static String getSportImageEndpoint(final String sportname) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(sportname).path("image").toTemplate();
    }

    @GET
    @Path("/{sportname}/image")
    public Response getImageSport(@PathParam("sportname") String sportname) {
        LOGGER.trace("Trying to retrieve image of sport '{}'", sportname);
        byte[] media = sportService.readImage(sportname)
                .orElseThrow(()-> {
            LOGGER.trace("Can't get '{}' sport, sport not found", sportname);
            return new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
        });
        LOGGER.trace("Successful retrieve image of sport '{}'", sportname);
        return Response.ok(media).header("Content-Type", "image/*").build();
    }
}
