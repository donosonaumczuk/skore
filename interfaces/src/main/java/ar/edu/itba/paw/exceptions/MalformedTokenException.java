package ar.edu.itba.paw.exceptions;

import org.springframework.security.core.AuthenticationException;

public class MalformedTokenException extends AuthenticationException {

    public MalformedTokenException(String s) {
        super(s);
    }
}
