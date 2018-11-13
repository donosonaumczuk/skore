package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.Exceptions.*;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Game;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.Validators.ValidImageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class UserController extends BaseController{
    private static final int USER_ROLE_ID = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDetailsService skoreUserDetailsService;

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    @Autowired
    @Qualifier("gameServiceImpl")
    private GameService gameService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/create", method = {RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("registerForm") UserForm userForm) {
        return new ModelAndView("createUser");
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm,
                               final BindingResult errors, @RequestParam(value = "file", required = false)
                                       MultipartFile file) throws IOException {
        if(errors.hasErrors()) {
            return createForm(userForm);
        }

        MultipartFile image;

        if(userForm.getImage().getSize() == 0) {
            image = null;
        }
        else {
            image = userForm.getImage();
        }

        final PremiumUser user = premiumUserService.create(userForm.getFirstName(), userForm.getLastName(),
                userForm.getEmail(), userForm.getUsername(), userForm.getCellphone(), userForm.getBirthday(),
                userForm.getCountry(), userForm.getState(), userForm.getCity(), userForm.getStreet(),
                0, userForm.getPassword(), image);

        if(user == null) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "canNotCreateUser").addObject("attribute", userForm.getUsername());
            //throw new CannotCreateUserException("Can't create user with username:" + userForm.getUsername());
        }

        return new ModelAndView("sendingConfirmationAccountMail");
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
    public ModelAndView userProfile(@PathVariable String username) {
        Optional<PremiumUser> u = premiumUserService.findByUserName(username);

        if(!u.isPresent()) {
            return new ModelAndView("404UserNotFound").addObject("username", username);
        }

        return new ModelAndView("userProfile").addObject("user", u.get());
    }

    @RequestMapping(value = "/editInfo", method = {RequestMethod.GET})
    public ModelAndView editUserForm(@ModelAttribute("editUserForm") EditUserForm editUserForm) {
        Optional<PremiumUser> u = premiumUserService.findByUserName(loggedUser().getUserName());

        if(!u.isPresent()) {
            return new ModelAndView("404UserNotFound").addObject("username",
                    loggedUser().getUserName());//should never occur
        }

        return new ModelAndView("editUser").addObject("user", u.get())
                .addObject("username", loggedUser().getUserName());
    }

    @RequestMapping(value = "/editInfo", method = {RequestMethod.POST })
    public ModelAndView edituser(@Valid @ModelAttribute("editUserForm") final EditUserForm editUserForm,
                                 final BindingResult errors,
                                 @RequestParam(value = "file", required = false)
                                         MultipartFile file) throws IOException {

        if(errors.hasErrors()) {
            return editUserForm(editUserForm);
        }

        Optional<PremiumUser> foundUser = premiumUserService.findByUserName(loggedUser().getUserName());
        PremiumUser currentUser = foundUser.orElseThrow(() -> new UserNotFoundException("Can't find user with" +
                "username:" + loggedUser().getUserName()));//should never occur
        MultipartFile image;

        if(editUserForm.getImage().getSize() == 0) {
            image = null;
        }
        else {
            image = editUserForm.getImage();
        }

         DateTimeFormatter expectedFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
         currentUser = premiumUserService.updateUserInfo(editUserForm.getFirstName(), editUserForm.getLastName(),
                currentUser.getEmail(), currentUser.getUserName(), editUserForm.getCellphone(),
                currentUser.getBirthday().format(expectedFormat), currentUser.getHome().getCountry(),
                currentUser.getHome().getState(), currentUser.getHome().getCity(), currentUser.getHome().getStreet(),
                currentUser.getReputation(), currentUser.getPassword(), image, currentUser.getUserName());


        return new ModelAndView("userProfile").addObject("user", currentUser);
    }

    @RequestMapping(value = "/changePassword", method = {RequestMethod.GET})
    public ModelAndView modifyPasswordForm(@ModelAttribute("modifyPasswordForm") ModifyPasswordForm modifyPasswordForm) {

//        if(!isLogged()) {
//            return new ModelAndView(("404UserNotLogged"));
//        }


        return new ModelAndView("modifyPassword");
    }

    @RequestMapping(value = "/changePassword", method = {RequestMethod.POST })
    public ModelAndView modifyPassword(@Valid @ModelAttribute("modifyPasswordForm") final ModifyPasswordForm
                                                   modifyPasswordForm, final BindingResult errors) throws IOException {

        if(errors.hasErrors()) {
            return modifyPasswordForm(modifyPasswordForm);
        }

        Optional<PremiumUser> foundUser = premiumUserService.findByUserName(modifyPasswordForm.getUsername());
        PremiumUser currentUser = foundUser.orElseThrow(() -> new UserNotFoundException("Can't find user with" +
                "username:" + modifyPasswordForm.getUsername()));

        DateTimeFormatter expectedFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        currentUser = premiumUserService.changePassword(modifyPasswordForm.getNewPassword(), currentUser.getUserName());


        return new ModelAndView("userProfile").addObject("user", currentUser);
    }



    @RequestMapping(value = "/confirm/**")
    public ModelAndView confirmAccount(HttpServletRequest request) {
        String path = request.getServletPath();
        path = path.replace("/confirm/", "");
        int splitIndex = path.indexOf('&');
        String username = path.substring(0, splitIndex);
        Optional<PremiumUser> foundUser = premiumUserService.findByUserName(username);

        if(!foundUser.isPresent()) {
            return new ModelAndView("404UserNotFound").addObject("username", username);
        }

        PremiumUser user = premiumUserService.findByUserName(username).get();//should never be empty

        if(user.getEnabled()) {
            return new ModelAndView("genericPageWithMessage").addObject("message",
                    "userIsAlreadyConfirmed").addObject("attribute", username);
        }

        if(premiumUserService.confirmationPath(path)) {
            UserDetails userDetails = skoreUserDetailsService.loadUserByUsername (username);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    user.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("is logged:" + isLogged() + "\n");

            return new ModelAndView("userProfile").addObject("isLogged", true)
                    .addObject("user", user)
                    .addObject("loggedUser", user);
            // return new ModelAndView("accountConfirmed");
        }

        return new ModelAndView("genericPageWithMessage").addObject("message",
                "canNotValidateUser").addObject("attribute", "");
        //CannotValidateUserException("Can't validate user");
    }

    @RequestMapping(value = "/joinMatch/*", method = {RequestMethod.GET })
    public ModelAndView joinMatch(HttpServletRequest request) {
        String path = request.getServletPath().replace("/joinMatch/", "");
        PremiumUser user = loggedUser();

        if(user == null) {
            return new ModelAndView("redirect:/joinMatchForm/" + path);
        }

        return new ModelAndView("redirect:/joinCompetitiveMatch/" + path);
    }

    @RequestMapping(value = "/joinMatchForm/{data:.+}", method = {RequestMethod.GET })
    public ModelAndView createJoinMatchForm(@ModelAttribute("joinMatchForm") JoinMatchForm joinMatchForm,
                                            @PathVariable String data) {
        return new ModelAndView("joinMatch").addObject("data", data);
    }

    @RequestMapping(value = "/joinMatchForm/{data:.+}", method = {RequestMethod.POST })
    public ModelAndView joinMatchForm(@Valid @ModelAttribute("joinMatchForm") final JoinMatchForm joinMatchForm,
                                      final BindingResult errors, HttpServletRequest request, @PathVariable String data) {

        if(errors.hasErrors()) {
            return createJoinMatchForm(joinMatchForm, data);
        }

        final int URL_DATE_LENGTH = 12;
        final int MIN_LENGTH = URL_DATE_LENGTH * 2 + 1;

        if(data.length() < MIN_LENGTH) {
            return new ModelAndView("genericPageWithMessages").addObject("message",
                    "canNotFoundGame").addObject("attribute", "");
        }

        String startTime = gameService.urlDateToKeyDate(data.substring(0, URL_DATE_LENGTH));
        String teamName1 = data.substring(URL_DATE_LENGTH, data.length() - URL_DATE_LENGTH);
        String finishTime = gameService.urlDateToKeyDate(data.substring(data.length() - URL_DATE_LENGTH));


        final User u = userService.create(joinMatchForm.getFirstName(), joinMatchForm.getLastName(),
                joinMatchForm.getEmail());
        Game game = gameService.findByKey(teamName1, startTime, finishTime);

        if(!game.getType().contains("Friendly")) {
            return new ModelAndView("cantJoinCompetitive");
        }
        else {
            userService.sendConfirmMatchAssistance(u, game, data);
        }

        return new ModelAndView("confirmationMatchMailSend");
    }


    ///joinCompetitiveMatch/201810021226$1|1-6967621630201810021246
    @RequestMapping(value = "/joinCompetitiveMatch/*")
    public ModelAndView joinCompetitiveMatch(HttpServletRequest request) {
        PremiumUser user = loggedUser();

        if(user == null) {
            throw new UserNotFoundException("error in login");//should never occur
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
        Game game = gameService.findByKey(teamName1, startTime, finishTime);

        if(game == null) {
            return new ModelAndView("genericPageWithMessages").addObject("message",
                    "canNotFoundGame").addObject("attribute", "");
        }
        try {
            game = gameService.insertUserInGame(teamName1, startTime, finishTime, user.getUser().getUserId());
            LOGGER.trace("added to Match");
        } catch (TeamFullException e) {
            LOGGER.error("Team is already full");

            return new ModelAndView("teamFull");
        }

        return new ModelAndView("redirect:/match/" + path);
    }
}
