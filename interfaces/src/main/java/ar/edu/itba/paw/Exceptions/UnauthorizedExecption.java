package ar.edu.itba.paw.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedExecption extends AuthenticationException {

    public UnauthorizedExecption(String s) {
        super(s);
    }
}
