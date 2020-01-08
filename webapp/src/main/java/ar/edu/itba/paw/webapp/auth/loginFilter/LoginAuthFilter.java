package ar.edu.itba.paw.webapp.auth.loginFilter;

import ar.edu.itba.paw.Exceptions.AlreadyLoggedException;
import ar.edu.itba.paw.Exceptions.InvalidLoginException;
import ar.edu.itba.paw.interfaces.SessionService;
import ar.edu.itba.paw.webapp.dto.LoginDto;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthFilter.class);

    @Autowired
    private SessionService sessionService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                            HttpServletResponse response) throws AuthenticationException {
        LOGGER.trace("Attempt to auth: {}", sessionService.getUserName());
        if (sessionService.isLogged()) {
            throw new AlreadyLoggedException(sessionService.getUserName() + " is already logged");
        }
        LoginDto loginDto;
        try {
            loginDto = getLoginDtoFromRequest(request);
            request.setAttribute("loginRequest", loginDto); //for more detail
        } catch (IOException e) {
            throw new InvalidLoginException("Error Processing login", e);
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword());
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    private LoginDto getLoginDtoFromRequest(HttpServletRequest request) throws IOException {
        String json = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, LoginDto.class);
    }
}
