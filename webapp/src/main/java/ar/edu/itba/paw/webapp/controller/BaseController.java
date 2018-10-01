package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

public  abstract class BaseController {


    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService us;

    @ModelAttribute("isLogged")
    public Boolean isLogged() {
        if(SecurityContextHolder.getContext() == null) {
            return false;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()) {
            return false;
        }
        final Optional<PremiumUser> user = us.findByUserName(authentication.getName());
        if(!user.isPresent()) {
            return false;
        }
        return true;
    }

    @ModelAttribute("loggedUser")
    public PremiumUser loggedUser() {
        if(!isLogged()) {
            return null;
        }
        Optional<PremiumUser> user = us.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        return user.get();
    }


}
