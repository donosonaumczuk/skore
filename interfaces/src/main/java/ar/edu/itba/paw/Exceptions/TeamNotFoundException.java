package ar.edu.itba.paw.Exceptions;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException (String message) {
        super(message);
    }
}
