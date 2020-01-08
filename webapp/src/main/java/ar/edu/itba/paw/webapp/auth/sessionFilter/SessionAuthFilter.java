package ar.edu.itba.paw.webapp.auth.sessionFilter;

import ar.edu.itba.paw.exceptions.NoJWTFoundException;
import ar.edu.itba.paw.exceptions.UnauthorizedExecption;
import ar.edu.itba.paw.interfaces.JWTService;
import ar.edu.itba.paw.webapp.auth.JasonWebToken.JWTUsernamePasswordAuthToken;
import ar.edu.itba.paw.webapp.auth.JasonWebToken.JWTUtility;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

public class SessionAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAuthFilter.class);

    private static final String AUTH_HEADER      = "Authorization";
    private static final String AUTH_BEARER_TYPE = "Bearer ";
    private static final String DEFAULT_FILTER   = "/**";

    private static final RequestMatcher logOutEndpointMatcher
            = new AntPathRequestMatcher("/api/logout", HttpMethod.POST);

    private static final String ADMIN = "ROLE_ADMIN";

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RequestMatcher optionalAuthEndpointsMatcher;

    @Autowired
    private RequestMatcher adminAuthEndpointsMatcher;

    public SessionAuthFilter() {
        super(DEFAULT_FILTER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        Authentication auth;
        Optional<JWTUsernamePasswordAuthToken> token = getTokenFromRequest(httpServletRequest);
        if (!token.isPresent()) {
            if (optionalAuthEndpointsMatcher.matches(httpServletRequest)) {
                LOGGER.trace("Give anonymous access");
                auth = new AnonymousAuthenticationToken("SKORE_ANONYMOUS",
                        "ANONYMOUS",
                        Collections.singletonList(new SimpleGrantedAuthority("NONE")));
            } else {
                throw new NoJWTFoundException("No JWT found in request");
            }
        }
        else {
            LOGGER.trace("Give JWT access");
            auth = getAuthenticationManager().authenticate(token.get());
            if(adminAuthEndpointsMatcher.matches(httpServletRequest)
                    && !auth.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN))) {
                throw new UnauthorizedExecption("Is not admin");
            }
        }
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                        HttpServletResponse response, FilterChain chain,
                        Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        if (logOutEndpointMatcher.matches(request)) {
            LOGGER.trace("Going to logout user");
            String tokenString = getTokenFromRequest(request)
                    .orElseThrow(()->new NoJWTFoundException("No JWT found in request"))
                    .getToken();
            Claims tokenClaims = getClaimsFromRequest(request);
            LocalDateTime expiry = tokenClaims.getExpiration().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            jwtService.addBlacklist(tokenString, expiry);
        }
        chain.doFilter(request, response);
    }

    private Optional<JWTUsernamePasswordAuthToken> getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);
        JWTUsernamePasswordAuthToken ans = null;
        if (header != null && header.startsWith(AUTH_BEARER_TYPE)) {
            String authToken = header.substring(AUTH_BEARER_TYPE.length());
            ans = new JWTUsernamePasswordAuthToken(authToken);
        }
        return Optional.ofNullable(ans);
    }

    private Claims getClaimsFromRequest(HttpServletRequest request) {
        Optional<JWTUsernamePasswordAuthToken> token = getTokenFromRequest(request);
        Claims ans = null;
        if (token.isPresent()) {
            ans = jwtUtility.validateTokenString(token.get().getToken());
        }
        return ans;
    }
}
