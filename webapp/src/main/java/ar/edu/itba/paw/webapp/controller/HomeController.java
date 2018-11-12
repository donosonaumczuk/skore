package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.SimpleEncrypter;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MatchForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(name = "country", required = false) String[] countries,
                              @RequestParam(name = "state", required = false) String[] states,
                              @RequestParam(name = "city", required = false) String[] cities,
                              @RequestParam Map<String, String> requestParams) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //TODO: DELETE THIS DEBUG LINES
        if(requestParams != null) {
            for (String filter : requestParams.keySet()) {
                if(!isValidFilter(filter)) {
                    return new ModelAndView("404"); //TODO: add a message like '404 bad request: wrong params'
                }
            }
        }

        if(countries != null) {
            for (String country : countries) { //TODO: DELETE THIS DEBUG LINES
                System.out.println("\ncountry = " + country);
            }
        }

        if(states != null) {
            for (String state : states) { //TODO: DELETE THIS DEBUG LINES
                System.out.println("\nstate = " + state);
            }
        }

        if(cities != null) {
            for (String city : cities) { //TODO: DELETE THIS DEBUG LINES
                System.out.println("\ncity = " + city);
            }
        }

        final ModelAndView mav = new ModelAndView("index").addObject("section", "default");
        return mav;

    }

    private boolean isValidFilter(String filter) {
        if(filter == null) {
            return false;
        }

        if(filter.equals("country") || filter.equals("state") || filter.equals("city")) {
            return true;
        }

        return false;
    }


    @RequestMapping(value = "/joined", method = RequestMethod.GET)
    public ModelAndView joinedMatches() {
        final ModelAndView mav = new ModelAndView("index").addObject("section", "joined");
        return mav;
    }

    @RequestMapping(value = "/created", method = RequestMethod.GET)
    public ModelAndView createdMatches() {
        final ModelAndView mav = new ModelAndView("index").addObject("section", "created");
        return mav;
    }
}
