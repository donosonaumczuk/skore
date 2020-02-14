import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

const getPlayerLink = player => {
    if (!player) {
        return null;
    }
    if (player.username) {
        return (
            <Link to={`/users/${player.username}`} className="player-username skore-link">
                @{player.username}
            </Link>
        );
    }
    else {
            return <p>{player.email}</p>;
    }
}

const MatchPagePlayer = ({ gamePlayers }) => {
    return (
        gamePlayers.map((currentPlayers, i) => {
            const  { playerTeamOne, playerTeamTwo } = currentPlayers;
            return (
                <tr key={i}>
                    <td scope="row">
                        {getPlayerLink(playerTeamOne)}
                    </td>
                    <td scope="row">
                        {getPlayerLink(playerTeamTwo)}
                    </td>
                </tr>
            );
        })
    );
}

MatchPagePlayer.propTypes = {
    gamePlayers: PropTypes.array.isRequired
}
export default MatchPagePlayer;