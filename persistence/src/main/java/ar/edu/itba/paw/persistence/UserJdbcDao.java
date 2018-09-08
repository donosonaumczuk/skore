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
                //new User(resultSet.getString("username"),resultSet.getInt("userid"));

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
            final String sqlQuery ="SELECT * FROM users where firstname=? and" +
                    " lastname=? and email=?";
            args.put("firstName", firstName);
            args.put("lastName", lastName);
            args.put("email", email);
            jdbcInsert.execute(args);
            List<User> userList = jdbcTemplate.query(sqlQuery, ROW_MAPPER, firstName, lastName, email);
            return userList.stream().findFirst();
            //return new User(username, userid.longValue());
        }

        @Override
        public Optional<User> findByFirstName(final String firstName) {
            return Optional.empty();
        }
}

