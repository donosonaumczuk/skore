package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MatchForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;

import javax.validation.Valid;

@Controller
public class FrontController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        //mav.addObject("user", us.updateEmail(10000, "Agustinizag1@gmail.com"));
        mav.addObject("user", us.findById(1));

        return mav;
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        final ModelAndView mav = new ModelAndView("test");
        mav.addObject("greeting", "Agustin");
        return mav;
    }

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
        final User u = us.create(userForm.getUsername(), "a", "b" );
        return new ModelAndView("redirect:/userId=" + u.getUserId());
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.GET })
    public ModelAndView createMatchForm(@ModelAttribute("registerForm") MatchForm matchForm){
        return new ModelAndView("createMatch");
    }

    @RequestMapping(value = "/createMatch", method = {RequestMethod.POST })
    public ModelAndView createMatch(@Valid @ModelAttribute("registerForm") final MatchForm matchForm,
                                    final BindingResult errors){
        if(errors.hasErrors()) {
            return createMatchForm(matchForm);
        }
       // final User u = us.create(userForm.getUsername(), "a", "b" );
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");

    }
}
