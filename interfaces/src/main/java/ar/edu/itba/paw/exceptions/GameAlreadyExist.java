package ar.edu.itba.paw.exceptions;

public class GameAlreadyExist extends RuntimeException {
    public GameAlreadyExist(String s) {
        super(s);
    }
}
