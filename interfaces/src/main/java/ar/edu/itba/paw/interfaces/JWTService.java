package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.JWT;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface JWTService {
    boolean isInBlacklist(String jwtoken);

    @Transactional
    JWT addBlacklist(String jwtoken, LocalDateTime expiry);

    @Transactional
    void delete(JWT jwt);

    @Transactional
    List<JWT> getAll();
}
