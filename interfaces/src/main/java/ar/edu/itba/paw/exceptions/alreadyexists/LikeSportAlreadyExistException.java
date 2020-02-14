package ar.edu.itba.paw.exceptions.alreadyexists;

public class LikeSportAlreadyExistException extends EntityAlreadyExistsException {

    private final static String ENTITY_NAME = "Like sport";

    private LikeSportAlreadyExistException(final String username, final String sportNameOfLiked) {
        super(ENTITY_NAME, username + "|" + sportNameOfLiked);
    }

    public static LikeSportAlreadyExistException ofUsernameAndSportName(final String username, final String sportNameOfLiked) {
        return new LikeSportAlreadyExistException(username, sportNameOfLiked);
    }
}