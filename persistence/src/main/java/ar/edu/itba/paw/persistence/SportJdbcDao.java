package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Sport;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SportJdbcDao implements SportDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Sport> ROW_MAPPER = (resultSet, rowNum) ->
            new Sport(resultSet.getString("sportName"), resultSet.getInt("playerQuantity"),
                    resultSet.getString("displayName"));

    @Autowired
    public SportJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sports");
    }

    @Override
    public Optional<Sport> findByName(final String sportName) {
        final List<Sport> list = jdbcTemplate.query("SELECT * FROM sports WHERE sportName = ?", ROW_MAPPER, sportName);
        return list.stream().findFirst();
    }

    @Override
    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName) {
        final Map<String, Object> args =  new HashMap<>();
        final String sqlQuery = "SELECT * FROM sports where sportName = ?";
        args.put("sportName", sportName);
        args.put("playerQuantity", playerQuantity);
        args.put("displayName", displayName);
        jdbcInsert.execute(args);

        List<Sport> sportsList = jdbcTemplate.query(sqlQuery, ROW_MAPPER, sportName);
        return sportsList.stream().findFirst();

    }


    @Override
    public boolean remove(final String sportName) {
        final String sqlQuery = "DELETE FROM sports where sportName = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, sportName);
        return rowsDeleted > 0;
    }

    @Override
    public List<Sport> getAllSports() {
        return jdbcTemplate.query("SELECT * FROM sports;", ROW_MAPPER);

    }

//    @Override
//    public Optional<User> updateBasicUserInfo(final long userId, final String newFirstName,
//                                              final String newLastName, final String newEmail) {
//        final String sqlQuery = "UPDATE users SET firstName = ?, lastName = ?, email = ?" +
//                " WHERE userId = ?";
//        jdbcTemplate.update(sqlQuery, newFirstName, newLastName, newEmail, userId);
//        return findById(userId);
//    }


}

