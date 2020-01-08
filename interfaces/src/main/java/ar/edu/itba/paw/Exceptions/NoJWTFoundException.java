package ar.edu.itba.paw.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class NoJWTFoundException extends AuthenticationException {

    public NoJWTFoundException(String s) {
        super(s);
    }
}
