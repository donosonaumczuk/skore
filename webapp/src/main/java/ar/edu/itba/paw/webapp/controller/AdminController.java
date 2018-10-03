package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.Exceptions.CannotCreateSportException;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.webapp.form.SportForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController{
    @Autowired

    @Qualifier("sportServiceImpl")
    private SportService sportService;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("admin/index");
    }

    @RequestMapping(value = "/createSport", method = {RequestMethod.GET })
    public ModelAndView createSportForm(@ModelAttribute("createSportForm") SportForm sportForm){
        return new ModelAndView("admin/addSport");
    }

    @RequestMapping(value = "/createSport", method = {RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("createSportForm") final SportForm sportForm,
                               final BindingResult errors, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if(errors.hasErrors()) {
            System.out.println("imageFile: " + sportForm.getImage().getContentType() + "\n\n\n\n");
            return createSportForm(sportForm);
        }
        final Sport sport = sportService.create(sportForm.getSportName(), sportForm.getPlayerQuantity(),
                sportForm.getDisplayName(), sportForm.getImage());
        if(sport == null) {
            throw new CannotCreateSportException("Can't create sport " + sportForm.getSportName());
        }
        return new ModelAndView("/admin/index");
    }


}
