package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PremiumUserDao;
import ar.edu.itba.paw.models.Place;
import ar.edu.itba.paw.models.PremiumUser;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class PremiumUserJdbcDao implements PremiumUserDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertUserRoles;
    private final UserJdbcDao userDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUserJdbcDao.class);


    private static final int USER_ROLE_ID = 0;
    private static final int ADMIN_ROLE_ID = 1;

    private static final boolean USER_DISABLED = false;
    private static final boolean USER_ENABLED = true;



    private final static RowMapper<PremiumUser> ROW_MAPPER = (resultSet, rowNum) ->
            new PremiumUser(resultSet.getString("firstName"), resultSet.getString("lastName"),
                    resultSet.getString("email"), resultSet.getInt("userid"),
                    resultSet.getString("userName"), resultSet.getString("cellphone"),
                    resultSet.getDate("birthday").toLocalDate(), new Place(
                            resultSet.getString("country"),resultSet.getString("state"),
                            resultSet.getString("city"), resultSet.getString("street")),
                    resultSet.getInt("reputation"), resultSet.getString("password"),
                    resultSet.getString("code"));

    private final static RowMapper<Role> ROLE_ROW_MAPPER = (resultSet, rowNum) ->
            new Role(resultSet.getString("roleName"), resultSet.getInt("roleId"));

    private final static RowMapper<byte[]> IMAGE_MAPPER = (resultSet, rowNum) ->
            resultSet.getBytes("image");

    @Autowired
    public PremiumUserJdbcDao(final DataSource dataSource, UserJdbcDao userDao) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("accounts");
        jdbcInsertUserRoles = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("userRoles");
        this.userDao = userDao;
    }

    @Override
    public Optional<PremiumUser> findByUserName(final String userName) {
        final List<PremiumUser> list = jdbcTemplate.query("SELECT * FROM accounts natural join users WHERE " +
                "userName = ?", ROW_MAPPER, userName);

        Optional<PremiumUser> user = list.stream().findFirst();
        if(user.isPresent()) {
            user.get().setRoles(getRoles(userName));
        }
        LOGGER.trace("user with username:{} found", userName);
        return user;
    }

    @Override
    public Optional<PremiumUser> create(final String firstName, final String lastName,
                                        final String email, final String userName,
                                        final String cellphone, final String birthday,
                                        final String country, final String state, final String city,
                                        final String street, final int reputation, final String password,
                                        final MultipartFile file) throws IOException{
        User user = userDao.create(firstName, lastName, email).get();
        final Map<String, Object> args =  new HashMap<>();
        final String code = new BCryptPasswordEncoder().encode(userName + email + LocalDateTime.now());
        args.put("userId", user.getUserId());
        args.put("userName", userName);
        args.put("cellphone", cellphone);
        args.put("birthday", birthday);
        args.put("country", country);
        args.put("state", state);
        args.put("city", city);
        args.put("street", street);
        args.put("reputation", reputation);
        args.put("email", email);
        args.put("password", password);
        args.put("enabled", USER_DISABLED);
        args.put("code", code);
        args.put("image", file.getBytes());

        if(jdbcInsert.execute(args) == 1) {
            LOGGER.trace("user with username: {} created", userName);
        }
        else {
            return Optional.empty();
        }
        if(!addRole(userName, USER_ROLE_ID)) {
            return Optional.empty();
        }
        LOGGER.trace("ROLE_USER set to user with username: {}", userName);

        return findByUserName(userName);
    }

    @Override
    public boolean remove(final String userName) {
        final String sqlQuery = "DELETE FROM accounts where userName = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, userName);
        return rowsDeleted > 0;
    }

    @Override
    public Optional<PremiumUser> updateUserInfo(final String newFirstName, final String newLastName,
                                                final String newEmail,final String newUserName,
                                                final String newCellphone, final String newBirthday,
                                                final String newCountry, final String newState,
                                                final String newCity, final String newStreet,
                                                final int newReputation, final String newPassword,
                                                final String oldUserName) {
        Optional<PremiumUser> currentUser = findByUserName(oldUserName);
        if(currentUser.isPresent()) {
            userDao.updateBasicUserInfo(currentUser.get().getUserId(), newFirstName, newLastName, newEmail);
            final String sqlQuery = "UPDATE accounts SET userName = ?, email = ?, cellphone = ?, birthday = ?," +
                    " country = ?, state = ?, city = ?, street = ?, reputation = ?, password = ? " +
                    "WHERE userName = ?";
            jdbcTemplate.update(sqlQuery, newUserName, newEmail, newCellphone, newBirthday, newCountry, newState,
                    newCity, newStreet, newReputation, newPassword, oldUserName);
            return findByUserName(newUserName);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> readImage(final String userName) {
         return jdbcTemplate.query("SELECT * FROM accounts WHERE userName = ?;", IMAGE_MAPPER,userName).stream().findFirst();
    }

    @Override
    public Optional<PremiumUser> findByEmail(final String email) {
        final List<PremiumUser> list = jdbcTemplate.query("SELECT * FROM accounts natural join users WHERE " +
                "email = ?", ROW_MAPPER, email);
        Optional<PremiumUser> user = list.stream().findFirst();
        if(user.isPresent()) {
            user.get().setRoles(getRoles(user.get().getUserName()));
        }
        LOGGER.trace("user with email:{} found", email);
        return user;
    }

    @Override
    public boolean addRole(final String username, final int roleId) {
        final Map<String, Object> args =  new HashMap<>();
        args.put("username", username);
        args.put("role", roleId);
        final boolean returnedValue = jdbcInsertUserRoles.execute(args) == 1;
        if(returnedValue) {
            LOGGER.trace("role added to {}", username);
        }
        else {
            LOGGER.error("couldn't add role to {}", username);

        }
        return returnedValue;

    }

    @Override
    public List<Role> getRoles(final String username) {
        final List<Role> roles = jdbcTemplate.query("SELECT roleName, roleId FROM userRoles, roles " +
                "WHERE userRoles.username = ? and userRoles.role = roles.roleId", ROLE_ROW_MAPPER, username);
        return roles;
    }

    @Override
    public boolean enableUser(final String username, final String code) {
        Optional<PremiumUser> currentUser = findByUserName(username);
        if(currentUser.isPresent() && currentUser.get().getCode().equals(code)) {
            final String sqlQuery = "UPDATE accounts SET enabled = ? WHERE userName = ?";
            return jdbcTemplate.update(sqlQuery, USER_ENABLED, username) == 1;
        }
        else {
            System.out.println("codeReceived = " + code);
            System.out.println("currentCode = " + currentUser.get().getCode());
            System.out.println("equals = " + equals(currentUser.get().getCode().equals(code)) + "\n\n\n\n");

            return false;
        }

    }

}
