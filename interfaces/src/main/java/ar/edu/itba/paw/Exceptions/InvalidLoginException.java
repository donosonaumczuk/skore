package ar.edu.itba.paw.Exceptions;

import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class InvalidLoginException extends AuthenticationException {

    public InvalidLoginException(String s, IOException e) {
        super(s, e);
    }
}
