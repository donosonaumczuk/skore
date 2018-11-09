package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.models.Sport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class SportHibernateDao implements SportDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName,
                                  final MultipartFile file) throws IOException {
        final Sport newSport = new Sport(sportName, playerQuantity, displayName, ((file==null)?null:file.getBytes()));
        em.persist(newSport);
        return Optional.of(newSport);
    }

    @Override
    public boolean remove(final String sportName) {
        Optional<Sport> sport = findByName(sportName);
        boolean ans = false;
        if(sport.isPresent()) {
            em.remove(sport.get());
            ans = true;
        }
        return ans;
    }

    @Override
    public List<Sport> getAllSports() {
        final TypedQuery<Sport> query = em.createQuery("FROM Sport", Sport.class);
        final List<Sport> list = query.getResultList();
        return list;
    }

    @Override
    public Optional<byte[]> readImage(final String sportName) {
        Optional<Sport> sport = findByName(sportName);
        return sport.isPresent()?Optional.of(sport.get().getImage()):Optional.empty();
    }

    @Override
    public Optional<Sport> findByName(final String sportName) {
            Sport sport = em.find(Sport.class, sportName);
            return (sport == null)?Optional.empty():Optional.of(sport);
    }
}
