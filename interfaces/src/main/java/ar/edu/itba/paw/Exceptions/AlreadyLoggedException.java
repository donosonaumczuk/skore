package ar.edu.itba.paw.Exceptions;


import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedException extends AuthenticationException {
    public AlreadyLoggedException(String s) {
        super(s);
    }
}
