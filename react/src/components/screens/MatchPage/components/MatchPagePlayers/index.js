import React from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import MatchPagePlayer from './components/MatchPagePlayer';

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

const MatchPagePlayers = ({ teamOnePlayers, teamTwoPlayers }) => {
    const gameplayers = getGamePlayersWithSameLength( teamOnePlayers, teamTwoPlayers );
    return (
        <div className="row text-center">
            <table className="table table-striped">
                <thead>
                <tr>
                    <th className="team-name" scope="col">
                        {i18next.t('matchPage.teamOne')}
                    </th>
                    <th className="team-name" scope="col">
                        {i18next.t('matchPage.teamTwo')}
                    </th>
                </tr>
                </thead>
                <tbody>
                    <MatchPagePlayer gamePlayers={gameplayers} />
                </tbody>
            </table>
        </div>
    )
}

MatchPagePlayers.propTypes = {
    teamOnePlayers: PropTypes.array.isRequired,
    teamTwoPlayers: PropTypes.array.isRequired,

}
export default MatchPagePlayers;