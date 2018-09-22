package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
@Controller
public class FrontController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(1));
        return mav;
    }
    @RequestMapping("/test")
    public ModelAndView test() {
        final ModelAndView mav = new ModelAndView("test");
        mav.addObject("greeting", "Agustin");
        return mav;
    }

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "name", required = true)final String firstName){
        final User u = us.create(firstName, "a", "b" );
        return new ModelAndView("redirect:/userId=" + u.getUserId());
    }

    @RequestMapping("/createMatch")
    public ModelAndView createMatch(){
        return new ModelAndView("createMatch");
    }
}
