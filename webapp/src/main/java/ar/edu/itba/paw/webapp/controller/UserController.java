package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.webapp.form.MatchForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RequestMapping("/user")
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService us;

    @RequestMapping(value = "/create", method = {RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("registerForm") UserForm userForm){
        return new ModelAndView("createUser");
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                               final BindingResult errors){
        if(errors.hasErrors()) {
            return createForm(userForm);
        }
        //final User u = us.create(userForm.getUsername(), "a", "b" );
        //return new ModelAndView("redirect:/userId=" + u.getUserId());
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.GET })
    public ModelAndView createMatchForm(@ModelAttribute("createMatchForm") MatchForm matchForm){
        return new ModelAndView("createMatch");
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.POST })
    public ModelAndView createMatch(@Valid @ModelAttribute("createMatchForm") final MatchForm matchForm,
                                    final BindingResult errors){
        if(errors.hasErrors()) {
            LOGGER.debug("date received: " + matchForm.getDate());
            return createMatchForm(matchForm);
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");

    }
}
