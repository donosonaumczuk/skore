package ar.edu.itba.paw.exceptions;

public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException (String message) {
        super(message);
    }
}
