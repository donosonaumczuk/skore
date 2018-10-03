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
import org.springframework.web.servlet.ModelAndView;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView("index");
        return mav;

    }



}
