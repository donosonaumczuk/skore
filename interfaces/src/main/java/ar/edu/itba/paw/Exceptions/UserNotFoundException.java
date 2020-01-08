package ar.edu.itba.paw.Exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException (String message) {
        super(message);
    }
}
