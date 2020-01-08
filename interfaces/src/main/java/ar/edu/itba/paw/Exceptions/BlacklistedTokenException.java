package ar.edu.itba.paw.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class BlacklistedTokenException extends AuthenticationException {

    public BlacklistedTokenException(String s) {
        super(s);
    }
}
