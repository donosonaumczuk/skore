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

import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private PremiumUserService premiumUserService;

    private PremiumUser currentPremiumUser;

    private static String ADMIN = "ROLE_ADMIN";
    private static int ADMIN_ID = 1;

    @Override
    public boolean isLogged() {
        Authentication auth = getAuth();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public Optional<PremiumUser> getLoggedUser() {
        PremiumUser ans;
        String username = getUserName();
        if (username == null) {
            ans = null;
        }
        else {
            if (currentPremiumUser == null || !currentPremiumUser.getUserName().equals(username)) {
                currentPremiumUser = premiumUserService.findByUserName(username).orElse(null);
            }
            ans = currentPremiumUser;
        }

        return Optional.ofNullable(ans);
    }

    @Override
    public boolean isAdmin() {
        if (!isLogged()) {
            return false;
        }
        else {
            Role adminRole = new Role(ADMIN, ADMIN_ID);
            return getLoggedUser().get().getRoles().contains(adminRole);
        }
    }

    @Override
    public String getUserName() {
        final Authentication authentication = getAuth();
        if (authentication == null || !isLogged()) {
            return null;
        }
        return authentication.getName();
    }

    private Authentication getAuth() {
        Authentication auth = null;
        SecurityContext secCont = SecurityContextHolder.getContext();
        if (secCont != null) {
            auth = secCont.getAuthentication();
        }
        return auth;
    }
}