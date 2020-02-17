import React, { Fragment } from 'react';
import i18next from 'i18next';
import HomeMatchPropType from '../../../../../proptypes/HomeMatchPropType';
import AuthService from '../../../../../services/AuthService';
import StatusLabel from './components/StatusLabel';

const getFormattedResult = gameResult => {
    if (gameResult) {
        const results = gameResult.split("-");
        const teamOneScore = parseInt(results[0], 10);
        const teamTwoScore = parseInt(results[1], 10);
        return `${teamOneScore} - ${teamTwoScore}`;
    }
    else {
        return null;
    }
}

const getTag = (currentMatch, currentUser, updateMatchScore) => {
    const { currentPlayers, totalPlayers, hasStarted, hasFinished } = currentMatch
    let formattedResult = getFormattedResult(currentMatch.results);
    if (currentPlayers === totalPlayers) {
        if (hasStarted && !hasFinished) {
            return <StatusLabel text={i18next.t('matchPage.matchIsPlaying')} />
        }
        else if(hasFinished) {
            if (!currentMatch.results) {
                if (currentUser === currentMatch.creator) {
                    return (
                        <div className="row text-center mb-3">
                            <div className="col">
                                <button className="btn btn-green"
                                        onClick={(e) => updateMatchScore(e, currentMatch)}>
                                    <i className="fas fa-plus mr-1"></i>
                                    {i18next.t('setMatchScoreForm.setScoreButton')}
                                </button>
                            </div>
                        </div>
                    );
                }
                else {
                    return <StatusLabel text={i18next.t('matchPage.waitingForScore')} />
                }
            }
            else {
                return (
                    <div className="row text-center">
						<div className="col">
							<h1 className="match-result">{formattedResult}</h1>
						</div>
					</div>
                );
            }
        }
    }
    else {
        if (hasStarted) {
            return <StatusLabel text={i18next.t('matchPage.matchIsCanceled')} />
        }
    }
    return <Fragment></Fragment>;
}

const MatchPageStatus = ({ currentMatch, updateMatchScore }) => {
    const currentUser = AuthService.getCurrentUser();
    const tag = getTag(currentMatch, currentUser, updateMatchScore);
    return (
        <Fragment>
            {tag}
        </Fragment>
    );
}

MatchPageStatus.propTypes = {
    currentMatch: HomeMatchPropType.isRequired
}

export default MatchPageStatus;
