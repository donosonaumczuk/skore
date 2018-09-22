package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PremiumUserJdbcDao implements PremiumUserDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final UserJdbcDao userDao;

    private final static RowMapper<PremiumUser> ROW_MAPPER = (resultSet, rowNum) ->
            new PremiumUser(resultSet.getString("firstName"), resultSet.getString("lastName"),
                    resultSet.getString("email"), resultSet.getInt("userid"),
                    resultSet.getString("userName"), resultSet.getString("cellphone"),
                    resultSet.getObject("birthday", LocalDate.class), new Place(
                            resultSet.getString("country"),resultSet.getString("state"),
                            resultSet.getString("city"), resultSet.getString("street")),
                    resultSet.getInt("reputation"), resultSet.getString("password"));

    @Autowired
    public PremiumUserJdbcDao(final DataSource dataSource, UserJdbcDao userDao) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("accounts");
        this.userDao = userDao;
    }

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        final List<PremiumUser> list = jdbcTemplate.query("SELECT * FROM accounts natural join users WHERE " +
                "userName = ?", ROW_MAPPER, userName);

        return list.stream().findFirst();
    }

    @Override
    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password) {
        User user = userDao.create(firstName, lastName, email).get();
        final Map<String, Object> args =  new HashMap<>();

        args.put("userId", user.getUserId());
        args.put("userName", userName);
        args.put("cellphone", cellphone);
        args.put("birthday", birthday);
        args.put("country", country);
        args.put("state", state);
        args.put("city", city);
        args.put("street", street);
        args.put("reputation", reputation);
        args.put("password", password);

        jdbcInsert.execute(args);
        return findByUserName(userName);
    }

    public boolean remove(final String userName) {
        final String sqlQuery = "DELETE FROM accounts where userName = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, userName);
        return rowsDeleted > 0;
    }

    public Optional<PremiumUser> updateUserInfo(final String newUserName, final String newCellphone,
                                                final String newBirthday, final String newCountry,
                                                final String newState, final String newCity,
                                                final String newStreet, final int newReputation,
                                                final String newPassword) {
        final String sqlQuery = "UPDATE accounts SET userName = ?, cellphone = ?, birthday = ?," +
                " country = ?, state = ?, city = ?, street = ?, reputation = ?, password = ? " +
                "WHERE userName = ?";
        jdbcTemplate.update(sqlQuery, newUserName , newCellphone, newBirthday, newCountry, newState,
                newCity, newStreet);

        return findByUserName(newUserName);
    }

}
