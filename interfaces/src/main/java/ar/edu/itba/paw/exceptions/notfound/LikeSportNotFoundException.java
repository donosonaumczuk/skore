package ar.edu.itba.paw.exceptions.notfound;

public class LikeSportNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Like sport";

    private LikeSportNotFoundException(final String username, final String sportNameOfLiked) {
        super(ENTITY_NAME, sportNameOfLiked + " from user " + username);
    }

    public static LikeSportNotFoundException ofUsernameAndSportName(final String username, final String sportNameOfLiked) {
        return new LikeSportNotFoundException(username, sportNameOfLiked);
    }
}