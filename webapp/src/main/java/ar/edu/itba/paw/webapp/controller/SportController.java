package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.SportDto;
import ar.edu.itba.paw.webapp.dto.SportListDto;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.controller.SportController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class SportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportController.class);

    public static final String BASE_PATH = "sports";

    private static final int MAX_DISPLAY_NAME_LENGTH = 100;

    @Autowired
    @Qualifier("sportServiceImpl")
    private SportService sportService;

    public static String getSportEndpoint(final String sportname) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(sportname).toTemplate();
    }

    public static String getSportImageEndpoint(final String sportname) {
        return URLConstants.getApiBaseUrlBuilder().path(BASE_PATH).path(sportname).path("image").toTemplate();
    }

    @GET
    @Path("/{sportname}/image")
    public Response getImageSport(@PathParam("sportname") String sportname) {
        LOGGER.trace("Trying to retrieve image of sport '{}'", sportname);
        byte[] media = sportService.readImage(sportname)
                .orElseThrow(()-> {
                    LOGGER.trace("Can't get '{}' sport image, sport does not exist", sportname);
                    return new ApiException(HttpStatus.BAD_REQUEST, "Sport '" + sportname + "' does not exist");
                });
        LOGGER.trace("Sport '{}' image retrieved successfully", sportname);
        return Response.ok(media).header(HttpHeaders.CONTENT_TYPE, com.google.common.net.MediaType.ANY_IMAGE_TYPE).build();
    }

    @GET
    @Path("/")
    public Response getAllSports() {
        LOGGER.trace("Getting all sports");
        return Response.ok(SportListDto.from(sportService.getAllSports().stream()
                .map(SportDto::from).collect(Collectors.toList()))).build();
    }

    @GET
    @Path("/{sportname}")
    public Response getASport(@PathParam("sportname") String sportname) {
        Sport sport = sportService.findByName(sportname).orElseThrow(() -> {
            LOGGER.trace("Sport '{}' does not exist", sportname);
            return new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
        });
        LOGGER.trace("Sport '{}' founded successfully", sportname);
        return Response.ok(SportDto.from(sport)).build();
    }

    @DELETE
    @Path("/{sportname}")
    public Response deleteASport(@PathParam("sportname") String sportname) {
        if (!sportService.remove(sportname)) {
            LOGGER.trace("Sport '{}' does not exist", sportname);
            throw new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
        }
        LOGGER.trace("Sport '{}' deleted successfully", sportname);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{sportname}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response modifyASport(@PathParam("sportname") String sportname, final SportDto sportDto) {
        byte[] imageBytes = validateAndProcessSportDto(sportDto);

        Sport newSport = sportService.modifySport(sportname, sportDto.getDisplayName(), imageBytes)
                .orElseThrow(() -> {
                    LOGGER.trace("Sport '{}' does not exist", sportname);
                    return new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
                });
        LOGGER.trace("Sport '{}' modified successfully", sportname);
        return Response.ok(SportDto.from(newSport)).build();
    }

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createASport(final SportDto sportDto) {
        Validator.getValidator().fieldHasData(sportDto.getImageSport(), "image");
        byte[] imageBytes = validateAndProcessSportDto(sportDto);

        Sport newSport = sportService.create(sportDto.getSportName(), sportDto.getPlayerQuantity(),
                sportDto.getDisplayName(), imageBytes)
                .orElseThrow(() -> {
                    LOGGER.trace("Sport '{}' already exist", sportDto.getSportName());
                    return new ApiException(HttpStatus.CONFLICT, "Sport '" +
                            sportDto.getSportName() + "' already exist");
                });
        LOGGER.trace("Sport '{}' created successfully", sportDto.getSportName());
        return Response.status(HttpStatus.CREATED.value()).entity(SportDto.from(newSport)).build();
    }

    private byte[] validateAndProcessSportDto(SportDto sportDto) {
        return Validator.getValidator()
                .isAlphaNumericAndLessThan(sportDto.getDisplayName(), "displayName", MAX_DISPLAY_NAME_LENGTH)
                .isAlphaNumericAndLessThan(sportDto.getSportName(), "sportName", MAX_DISPLAY_NAME_LENGTH)
                .isNumberGreaterThanZero(sportDto.getPlayerQuantity(), "playerQuantity")
                .validateAndProcessImage(sportDto.getImageSport());
    }
}
