package ar.edu.itba.paw.services;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException (String message) {
        super(message);
    }
}
