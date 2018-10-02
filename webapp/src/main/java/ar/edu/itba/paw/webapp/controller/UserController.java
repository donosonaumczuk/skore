package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.UserDao;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController extends BaseController{
    private static final int USER_ROLE_ID = 0;

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
                userForm.getState(), userForm.getCity(), userForm.getStreet(), 0, new BCryptPasswordEncoder().encode(userForm.getPassword()));
        //us.addRole(user.getUserName(), USER_ROLE_ID);
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

    @RequestMapping(value = "/profile/{username}", method = {RequestMethod.GET})
    public ModelAndView userProfile(@PathVariable("username") String username) {
        Optional<PremiumUser> u = us.findByUserName(username);
        if(!u.isPresent())
            return new ModelAndView("404UserNotFound").addObject("username", username);

        return new ModelAndView("userProfile").addObject("user", u.get());
    }
}
