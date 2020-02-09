package ar.edu.itba.paw.exceptions;

public class LackOfPermissionsException extends RuntimeException {

    public LackOfPermissionsException(String s) {
        super(s);
    }
}
