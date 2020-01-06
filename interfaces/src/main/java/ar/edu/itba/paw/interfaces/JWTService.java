package ar.edu.itba.paw.interfaces;

public interface JWTService {
    boolean isInBlacklist(String jwtoken);
}
