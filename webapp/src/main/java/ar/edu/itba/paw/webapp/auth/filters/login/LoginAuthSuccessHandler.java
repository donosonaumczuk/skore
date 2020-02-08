package ar.edu.itba.paw.webapp.auth.filters.login;

import ar.edu.itba.paw.exceptions.notfound.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.auth.token.JWTUtility;
import ar.edu.itba.paw.webapp.dto.AuthDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthSuccessHandler.class);

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private PremiumUserService premiumUserService;

    @Autowired
    private SessionService sessionService;

    private static final String TOKEN_HEADER = "X-TOKEN";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        LOGGER.info("{} has been successfully authenticate", authentication.getName());

        PremiumUser premiumUser = premiumUserService.findByUserName(authentication.getName())
                .orElseThrow(()-> UserNotFoundException.ofUsername(authentication.getName())); // Should never happen

        httpServletResponse.addHeader(TOKEN_HEADER, jwtUtility.createToken(premiumUser));
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String username = premiumUser.getUserName();
        long userId = premiumUser.getUser().getUserId();
        boolean isAdmin = sessionService.isAdmin();
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), AuthDto.from(username, userId, isAdmin));
    }
}
