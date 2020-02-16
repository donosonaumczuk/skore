import React from 'react';
import PropTypes from 'prop-types';
import i18next from 'i18next';

const getResult = gameResult => {
    const results = gameResult.split(" - ");
    return {
        teamOne: parseInt(results[0], 10),
        teamTwo: parseInt(results[1], 10)
    };
}

const getUserTeam = (user, team1, team2) => {
    let userTeam = 0;
    team1.forEach(player => {
        if (player.username && player.username === user) {
            userTeam = 1;
        }
    });
    if (userTeam === 0) {
        team2.forEach(player => {
            if (player.username && player.username === user) {
                userTeam = 2;
            }
        });
    }
    return userTeam;
}

const getResultLabel = (userTeam, resultTeamOne, resultTeamTwo) => {
    if (resultTeamOne > resultTeamTwo) {
        if (userTeam === 1) {
            return i18next.t('profile.match.won');
        }
        else {
            return i18next.t('profile.match.lost');
        }
    }
    else if (resultTeamTwo > resultTeamOne) {
        if (userTeam === 2) {
            return i18next.t('profile.match.won');
        }
        else {
            return i18next.t('profile.match.lost');
        }
    }
    else {
        return i18next.t('profile.match.tie');
    }
}

const getResultLabelClass = resultLabel => {
    if (resultLabel === "WON") {
        return "fa-check-circle";
    }
    else if (resultLabel === "TIE") {
        return "fa-minus-circle"
    }
    else {
        return  "fa-times-circle"
    }
}


const GameResult = ({ gameResult, username, teamOne, teamTwo }) => {
    const result = getResult(gameResult);
    const userTeam = getUserTeam(username, teamOne, teamTwo);
    let resultLabel= getResultLabel(userTeam, result.teamOne, result.teamTwo);
    let resultLabelClass = getResultLabelClass(resultLabel)
    
    return (
        <div className="offset-1 col-4 col-sm-3">
                <div className="row text-center">
                    <div className="col">
                        <p className="result-label">{gameResult}</p>
                    </div>
                </div>
                <div className="row text-center">
                    <div className="col mt-xl-2 ml-xl-4">
                        <i className={`name-label fas ${resultLabelClass} mr-2`} ></i>{resultLabel}
                    </div>
                </div>
            </div>
    );
}

GameResult.propTypes = {
    gameResult: PropTypes.string.isRequired,
    username: PropTypes.string.isRequired,
    teamOne: PropTypes.array.isRequired,
    teamTwo: PropTypes.array.isRequired
}

export default GameResult;