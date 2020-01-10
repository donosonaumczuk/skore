package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.constants.URLConstants;
import ar.edu.itba.paw.webapp.dto.SportDtoInput;
import ar.edu.itba.paw.webapp.dto.SportDtoOutput;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.io.IOException;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.controller.SportController.BASE_PATH;

@Controller
@Path(BASE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class SportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportController.class);

    public static final String BASE_PATH = "sports";

    @Autowired
    @Qualifier("sportServiceImpl")
    private SportService sportService;

    public static String getSportEndpoint(String sportname) {
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
                    LOGGER.trace("Can't get '{}' sport, sport not found", sportname);
                    return new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
                });
        LOGGER.trace("Successful retrieve image of sport '{}'", sportname);
        return Response.ok(media).header("Content-Type", "image/*").build();
    }

    @GET
    @Path("/")
    public Response getAllSports() {
        LOGGER.trace("Getting all sports");
        return Response.ok(sportService.getAllSports().stream()
                .map(SportDtoOutput::new).collect(Collectors.toList())).build();
    }

    @DELETE
    @Path("/{sportname}")
    public Response deleteASport(@PathParam("sportname") String sportname) {
        if (!sportService.remove(sportname)) {
            LOGGER.trace("Sport '{}' does not exist", sportname);
            throw new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist");
        }
        LOGGER.trace("Successful delete the sport '{}'", sportname);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{sportname}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response modifyASport(@PathParam("sportname") String sportname, final SportDtoInput sportDto) throws IOException { //TODO: see what to do with exception
        //TODO validate
        Sport newSport = sportService.modifySport(sportname, sportDto.getDisplayName(), sportDto.getImageSport())
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "Sport '" + sportname + "' does not exist"));
        LOGGER.trace("Successful modify the sport '{}'", sportname);
        return Response.ok(SportDtoOutput.from(newSport)).build();
    }

    @POST
    @Path("/")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createASport(final SportDtoInput sportDto) throws IOException { //TODO: see what to do with exception
        //TODO validate
        Sport newSport = sportService.create(sportDto.getSportName(), sportDto.getPlayerQuantity(),
                sportDto.getDisplayName(), sportDto.getImageSport())
                .orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "Sport '" +
                        sportDto.getSportName() + "' already exist"));
        LOGGER.trace("Successful create the sport '{}'", sportDto.getSportName());
        return Response.ok(SportDtoOutput.from(newSport)).build();
    }

//
//    @RequestMapping(value="/editSport/{sportName}",  method = {RequestMethod.GET})
//    public ModelAndView editSportForm(@ModelAttribute("editSportForm") EditSportForm editSportForm,
//                                      HttpServletRequest request, @PathVariable String sportName) {
//        //String sportName = request.getServletPath().replace("/admin/editSport/", "");
//        Sport sport = null;
//        try {
//            sport = sportService.findByName(sportName);
//        }
//        catch (SportNotFoundException e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "sportNotFoundMessage").addObject("attribute", "");
//        }
//
//        return new ModelAndView("admin/editSport").addObject("sport", sport);
//    }
//
//    @RequestMapping(value="/editSport/*",  method = {RequestMethod.POST})
//    public ModelAndView editSport(@Valid @ModelAttribute("editSportForm") final EditSportForm editSportForm,
//                                  final BindingResult errors, @RequestParam(value = "file", required = false)
//                                              MultipartFile file, HttpServletRequest request) throws IOException {
//        if(errors.hasErrors()) {
//            return editSportForm(editSportForm, request, editSportForm.getSportName());
//        }
//
//        MultipartFile image = editSportForm.getImage();
//
//        if(image.getSize() == 0) {
//            image = null;
//        }
//
//        try {
//            sportService.findByName(editSportForm.getSportName());
//            sportService.modifySport(editSportForm.getSportName(), editSportForm.getDisplayName(),
//                    image);
//        }
//        catch (Exception e) {
//            return new ModelAndView("genericPageWithMessage").addObject("message",
//                    "sportNotFoundMessage").addObject("attribute", "");
//        }
//
//        List<Sport> sports = sportService.getAllSports();
//
//        if(sports == null) {
//            sports = new LinkedList<>();
//        }
//
//        return new ModelAndView("/admin/index").addObject("sports", sports);
//    }
//
//
//    @RequestMapping(value = "/createSport", method = {RequestMethod.GET })
//    public ModelAndView createSportForm(@ModelAttribute("createSportForm") SportForm sportForm){
//        return new ModelAndView("admin/addSport");
//    }
//
//    @RequestMapping(value = "/createSport", method = {RequestMethod.POST })
//    public ModelAndView create(@Valid @ModelAttribute("createSportForm") final SportForm sportForm,
//                               final BindingResult errors, @RequestParam(value = "file", required = false)
//                                           MultipartFile file) throws IOException {
//        if(errors.hasErrors()) {
//            return createSportForm(sportForm);
//        }
//
//        int playerQuantity= 0;
//        for(int i = 0; i< sportForm.getPlayerQuantity().length(); i++) {
//            playerQuantity = playerQuantity * 10 + (sportForm.getPlayerQuantity().charAt(i)-'0');
//        }
//
//
//        try {
//            final Sport sport = sportService.create(sportForm.getSportName(), playerQuantity,
//                    sportForm.getDisplayName(), sportForm.getImage());
//        }
//        catch (Exception e) {
//            return new ModelAndView("/admin/sportDuplicated").addObject("sportName", sportForm.getSportName());
//        }
//
//        List<Sport> sports = sportService.getAllSports();
//
//        if(sports == null) {
//            sports = new LinkedList<>();
//        }
//
//        return new ModelAndView("/admin/index").addObject("sports", sports);
//    }


}
