package ar.edu.itba.paw.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BlacklistedTokenException extends AuthenticationException {

    public BlacklistedTokenException(String s) {
        super(s);
    }
}
