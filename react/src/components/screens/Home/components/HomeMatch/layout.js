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

const HomeMatch = ({ currentMatch, creatorImageUrl, sportImageUrl, handleClick }) => {
    const address = currentMatch.location;
    return (<div>
        <div className="row p-2 mt-2 match-card rounded-border" onClick ={() => handleClick()}>
            <div className="col">
                <div className="row mb-4">
                    <CreatorInfo creatorImageUrl={creatorImageUrl} creator={currentMatch.creator}
                                    title={currentMatch.title} />
                    <SportInfo sportImageUrl={sportImageUrl} sport={currentMatch.sportName} />
                    <MatchAvailability currentMatch={currentMatch} />
                </div>
                <MatchCompetitivity isCompetitive={currentMatch.competitive} />
                <MatchDate date={currentMatch.date} time ={currentMatch.time} />
                <MatchDuration durationInMinutes={currentMatch.durationInMinutes} />
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
    handleClick: PropTypes.func.isRequired
}

export default HomeMatch;