package ar.edu.itba.paw.exceptions;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException (String message) {
        super(message);
    }
}
