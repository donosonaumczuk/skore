package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.NotificationDao;
import ar.edu.itba.paw.models.*;
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
public class NotificationJdbcDao implements NotificationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Notification> ROW_MAPPER = (resultSet, rowNum) ->
            new Notification(resultSet.getTimestamp("startTime").toLocalDateTime(),
                        resultSet.getString("content"),
                        resultSet.getBoolean("seen"));

    @Autowired
    public NotificationJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("notification");
    }

    public Optional<Notification> create(final String startTime, final String content,
                                         final String userName) {
        final Map<String, Object> args =  new HashMap<>();

        args.put("startTime",   startTime);
        args.put("content",     content);
        args.put("seen",        0);
        args.put("userName",    userName);

        jdbcInsert.execute(args);
        return findByKey(startTime, content);
    }

    public Optional<Notification> findByKey(final String startTime, final String content) {
        final String getANotification = "SELECT * FROM notification WHERE startTime = ? AND " +
                "content = ?;";
        final List<Notification> list = jdbcTemplate.query(getANotification, ROW_MAPPER,
                startTime, content);
        return list.stream().findFirst();
    }

    public List<Notification> getNotificationsByUserName(final String userName) {
        final String getANotifications = "SELECT * FROM notification WHERE userName = ?;";
        final List<Notification> list = jdbcTemplate.query(getANotifications, ROW_MAPPER,
                userName);
        return list;
    }
}
