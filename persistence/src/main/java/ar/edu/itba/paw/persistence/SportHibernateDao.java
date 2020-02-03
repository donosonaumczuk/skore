package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.SportSort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class SportHibernateDao implements SportDao {

    @PersistenceContext
    private EntityManager em;

    private static final String QUERY_START           = "SELECT s FROM Sport as s WHERE s.sportName = s.sportName";
    private static final String QUERY_SPORT_NAME      = "s.sportName";
    private static final String QUERY_PLAYER_QUANTITY = "s.playerQuantity";
    private static final String EQUALS                = "=";
    private static final String LESS_THAN             = "<";
    private static final String GREATER_THAN          = ">";
    private static final String SPORT_NAME            = "sportName";
    private static final String MIN_QUANTITY          = "minQuantity";
    private static final String MAX_QUANTITY          = "maxQuantity";

    @Override
    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName,
                                  final byte[] file) {
        if(findByName(sportName).isPresent()) {
            return Optional.empty();
        }

        final Sport newSport = new Sport(sportName, playerQuantity, displayName, file);
        em.persist(newSport);
        return Optional.of(newSport);
    }

    @Override
    public Optional<Sport> modifySport(final String sportName, final String displayName, final Integer playerQuantity,
                                       final byte[] file) {
        Sport sport = em.find(Sport.class, sportName);

        if(sport == null) {
            return Optional.empty();
        }

        if(displayName != null) {
            sport.setDisplayName(displayName);
        }

        if(playerQuantity != null) {
            sport.setQuantity(playerQuantity);
        }

        if(file != null) {
            sport.setImage(file);
        }

        em.merge(sport);
        return Optional.of(sport);
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
    public List<Sport> findSports(final List<String> sportNames, final Integer minQuantity,
                                  final Integer maxQuantity, final SportSort sort) {
        DaoHelper daoHelper = new DaoHelper(QUERY_START);
        daoHelper.addFilter(QUERY_SPORT_NAME, EQUALS, SPORT_NAME, sportNames);
        daoHelper.addFilter(QUERY_PLAYER_QUANTITY, LESS_THAN, MIN_QUANTITY, minQuantity);
        daoHelper.addFilter(QUERY_PLAYER_QUANTITY, GREATER_THAN, MAX_QUANTITY, maxQuantity);
        final TypedQuery<Sport> query = em.createQuery(daoHelper.getQuery() +
                (sort != null ? sort.toQuery() : ""), Sport.class);
        List<String> valueName = daoHelper.getValueNames();
        List<Object> values    = daoHelper.getValues();

        for(int i = 0; i < valueName.size(); i++) {
            query.setParameter(valueName.get(i), values.get(i));
        }
        return query.getResultList();
    }

    @Override
    public Optional<byte[]> readImage(final String sportName) {
        Optional<Sport> sport = findByName(sportName);
        if(sport.isPresent() && sport.get().getImage() != null) {
            return Optional.of(sport.get().getImage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sport> findByName(final String sportName) {
            Sport sport = em.find(Sport.class, sportName);
            return (sport == null)?Optional.empty():Optional.of(sport);
    }
}
