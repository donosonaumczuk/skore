import React, { Fragment } from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import MatchPagePlayer from './components/MatchPagePlayer';

const TEAM_ONE_NUMBER = 1;
const TEAM_TWO_NUMBER = 2;

const getGamePlayersWithSameLength = (teamOnePlayers, teamTwoPlayers) => {
    let maxLength = teamOnePlayers.length > teamTwoPlayers.length ? teamOnePlayers.length :
                                                                    teamTwoPlayers.length;
    let gamePlayers = [];
    for (let i = 0 ; i < maxLength; i++) {
        gamePlayers[i] = {
        playerTeamOne: teamOnePlayers[i] ? teamOnePlayers[i] : "",
        playerTeamTwo: teamTwoPlayers[i] ? teamTwoPlayers[i] : ""
        };
    }
    return gamePlayers;
}

const getTeamAward = (score, teamNumber) => {
    if (score) {
        const teamScores = score.split("-");
        const teamOneScore = parseInt(teamScores[0]);
        const teamTwoScore = parseInt(teamScores[1]);
        if (teamOneScore !== teamTwoScore) {
            const winnerTeam = teamOneScore > teamTwoScore ? TEAM_ONE_NUMBER : TEAM_TWO_NUMBER;
            if (winnerTeam === teamNumber) {
                return (
                    <i className="mr-2 fas fa-winner fa-award" data-toggle="tooltip"
                        data-placement="top" data-html="true" 
                        title={i18next.t('matchPage.winner')}>
                    </i>
                );
            }
        }
    }
    return <Fragment/>;
}

const MatchPagePlayers = ({ teamOnePlayers, teamTwoPlayers, score }) => {
    const gamePlayers = getGamePlayersWithSameLength( teamOnePlayers, teamTwoPlayers );
    const teamOneAward = getTeamAward(score, TEAM_ONE_NUMBER);
    const teamTwoAward = getTeamAward(score, TEAM_TWO_NUMBER);
    return (
        <div className="row text-center">
            <table className="table table-striped">
                <thead>
                <tr>
                    <th className="team-name" scope="col">
                        {i18next.t('matchPage.teamOne')}
                        {teamOneAward}
                    </th>
                    <th className="team-name" scope="col">
                        {teamTwoAward}
                        {i18next.t('matchPage.teamTwo')}
                    </th>
                </tr>
                </thead>
                <tbody>
                    <MatchPagePlayer gamePlayers={gamePlayers} />
                </tbody>
            </table>
        </div>
    )
}

MatchPagePlayers.propTypes = {
    teamOnePlayers: PropTypes.array.isRequired,
    teamTwoPlayers: PropTypes.array.isRequired,
    score: PropTypes.string
}

export default MatchPagePlayers;