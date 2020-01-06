package ar.edu.itba.paw.webapp.auth.JasonWebToken;

import ar.edu.itba.paw.Exceptions.BlacklistedTokenException;
import ar.edu.itba.paw.Exceptions.MalformedTokenException;
import ar.edu.itba.paw.interfaces.JWTService;
import ar.edu.itba.paw.webapp.auth.SkoreUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTUserDetailsAuthProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private SkoreUserDetailsService skoreUserDetailsService;

    @Autowired
    private JWTService jwtService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWTUsernamePasswordAuthToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        if(!(usernamePasswordAuthenticationToken instanceof JWTUsernamePasswordAuthToken)) {
            throw new MalformedTokenException("It is not a JWTUsernamePasswordAuthToken: rejecting auth");
        } else if(jwtService
                .isInBlacklist(((JWTUsernamePasswordAuthToken)
                        usernamePasswordAuthenticationToken).getToken())) {
            throw new BlacklistedTokenException("Token is in blacklist: rejecting auth");
        }
    }

    @Override
    protected UserDetails retrieveUser(String s,
                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        JWTUsernamePasswordAuthToken jwtUsernamePasswordAuthToken =
                ((JWTUsernamePasswordAuthToken) usernamePasswordAuthenticationToken);
        String jwtToken = jwtUsernamePasswordAuthToken.getToken();

        Claims tokenClaims = jwtUtility.validateTokenString(jwtToken);
        String username = tokenClaims == null ? null : tokenClaims.getSubject();
        if (username == null) {
            throw new MalformedTokenException("Token username is not present");
        }
        return skoreUserDetailsService.loadUserByUsername(username);
    }
}
