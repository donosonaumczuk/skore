package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private PremiumUserService us;

    private PremiumUser current;

    @Override
    public boolean isLogged() {
        Authentication auth = getAuth();
        return auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public PremiumUser loggedUser() {
        final Authentication authentication = getAuth();
        if(authentication == null || !isLogged()) {
            return null;
        }
        String username = authentication.getName();

        if(current == null || !current.getUserName().equals(username)) {
            current = us.findByUserName(username).orElse(null);
        }

        return current;
    }

    @Override
    public boolean isAdmin() {
        if(!isLogged()) {
            return false;
        }
        else {
            Role adminRole = new Role("ROLE_ADMIN", 1);
            return loggedUser().getRoles().contains(adminRole);
        }
    }

    private Authentication getAuth() {
        Authentication auth = null;
        SecurityContext secCont = SecurityContextHolder.getContext();
        if(secCont != null) {
            auth = secCont.getAuthentication();
        }
        return auth;
    }
}
