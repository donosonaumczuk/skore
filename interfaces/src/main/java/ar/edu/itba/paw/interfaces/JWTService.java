package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.JWT;

import java.time.LocalDateTime;
import java.util.List;

public interface JWTService {

    boolean isInBlacklist(String jwtoken);

    JWT addBlacklist(String jwtoken, LocalDateTime expiry);

    void delete(JWT jwt);

    List<JWT> getAll();
}
