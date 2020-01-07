package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.JWTDao;
import ar.edu.itba.paw.models.JWT;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JWTHibernateDao implements JWTDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean isInBlacklist(String jwtoken) {
        return findByToken(jwtoken).isPresent();
    }

    @Override
    public JWT addBlacklist(String jwtoken, LocalDateTime expiry) {
        final JWT jwt = new JWT(jwtoken, expiry);
        em.persist(jwt);
        return jwt;
    }

    private Optional<JWT> findByToken(String jwtoken) {
        final TypedQuery<JWT> query = em.createQuery("FROM JWT WHERE token = :token",
                                    JWT.class);
        query.setParameter("token", jwtoken);
        final List<JWT> list = query.getResultList();
        return list.stream().findFirst();
    }
}
