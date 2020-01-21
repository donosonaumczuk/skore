package ar.edu.itba.paw.exceptions;

import org.springframework.security.core.AuthenticationException;

//TODO: I think we must create an ExceptionMapper for this to look like any other (as an ApiErrorDto)
public class UnauthorizedExecption extends AuthenticationException {

    public UnauthorizedExecption(String s) {
        super(s);
    }
}
