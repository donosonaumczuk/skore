package ar.edu.itba.paw.webapp.auth.loginFilter;

import ar.edu.itba.paw.Exceptions.AlreadyLoggedException;
import ar.edu.itba.paw.Exceptions.InvalidLoginException;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        if(e instanceof AlreadyLoggedException) {
            LOGGER.warn("Attempt to log when is already log");
            httpServletResponse.setStatus(404);
        }
        else if(e instanceof InvalidLoginException) {
            LOGGER.info("Invalid login: {}", e.getCause().getMessage());
            httpServletResponse.setStatus(400);
        }
        else if(e instanceof BadCredentialsException) {
            LoginDto loginDto = (LoginDto) httpServletRequest.getAttribute("loginRequest");
            if(loginDto.getUsername() == null || loginDto.getPassword() == null) {
                LOGGER.info("Invalid login JSON");
                httpServletResponse.setStatus(422);//TODO check, maybe do more
            } else {
                LOGGER.info("Invalid login JSON: invalid credentials");
                httpServletResponse.setStatus(401);
            }
        }
    }
}