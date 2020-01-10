package ar.edu.itba.paw.webapp.auth.token;

import ar.edu.itba.paw.interfaces.JWTService;
import ar.edu.itba.paw.models.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

public class JWTBlacklistCleaner {

    private final Logger LOGGER = LoggerFactory.getLogger(JWTBlacklistCleaner.class);

    private final int ONE_DAY = 24 * 3600 * 1000;

    @Autowired
    private JWTService jwtservice;

    @Scheduled(fixedDelay = 2 * ONE_DAY, initialDelay = 0) //TODO calibrate
    public void clean() {
        LOGGER.info("Going to clean blacklist");
        LocalDateTime now = LocalDateTime.now();
        List<JWT> jwts = jwtservice.getAll();
        jwts.stream().filter((jwt)->jwt.getExpiry() != null && jwt.getExpiry().isBefore(now))
                .forEach((jwt)-> jwtservice.delete(jwt));
        LOGGER.info("Blacklist is clean");
    }
}
