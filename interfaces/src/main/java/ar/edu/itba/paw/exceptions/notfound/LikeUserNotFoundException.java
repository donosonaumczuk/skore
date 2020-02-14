package ar.edu.itba.paw.exceptions.notfound;

public class LikeUserNotFoundException extends EntityNotFoundException {

    private final static String ENTITY_NAME = "Like user";

    private LikeUserNotFoundException(final String username, final String usernameOfLiked) {
        super(ENTITY_NAME, username + "|" + usernameOfLiked);
    }

    public static LikeUserNotFoundException ofUsernames(final String username, final String usernameOfLiked) {
        return new LikeUserNotFoundException(username, usernameOfLiked);
    }
}
