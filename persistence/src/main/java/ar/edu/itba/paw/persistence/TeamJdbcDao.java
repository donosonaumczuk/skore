package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.Exceptions.TeamFullException;
import ar.edu.itba.paw.Exceptions.TeamNotFoundException;
import ar.edu.itba.paw.Exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.TeamDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class TeamJdbcDao implements TeamDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertTeam;
    private final SimpleJdbcInsert jdbcInsertIsPartOf;

    private final static ResultSetExtractor<List<Team>> MAPPER = (resultSet) -> {
        List<Team> teams = new ArrayList<>();
        Team currentTeam = null;
        String currentTeamName;
        while(resultSet.next()) {
            currentTeamName = resultSet.getString("teamName");
            if (currentTeam == null) {
                currentTeam = mapATeam(resultSet);
            }
            else if (!currentTeam.getName().equals(currentTeamName)) {
                teams.add(currentTeam);
                currentTeam = mapATeam(resultSet);
            }
            currentTeam.addPlayer(mapAPlayer(resultSet));
        }
        if (currentTeam != null) {
            teams.add(currentTeam);
        }
        return teams;
    };

    @Autowired
    public TeamJdbcDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertTeam = new SimpleJdbcInsert(jdbcTemplate).withTableName("teams");
        jdbcInsertIsPartOf = new SimpleJdbcInsert(jdbcTemplate).withTableName("isPartOf");
    }

    @Override
    public Optional<Team> findByTeamName(final String teamName) {
        String query =
                "SELECT leader.firstName as leaderFirstName, leader.lastName as leaderLastName, " +
                    "leader.email as leaderEmail, leader.userId as leaderUserId, leader.userName " +
                    "as leaderUserName, acronym, teamName, isTemp, team.sportName as sportName, " +
                    "team.playerQuantity as playerQuantity, " +
                    "users.firstName as usersFirstName, users.lastName as usersLastName, " +
                    "users.email as usersEmail, users.userId as usersUserId " +
                "FROM (accounts NATURAL JOIN users) AS leader, (teams NATURAL JOIN sports) AS team, " +
                    "(isPartOf NATURAL JOIN users) AS users " +
                "WHERE team.leaderName = leader.userName AND users.teamName = team.teamName " +
                    "AND team.teamName = ? " +
                "ORDER BY teamName;";
        final List<Team> list = jdbcTemplate.query(query, MAPPER, teamName);
        Optional<Team> team = list.stream().findFirst();
        return team;
    }

    @Override
    public Optional<Team> create(final String leaderName, final long leaderId,
                                 final String acronym, final String teamName,
                                 final boolean isTemp, final String sportName) {

        final Map<String, Object> argsTeam =  new HashMap<>();
        final Map<String, Object> argsIsPartOF =  new HashMap<>();

        argsTeam.put("leaderName", leaderName);
        argsTeam.put("acronym", acronym);
        argsTeam.put("teamName", teamName);
        argsTeam.put("isTemp", isTemp? 1 : 0);
        argsTeam.put("sportName", sportName);

        argsIsPartOF.put("teamName", teamName);
        argsIsPartOF.put("userId", leaderId);

        jdbcInsertTeam.execute(argsTeam);
        jdbcInsertIsPartOf.execute(argsIsPartOF);
        return findByTeamName(teamName);
    }

    @Override
    public boolean remove(final String teamName) {
        final String sqlQuery = "DELETE FROM teams where teamName = ?;";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, teamName);
        return rowsDeleted > 0;
    }

    @Override
    public Optional<Team> updateTeamInfo(final String newTeamName, final String newAcronym,
                                         final String newLeaderName, final String newSportName,
                                         final String oldTeamName) {
        int rowsModifiedTeam;
        final String sqlQueryTeam = "UPDATE teams SET teamName = ?, acronym = ?, leaderName = ?, " +
                "sportName = ? WHERE teamName = ?";
        rowsModifiedTeam = jdbcTemplate.update(sqlQueryTeam, newTeamName, newAcronym, newLeaderName,
                newSportName, oldTeamName);
        if(rowsModifiedTeam != 0) {
            return findByTeamName(newTeamName);
        }

        return  Optional.empty();
    }

    public Optional<Team> addPlayer(final String teamName, final long userId) {
        Optional<Team> team = findByTeamName(teamName);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("There is not a team with the name " + teamName);
        }
        if(team.get().getPlayers().size() > team.get().getSport().getQuantity()) {
            throw new TeamFullException("The team " + teamName + "is full");
        }

        final Map<String, Object> argsIsPartOF =  new HashMap<>();

        argsIsPartOF.put("teamName", teamName);
        argsIsPartOF.put("userId", userId);

        jdbcInsertIsPartOf.execute(argsIsPartOF);

        return findByTeamName(teamName);
    }

    public Optional<Team> removePlayer(final String teamName, final long userId) {
        Optional<Team> team = findByTeamName(teamName);
        if(!team.isPresent()) {
            throw new TeamNotFoundException("There is not a team with the name: " + teamName);
        }

        final String sqlQuery = "DELETE FROM isPartOf where teamName = ? AND userId = ?;";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, teamName, userId);

        if(rowsDeleted == 0) {
            throw new UserNotFoundException("There is not a user with th id: " + userId);
        }

        return findByTeamName(teamName);
    }

    private static Team mapATeam(ResultSet resultSet) throws SQLException, DataAccessException {
        return new Team(new PremiumUser(resultSet.getString("leaderFirstName"),
                            resultSet.getString("leaderLastName"),
                            resultSet.getString("leaderEmail"),
                            resultSet.getInt("leaderUserId"),
                            resultSet.getString("leaderUserName")),
                        resultSet.getString("acronym"),
                        resultSet.getString("teamName"),
                        resultSet.getInt("isTemp") == 1,
                        new Sport(resultSet.getString("sportName"),
                            resultSet.getInt("playerQuantity")));
    }

    private static User mapAPlayer(ResultSet resultSet) throws SQLException, DataAccessException {
        return new User(resultSet.getString("usersFirstName"),
                resultSet.getString("usersLastName"),
                resultSet.getString("usersEmail"),
                resultSet.getInt("usersUserId"));
    }
}

