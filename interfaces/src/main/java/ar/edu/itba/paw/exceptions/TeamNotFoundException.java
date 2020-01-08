package ar.edu.itba.paw.exceptions;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException (String message) {
        super(message);
    }
}
