package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Role;
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
public class RoleJdbcDao implements RoleDao {
   private final JdbcTemplate jdbcTemplate;
   private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Role> ROW_MAPPER = (resultSet, rowNum) ->
            new Role(resultSet.getString("roleName"), resultSet.getInt("roleId"));

    @Autowired
    public RoleJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("roles");
    }

    @Override
    public Optional<Role> findRoleById(final int roleId) {
        final List<Role> list = jdbcTemplate.query("SELECT * FROM roles WHERE roleId = ?", ROW_MAPPER, roleId);

        return list.stream().findFirst();
    }

    @Override
    public Optional<Role> create(final String roleName, final int roleId) {
        final Map<String, Object> args =  new HashMap<>();
//        final String sqlQuery = "SELECT * FROM roles where roleName = ? and " +
//                "lastName = ? and email = ?";
        args.put("roleName", roleName);
        args.put("roleId", roleId);
        jdbcInsert.execute(args);

        //Should be with these three statements evans
        //Number userId = jdbcInsert.executeAndReturnKey(args); should be with this statement
        //return new User(firstName, lastName, email, userId.longValue());
        return findRoleById(roleId);

    }

    @Override
    public boolean remove(final int roleId) {
        final String sqlQuery = "DELETE FROM roles where roleId = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, roleId);
        return rowsDeleted > 0;
    }

}
