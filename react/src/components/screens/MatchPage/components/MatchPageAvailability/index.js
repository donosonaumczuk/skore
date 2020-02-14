import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import AuthService from '../../../../../services/AuthService';
import MatchButton from '../../../../match/MatchButton';
import i18next from 'i18next';

const isUserInTeam = (currentUser, team) => {
    if (!currentUser || !team) {
        return false;
    }
    let userFound = false;
    team.players.forEach(player => {
        if (player.username === currentUser) {
            userFound = true;
        }
    });
    return userFound;
}

const isInMatch = (currentUser, teamOne, teamTwo) => {
    if (!currentUser) {
        return false;
    }
    let userFound = isUserInTeam(currentUser, teamOne);
    if (!userFound) {
        userFound = isUserInTeam(currentUser, teamTwo);
    }
    return userFound;
}

const matchHasStarted = currentMatch => {
    const date = currentMatch.date;
    const time = currentMatch.time;
    const startTime = new Date(date.year, date.monthNumber -1, date.dayOfMonth, time.hour, time.minute);
    const currentTime = new Date();
    return startTime <= currentTime;
}

const getButton = (currentMatch, currentUser, joinMatch, cancelMatch, deleteMatch) => {
    if (matchHasStarted(currentMatch)) {
        return <Fragment></Fragment>;
    }
    if (currentUser && currentUser === currentMatch.creator) {
        return <MatchButton buttonStyle="btn btn-negative join-button" 
                            handleClick={deleteMatch} currentMatch={currentMatch}
                            buttonText={i18next.t('home.deleteMatch')}
                            fontAwesome="fas fa-trash-alt mr-1" />
    }
    else if (currentUser && isInMatch(currentUser, currentMatch.team1, currentMatch.team2)) {
        return <MatchButton buttonStyle="btn btn-negative join-button"
                            handleClick={cancelMatch} currentMatch={currentMatch}
                            buttonText={i18next.t('home.cancelMatch')}
                            fontAwesome="fas fa-times mr-1" />
    }
    else if(currentMatch.totalPlayers > currentMatch.currentPlayers) { 
        let buttonText;        
        if (currentMatch.competitive) {
            buttonText = i18next.t('home.joinCompetitiveMatch');
        }
        else {
            buttonText = i18next.t('home.joinMatch');
        }
        return <MatchButton buttonStyle="btn btn-green join-button"
                            handleClick={joinMatch} currentMatch={currentMatch} 
                            buttonText={buttonText} fontAwesome="fas fa-plus mr-1" />
    }
    return <Fragment></Fragment>
}

const MatchPageAvailability = ({ currentMatch, joinMatch, cancelMatch, deleteMatch }) => {
    const { currentPlayers, totalPlayers} = currentMatch;
    let button = getButton(currentMatch, AuthService.getCurrentUser(), 
                            joinMatch, cancelMatch, deleteMatch);
    return (
        <div className="row text-center mb-3">
            <div className="col offset-4 playing-label">
                <i className="name-label fas fa-users mr-2"></i>
                {`${currentPlayers} / ${totalPlayers}`} 
            </div>
            <div className="col">
                {button}
                {/* TODO update with corresponding button */}
            </div>
        </div>
    );
}

MatchPageAvailability.propTypes = {
    currentMatch: PropTypes.object.isRequired,
    joinMatch: PropTypes.func.isRequired,
    cancelMatch: PropTypes.func.isRequired,
    deleteMatch: PropTypes.func.isRequired
}

export default MatchPageAvailability;
