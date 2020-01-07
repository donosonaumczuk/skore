package ar.edu.itba.paw.webapp.auth.loginFilter;

import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.webapp.auth.JasonWebToken.JWTUtility;
import ar.edu.itba.paw.webapp.dto.PremiumUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {
        LOGGER.info("{} has been successfully authenticate", authentication.getName());

        PremiumUser premiumUser = premiumUserService.findByUserName(authentication.getName())
                .orElseThrow(()->new UserNotFoundException("Login user does not exit")); //This exception should never happen

        httpServletResponse.addHeader("X-TOKEN", jwtUtility.createToken(premiumUser));
        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType(ContentType.APPLICATION_JSON.toString());

        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(),
                new PremiumUserDto(premiumUser));
    }
}
