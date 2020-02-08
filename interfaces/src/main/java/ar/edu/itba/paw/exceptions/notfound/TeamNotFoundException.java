package ar.edu.itba.paw.exceptions.notfound;

public class TeamNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Team";

    private TeamNotFoundException(final String teamId) {
        super(ENTITY_NAME, teamId);
    }

    public static TeamNotFoundException ofId(final String teamId) {
        return new TeamNotFoundException(teamId);
    }
}
