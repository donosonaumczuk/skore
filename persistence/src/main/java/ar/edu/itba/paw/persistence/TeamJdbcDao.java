package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TeamDao;
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
public class TeamJdbcDao implements TeamDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Team> ROW_MAPPER = (resultSet, rowNum) ->
            new Team(new PremiumUser(resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("email"),
                        resultSet.getInt("userId"),
                        resultSet.getString("userName")),
                    resultSet.getString("acronym"),
                    resultSet.getString("teamName"),
                    resultSet.getInt("isTemp") == 1,
                    new Sport(resultSet.getString("sportName"),
                            resultSet.getInt("playerQuantity")));

    @Autowired
    public TeamJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("teams");
    }

    public Optional<Team> findByTeamName(final String teamName) {
        final List<Team> list = jdbcTemplate.query("SELECT * FROM accounts natural join users " +
                "natural join teams natural join sports  WHERE teamName = ?", ROW_MAPPER, teamName);
        Optional<Team> team = list.stream().findFirst();
        if(team.isPresent()) {
            //loadPlayers(team);
        }
        return team;
    }

    public Optional<Team> create(final String leaderName, final String acronym,
                                 final String teamName,final boolean isTemp,
                                 final String sportName) {

        final Map<String, Object> args =  new HashMap<>();

        args.put("leaderName", leaderName);
        args.put("acronym", acronym);
        args.put("teamName", teamName);
        args.put("isTemp", isTemp? 1 : 0);
        args.put("sportName", sportName);


//        CREATE TABLE IF NOT EXISTS teams(
//                teamName    VARCHAR(100) PRIMARY KEY,
//        acronym     VARCHAR(100),
//                leaderName  VARCHAR(100) REFERENCES accounts(userName) NOT NULL,
//                isTemp      INTEGER NOT NULL,
//                sportName   VARCHAR (100) NOT NULL,
//        FOREIGN KEY (sportName) REFERENCES sports(sportName)
//                --Filters--
//);
//
        jdbcInsert.execute(args);
        return findByTeamName(teamName);
    }

    public boolean remove(final String teamName) {
        return false;
    }

    public Optional<Team> addPlayer(final String teamName, final long userId) {
        return null;
    }

    public Optional<Team> removePlayer(final String teamName, final long userId) {
        return null;
    }

    public Optional<Team> updateTeamInfo(final String newTeamName, final String newAcronym,
                                         final String newLeaderName, final String newSportName) {
        return null;
    }
}

