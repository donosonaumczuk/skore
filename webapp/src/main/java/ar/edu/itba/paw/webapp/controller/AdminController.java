package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.SportNotFoundException;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.form.EditSportForm;
import ar.edu.itba.paw.webapp.form.SportForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController{
    @Autowired

    @Qualifier("sportServiceImpl")
    private SportService sportService;

    @RequestMapping("/")
    public ModelAndView index() {
        List<Sport> sports = sportService.getAllSports();

        if(sports == null) {
            sports = new LinkedList<>();
        }

        return new ModelAndView("admin/index").addObject("sports", sports);
    }

    @RequestMapping("/removeSport/**")
    public ModelAndView removeSport(HttpServletRequest request) {
        String sportName = request.getServletPath().replace("/admin/removeSport/", "");

        try {
            sportService.findByName(sportName);
        }
        catch (SportNotFoundException e) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "sportNotFoundMessage").addObject("attribute", "");
        }

        //sportService.remove(sportName);
        List<Sport> sports = sportService.getAllSports();

        if(sports == null) {
            sports = new LinkedList<>();
        }

        return new ModelAndView("admin/index").addObject("sports", sports);
    }

    @RequestMapping(value="/editSport/{sportName}",  method = {RequestMethod.GET})
    public ModelAndView editSportForm(@ModelAttribute("editSportForm") EditSportForm editSportForm,
                                      HttpServletRequest request, @PathVariable String sportName) {
        //String sportName = request.getServletPath().replace("/admin/editSport/", "");
        Sport sport = null;
        try {
            sport = sportService.findByName(sportName);
        }
        catch (SportNotFoundException e) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "sportNotFoundMessage").addObject("attribute", "");
        }

        return new ModelAndView("admin/editSport").addObject("sport", sport);
    }

    @RequestMapping(value="/editSport/*",  method = {RequestMethod.POST})
    public ModelAndView editSport(@Valid @ModelAttribute("editSportForm") final EditSportForm editSportForm,
                                  final BindingResult errors, @RequestParam(value = "file", required = false)
                                              MultipartFile file, HttpServletRequest request) throws IOException {
        if(errors.hasErrors()) {
            return editSportForm(editSportForm, request, editSportForm.getSportName());
        }

        MultipartFile image = editSportForm.getImage();

        if(image.getSize() == 0) {
            image = null;
        }

        try {
            sportService.findByName(editSportForm.getSportName());
            sportService.modifySport(editSportForm.getSportName(), editSportForm.getDisplayName(),
                    image);
        }
        catch (Exception e) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "sportNotFoundMessage").addObject("attribute", "");
        }

        List<Sport> sports = sportService.getAllSports();

        if(sports == null) {
            sports = new LinkedList<>();
        }

        return new ModelAndView("/admin/index").addObject("sports", sports);
    }


    @RequestMapping(value = "/createSport", method = {RequestMethod.GET })
    public ModelAndView createSportForm(@ModelAttribute("createSportForm") SportForm sportForm){
        return new ModelAndView("admin/addSport");
    }

    @RequestMapping(value = "/createSport", method = {RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("createSportForm") final SportForm sportForm,
                               final BindingResult errors, @RequestParam(value = "file", required = false)
                                           MultipartFile file) throws IOException {
        if(errors.hasErrors()) {
            return createSportForm(sportForm);
        }

        int playerQuantity= 0;
        for(int i = 0; i< sportForm.getPlayerQuantity().length(); i++) {
            playerQuantity = playerQuantity * 10 + (sportForm.getPlayerQuantity().charAt(i)-'0');
        }


        try {
            final Sport sport = sportService.create(sportForm.getSportName(), playerQuantity,
                    sportForm.getDisplayName(), sportForm.getImage());
        }
        catch (Exception e) {
            return new ModelAndView("/admin/sportDuplicated").addObject("sportName", sportForm.getSportName());
        }

        List<Sport> sports = sportService.getAllSports();

        if(sports == null) {
            sports = new LinkedList<>();
        }

        return new ModelAndView("/admin/index").addObject("sports", sports);
    }


}
