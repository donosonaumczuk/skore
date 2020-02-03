package ar.edu.itba.paw.exceptions.notfound;

public class TeamNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Team";

    public TeamNotFoundException(final String teamId) {
        super(ENTITY_NAME, teamId);
    }
}
