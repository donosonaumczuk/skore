package ar.edu.itba.paw.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class MalformedTokenException extends AuthenticationException {
    public MalformedTokenException(String s) {
        super(s);
    }
}
