package ar.edu.itba.paw.exceptions.alreadyexists;

public class LikeUserAlreadyExistException extends EntityAlreadyExistsException {

    private final static String ENTITY_NAME = "Like user";

    private LikeUserAlreadyExistException(final String username, final String usernameOfLiked) {
        super(ENTITY_NAME, usernameOfLiked + " from user " + username);
    }

    public static LikeUserAlreadyExistException ofUsernames(final String username, final String usernameOfLiked) {
        return new LikeUserAlreadyExistException(username, usernameOfLiked);
    }
}
