package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.Exceptions.GameNotFoundException;
import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.JoinMatchForm;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.MatchForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.Validators.ValidImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class UserController extends BaseController{
    private static final int USER_ROLE_ID = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService us;

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/create", method = {RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("registerForm") UserForm userForm){
        return new ModelAndView("createUser");
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                               final BindingResult errors, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        //evans
        if(errors.hasErrors()) {
            System.out.println("imageFile: " + userForm.getImage().getContentType() + "\n\n\n\n");
            return createForm(userForm);
        }
        final PremiumUser user = us.create(userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(),
                userForm.getUsername(), userForm.getCellphone(), userForm.getBirthday(), userForm.getCountry(),
                userForm.getState(), userForm.getCity(), userForm.getStreet(), 0, userForm.getPassword(),
                userForm.getImage());
        //us.addRole(user.getUserName(), USER_ROLE_ID); evans
        //return new ModelAndView("redirect:/userId=" + u.getUserId());
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ModelAndView loginForm(@RequestParam(value = "error", required = false) String error) {
        ModelAndView mav = new ModelAndView("login");
        if(error != null) {
            mav.addObject("error", "errorTest");
        }
        return mav;
    }

    @RequestMapping(value = "/profile/{username}", method = {RequestMethod.GET})
    public ModelAndView userProfile(@PathVariable("username") String username) {
        Optional<PremiumUser> u = us.findByUserName(username);
        if(!u.isPresent())
            return new ModelAndView("404UserNotFound").addObject("username", username);

        return new ModelAndView("userProfile").addObject("user", u.get());
    }

    @RequestMapping(value = "/confirm/**")
    public ModelAndView confirmAccount(HttpServletRequest request) {
        String path = request.getServletPath();
        us.confirmationPath(path);
        return new ModelAndView("index");
    }
//
//    @RequestMapping(value = "/joinMatch/*", method = {RequestMethod.GET })
//    public ModelAndView joinMatch(HttpServletRequest request){
//        String path = request.getServletPath().replace("/joinMatch/", "");
//        final int URL_DATE_LENGTH =12;
//        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;
//        if(path.length() < MIN_LENGTH) {
//            throw new GameNotFoundException("path '" + path + "' is too short to be formatted to a key");
//        }
//        String startTime = gameService.urlDateToKeyDate(path.substring(0, URL_DATE_LENGTH));
//        String teamName1 = path.substring(URL_DATE_LENGTH, path.length() - URL_DATE_LENGTH);
//        String finishTime = gameService.urlDateToKeyDate(path.substring(path.length() - URL_DATE_LENGTH));
////        System.out.println(startTime + "\n\n");
////        System.out.println(teamName1 + "\n\n");
////        System.out.println(finishTime + "\n\n");
//        PremiumUser user = loggedUser();
//        if(user == null) {
//            System.out
//            Object returned =  new ModelAndView("redirect:/joinMatchForm");
//        }
//        return new ModelAndView("index");
//    }
//
//    @RequestMapping(value = "/joinMatchForm", method = {RequestMethod.GET })
//    public ModelAndView createJoinMatchForm(@ModelAttribute("joinMatchForm") JoinMatchForm joinMatchForm){
//        return new ModelAndView("joinMatch");
//    }

//    @RequestMapping(value = "/joinMatchForm", method = {RequestMethod.POST })
//    public Object joinMatchForm(@Valid @ModelAttribute("joinMatchForm") final JoinMatchForm joinMatchForm,
//                               final BindingResult errors) {
//        System.out.println("llego a esta");
//        if(errors.hasErrors()) {
//            return createJoinMatchForm(joinMatchForm);
//        }
//        //final User u = us.create(userForm.getUsername(), "a", "b" );
//        //return new ModelAndView("redirect:/userId=" + u.getUserId());
//        return userService.create(joinMatchForm.getFirstName(), joinMatchForm.getLastName(),
//                joinMatchForm.getEmail());
//    }



    ///joinCompetitiveMatch/201810021226$1|1-6967621630201810021246
    @RequestMapping(value = "/joinCompetitiveMatch/*")
    public ModelAndView joinCompetitiveMatch(HttpServletRequest request) {
        PremiumUser user = loggedUser();
        if(user == null) {
            throw new UserNotFoundException("error in login");
        }
        String path = request.getServletPath().replace("/joinCompetitiveMatch/", "");
        //path = startTimenombreTeamfinishTime;
        final int URL_DATE_LENGTH =12;
        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;
        if(path.length() < MIN_LENGTH) {
            throw new GameNotFoundException("path '" + path + "' is too short to be formatted to a key");
        }

        String startTime = gameService.urlDateToKeyDate(path.substring(0, URL_DATE_LENGTH));
        String teamName1 = path.substring(URL_DATE_LENGTH, path.length() - URL_DATE_LENGTH);
        String finishTime = gameService.urlDateToKeyDate(path.substring(path.length() - URL_DATE_LENGTH));
        Game game = gameService.insertUserInGame(teamName1, startTime, finishTime, user.getUserId());


        return new ModelAndView("redirect:/match/" + path);
    }
}
