package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.form.JoinMatchForm;
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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
                               final BindingResult errors) {
        if(errors.hasErrors()) {
            return createForm(userForm);
        }
        final PremiumUser user = us.create(userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(),
                userForm.getUsername(), userForm.getCellphone(), userForm.getBirthday(), userForm.getCountry(),
                userForm.getState(), userForm.getCity(), userForm.getStreet(), 0, userForm.getPassword());
        //return new ModelAndView("redirect:/userId=" + u.getUserId());
        return new ModelAndView("index");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");

    }

    @RequestMapping("/joinMatch")
    public ModelAndView joinMatch() {
        return new ModelAndView("joinMatch");

    }

    @RequestMapping(value = "/joinMatch", method = {RequestMethod.GET })
    public ModelAndView createJoinMatchForm(@ModelAttribute("joinMatchForm") JoinMatchForm joinMatchForm){
        return new ModelAndView("joinMatch");
    }

    @RequestMapping(value = "/joinMatch", method = {RequestMethod.POST })
    public ModelAndView joinMatch(@Valid @ModelAttribute("joinMatchForm") final JoinMatchForm joinMatchForm,
                               final BindingResult errors) {
        if(errors.hasErrors()) {
            return createJoinMatchForm(joinMatchForm);
        }
        //final User u = us.create(userForm.getUsername(), "a", "b" );
        //return new ModelAndView("redirect:/userId=" + u.getUserId());
        return new ModelAndView("index");
    }

    //llevarlo a un base controller y podes tener el usuario loggeado evans
//    @ModelAttribute("loggedUser")
//    public PremiumUser logggedUser() {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(!authentication.isAuthenticated()) {
//            return null;
//        }
//        final PremiumUser user = us.findByUserName(authentication.getName());
//        return user;
//    }
}
