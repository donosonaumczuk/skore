package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.JWT;

import java.time.LocalDateTime;

public interface JWTDao {
    boolean isInBlacklist(String jwtoken);

    JWT addBlacklist(String jwtoken, LocalDateTime expiry);
}
