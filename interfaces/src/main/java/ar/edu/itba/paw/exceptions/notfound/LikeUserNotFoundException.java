package ar.edu.itba.paw.exceptions.notfound;

public class LikeUserNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Like user";

    private LikeUserNotFoundException(final String username, final String usernameOfLiked) {
        super(ENTITY_NAME, usernameOfLiked + " from user " + username);
    }

    public static LikeUserNotFoundException ofUsernames(final String username, final String usernameOfLiked) {
        return new LikeUserNotFoundException(username, usernameOfLiked);
    }
}
