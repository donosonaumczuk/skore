import React from 'react';
import PropTypes from 'prop-types';
import HomeMatchPropType from '../../../../../proptypes/HomeMatchPropType';
import CreatorInfo from '../../../../match/CreatorInfo';
import SportInfo from '../../../../match/SportInfo';
import MatchCompetitivity from '../../../../match/MatchCompetitivity';
import MatchDate from '../../../../match/MatchDate';
import MatchLocation from '../../../../match/MatchLocation';
import MatchDuration from '../../../../match/MatchDuration';
import MatchAvailability from '../../../../match/MatchAvailability';
import GameResult from '../../../../match/GameResult';

const getAvailabilityOrResult = (currentMatch, joinMatch, cancelMatch, deleteMatch) => {
    if (currentMatch.results) {
        return (
            <GameResult gameResult={currentMatch.results}
                        username={currentMatch.creator}
                        isInTeamOne={currentMatch.inTeam1}
                        isInTeamTwo={currentMatch.inTeam2} />
        );
    }
    else {
        return (
            <MatchAvailability currentMatch={currentMatch} joinMatch={joinMatch}
                                cancelMatch={cancelMatch} deleteMatch={deleteMatch} />
        );
    }
}

const HomeMatch = ({ currentMatch, creatorImageUrl, sportImageUrl, handleClick,
                        joinMatch, cancelMatch, deleteMatch }) => {
    const address = currentMatch.location;
    const availabilityOrResult = getAvailabilityOrResult(currentMatch, joinMatch, cancelMatch, deleteMatch);
    return (<div>
        <div className="row p-2 mt-2 match-card rounded-border" onClick ={() => handleClick(currentMatch.key)}>
            <div className="col">
                <div className="row mb-4">
                    <CreatorInfo creatorImageUrl={creatorImageUrl} creator={currentMatch.creator}
                                    title={currentMatch.title} />
                    <SportInfo sportImageUrl={sportImageUrl} sport={currentMatch.sportName} />
                    {availabilityOrResult}
                </div>
                <MatchCompetitivity isCompetitive={currentMatch.competitive} />
                <MatchDate date={currentMatch.date} time ={currentMatch.time} />
                <MatchDuration durationInMinutes={currentMatch.minutesOfDuration} />
                <MatchLocation address={address} />
            </div>
        </div>
        </div>
    );
}

HomeMatch.propTypes = {
    currentMatch: HomeMatchPropType.isRequired,
    creatorImageUrl: PropTypes.string.isRequired,
    sportImageUrl: PropTypes.string.isRequired,
    handleClick: PropTypes.func.isRequired,
    joinMatch: PropTypes.func.isRequired,
    cancelMatch: PropTypes.func.isRequired,
    deleteMatch: PropTypes.func.isRequired
}

export default HomeMatch;