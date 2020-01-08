package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "blacklist")
public class JWT {

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "jwtseq", sequenceName = "jwtidseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jwtseq")
    private long id;

    @Column(name = "token", length = 500, nullable = false)
    private String token;

    @Column(name = "expiry", nullable = false)
    private LocalDateTime expiry;

    /*package*/ JWT() {
        //For Hibernate
    }

    public JWT(String tokenString, LocalDateTime expiry) {
        this.token = tokenString;
        this.expiry = expiry;
    }

    public JWT(String tokenString, LocalDateTime expiry, int id) {
        this(tokenString, expiry);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !object.getClass().equals(getClass())) {
            return false;
        }

        JWT jwt = (JWT) object;
        return getId() == jwt.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
