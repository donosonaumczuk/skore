import React from 'react';
import PropTypes from 'prop-types';
import i18next from 'i18next';

const getResult = gameResult => {
    const results = gameResult.split("-");
    return {
        teamOne: parseInt(results[0], 10),
        teamTwo: parseInt(results[1], 10)
    };
}

const getUserTeam = (isInTeamOne, isInTeamTwo) => {
    let userTeam = 0;
    if (isInTeamOne) {
        userTeam = 1;
    }
    else if (isInTeamTwo) {
        userTeam =2;
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
    const WON_LABEL = i18next.t('profile.match.won');
    const TIE_LABEL = i18next.t('profile.match.tie');
    if (resultLabel === WON_LABEL) {
        return "fa-check-circle";
    }
    else if (resultLabel === TIE_LABEL) {
        return "fa-minus-circle"
    }
    else {
        return  "fa-times-circle"
    }
}

const getFormattedResult = gameResult => {
    return `${gameResult.teamOne} - ${gameResult.teamTwo}`;
}

const GameResult = ({ gameResult, username, isInteamOne, isInteamTwo }) => {
    const result = getResult(gameResult);
    const userTeam = getUserTeam(username, isInteamOne, isInteamTwo);
    let resultLabel= getResultLabel(userTeam, result.teamOne, result.teamTwo);
    let resultLabelClass = getResultLabelClass(resultLabel);
    let formattedResult = getFormattedResult(result);

    return (
        <div className="offset-1 col-4 col-sm-3 ml-0 mr-0">
                <div className="row text-center">
                    <div className="col">
                        <p className="result-label">{formattedResult}</p>
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
    isInTeamOne: PropTypes.bool.isRequired,
    isInTeamTwo: PropTypes.bool.isRequired
}

export default GameResult;