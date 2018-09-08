package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
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
public class UserJdbcDao implements UserDao {
        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert jdbcInsert;

        private final static RowMapper<User> ROW_MAPPER = (resultSet, rowNum) ->
                new User(resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("email"), resultSet.getInt("userid"));

        @Autowired
        public UserJdbcDao(final DataSource dataSource) {
            jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("users")
                    .usingGeneratedKeyColumns("userid");
        }

        @Override
        public Optional<User> findById(final long id) {
            final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?",ROW_MAPPER,id);

            return list.stream().findFirst();
        }

        @Override
        public Optional<User> create(final String firstName, final String lastName, final String email) {
            final Map<String, Object> args =  new HashMap<>();
            final String sqlQuery = "SELECT * FROM users where firstname = ? and " +
                    "lastname = ? and email = ?";
            args.put("firstName", firstName);
            args.put("lastName", lastName);
            args.put("email", email);
            jdbcInsert.execute(args);

            //Should be with these three statements
            //Number userId = jdbcInsert.executeAndReturnKey(args); should be with this statement
            //final String sqlQuery = "SELECT * FROM users where userid = ?";
            // List<User> userList = jdbcTemplate.query(sqlQuery, ROW_MAPPER, userid);

            List<User> userList = jdbcTemplate.query(sqlQuery, ROW_MAPPER, firstName, lastName, email);
            return userList.stream().findFirst();


        }

        @Override
        public Optional<User> findByFirstName(final String firstName) {
            return Optional.empty();
        }

        @Override
        public boolean remove(final long userId) {
            final String sqlQuery = "DELETE FROM users where userid = ?";
            int rowsDeleted = jdbcTemplate.update(sqlQuery, userId);
            return rowsDeleted > 0;
        }
}

