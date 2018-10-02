package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
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

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView index() {
       System.out.println( formatDate("12/11/2018 01:00:00") + "\n\n\n\n");
        final ModelAndView mav = new ModelAndView("index");
        return mav;
    }
    private static String formatDate(String date) {
        String month = "" + date.charAt(0) + date.charAt(1);
        String day = "" + date.charAt(3) + date.charAt(4);
        String year = "" + date.charAt(6) + date.charAt(7) + date.charAt(8) + date.charAt(9);
        String hour ="" + date.charAt(11) + date.charAt(12);
        String min ="" + date.charAt(14) + date.charAt(15);
        String sec ="" + date.charAt(17) + date.charAt(18);
        String formattedDate = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
        LOGGER.trace("birthday date of user formatted to: {}", formattedDate);
        return formattedDate;

    }
}
