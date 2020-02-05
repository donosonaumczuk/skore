package ar.edu.itba.paw.webapp.auth.token;

import ar.edu.itba.paw.models.PremiumUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class JWTUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtility.class);

    private final String secret;
    private final long maxValidTime;
    private final SecureRandom secureRandom;

    @Autowired
    public JWTUtility(Environment environment) {
        this.secret = environment.getProperty("token.secret");
        this.maxValidTime = environment.getProperty("token.time", Integer.class);
        this.secureRandom = new SecureRandom();
    }

    public String createToken(PremiumUser premiumUser) {
        Claims claims = Jwts.claims().setSubject(premiumUser.getUserName());
        Date timeNow = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("salt", secureRandom.nextLong())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(timeNow)
                .setExpiration(new Date(timeNow.getTime() + maxValidTime))
                .compact();
    }

    public Claims validateTokenString(String tokenString) {
        Claims claims;
        try {
            Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
            claims = (Claims) token.getBody();
            Header header = token.getHeader();
            Date timeNow = new Date();

            if(claims.getIssuedAt() == null || claims.getIssuedAt().after(timeNow)) {
                throw new JwtException("Invalid issued at date");
            }
            if(claims.getExpiration() == null || claims.getExpiration().before(timeNow)) {
                throw new JwtException("Invalid expiration date");
            }
            if(header.get("salt") == null) {
                throw new JwtException("Missing salt in Jason Web Token");
            }

        } catch (ClassCastException | JwtException e) {
            LOGGER.warn("Error validating Jason Web Token: {}", e.getMessage());
            claims = null;
        }
        return claims;
    }
}
