package ar.edu.itba.paw.webapp.auth.JasonWebToken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class JWTUsernamePasswordAuthToken extends UsernamePasswordAuthenticationToken {
    private String JWTtoken;


    public JWTUsernamePasswordAuthToken(String JWTtoken) {
        super(null, null);
        this.JWTtoken = JWTtoken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public String getToken() {
        return JWTtoken;
    }
}
