import React from 'react';
import Proptypes from 'prop-types';

const getResult = gameResult => {
    const results = gameResult.split("-");
    return {
        teamOne: parseInt(results[0], 10),
        teamTwo: parseInt(results[1], 10)
    };
}

const getUserTeam = (user, team1, team2) => {
    let userTeam = 0
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
            return "WON";
        }
        else {
            return "LOST"
        }
    }
    else if (resultTeamTwo > resultTeamOne) {
        if (userTeam === 2) {
            return "WON";
        }
        else {
            return "LOST"
        }
    }
    else {
        return "TIE";
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
    console.log("result");
    console.table(result);
    const userTeam = getUserTeam(username, teamOne, teamTwo);
    console.log("user team:");
    console.table(userTeam);
    let resultLabel= getResultLabel(userTeam, result.teamOne, result.teamTwo);
    console.log("result label");
    console.table(resultLabel);
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
                        {/* <i className="name-label fas fa-check-circle mr-2"></i><spring:message code="winLabel"/>
                        <i className="name-label fas fa-minus-circle mr-2"></i><spring:message code="tieLabel"/>
                        <i className="name-label fas fa-times-circle mr-2"></i><spring:message code="loseLabel"/> */}       
                    </div>
                </div>
            </div>
    );
}

GameResult.propTypes = {
    gameResult: Proptypes.string.isRequired,
    username: Proptypes.string.isRequired,
    teamOne: Proptypes.array.isRequired,
    teamTwo: Proptypes.array.isRequired
}

export default GameResult;