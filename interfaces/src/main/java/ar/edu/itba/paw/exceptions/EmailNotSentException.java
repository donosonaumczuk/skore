package ar.edu.itba.paw.exceptions;

public class EmailNotSentException extends RuntimeException {

    public EmailNotSentException(String s) {
        super(s);
    }
}
