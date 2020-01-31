package ar.edu.itba.paw.exceptions;

public class GameAlreadyExist extends EntityAlreadyExistsException {

    private GameAlreadyExist(String s) {
        super(s);
    }

    public static GameAlreadyExist ofKey(final String entityName, final String entityId) {

    }
}
