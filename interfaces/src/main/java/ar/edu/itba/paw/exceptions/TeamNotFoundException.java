package ar.edu.itba.paw.exceptions;

public class TeamNotFoundException extends EntityNotFoundException {

    public TeamNotFoundException(final String teamId) {
        super("Team", teamId);
    }
}
