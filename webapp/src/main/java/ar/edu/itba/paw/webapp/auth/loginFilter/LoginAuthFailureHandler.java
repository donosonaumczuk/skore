package ar.edu.itba.paw.webapp.auth.loginFilter;

import ar.edu.itba.paw.Exceptions.AlreadyLoggedException;
import ar.edu.itba.paw.Exceptions.InvalidLoginException;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginAuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) {
        if (e instanceof AlreadyLoggedException) {
            LOGGER.warn("Attempt to log when is already log");
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
        else if (e instanceof InvalidLoginException) {
            LOGGER.info("Invalid login: {}", e.getCause().getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        else if (e instanceof BadCredentialsException) {
            LoginDto loginDto = (LoginDto) httpServletRequest.getAttribute("loginRequest");
            if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
                LOGGER.info("Invalid login JSON");
                httpServletResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            }
            else {
                LOGGER.info("Invalid login JSON: invalid credentials");
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }
}
