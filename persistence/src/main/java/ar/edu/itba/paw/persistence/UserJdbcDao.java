package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {
        private final JdbcTemplate jdbcTemplate;
        private final SimpleJdbcInsert jdbcInsert;

        private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new User(resultSet.getString("username"),resultSet.getInt("userid"));
            }
        };

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
        public User create(String username) {
            final Map<String, Object> args =  new HashMap<>();
            args.put("username", username);
            final Number userid = jdbcInsert.execute(args);

            return new User(username,userid.longValue());
        }
}
