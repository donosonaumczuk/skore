import React from 'react';
import PropTypes from 'prop-types';
import CreatorInfo from '../../../match/CreatorInfo';
import SportInfo from '../../../match/SportInfo';
import GameResult from '../../../match/GameResult';
import MatchCompetitivity from '../../../match/MatchCompetitivity';
import MatchDate from '../../../match/MatchDate';
import MatchLocation from '../../../match/MatchLocation';
import UserMatchWithResultPropType from '../../../../proptypes/UserMatchWithResultPropType';

const getImageUrls = links => {
    let creatorImageUrl;
    let sportImageUrl;
    links.map(link => {
        if (link.rel === "creatorImage") {
            creatorImageUrl = link.href;
        }
        if (link.rel === "sportImage") {
            sportImageUrl = link.href;
        }
        return link;
    })
    return {
        creatorImageUrl: creatorImageUrl,
        sportImageUrl: sportImageUrl
    };
}

const goToMatch = (e, match, history) => {
    e.stopPropagation();
    history.push(`/match/${match.key}`);
}

const UserMatchWithResult = ({ currentMatch, username, history }) => {
    const imageUrls = getImageUrls(currentMatch.links);
    const creatorImageUrl = imageUrls.creatorImageUrl;
    const sportImageUrl = imageUrls.sportImageUrl;
    const address = currentMatch.location;
    console.log(currentMatch);
    return (
        <div className="row p-2 mt-2 match-card rounded-border" 
                        onClick={(e) => goToMatch(e, currentMatch, history)} >
            <div className="col">
                <div className="row mb-4">
                    <CreatorInfo creatorImageUrl={creatorImageUrl} creator={currentMatch.creator}
                                    title={currentMatch.title} />
                    <SportInfo sportImageUrl={sportImageUrl} sport={currentMatch.sportName} />
                    <GameResult gameResult={currentMatch.results}
                                isInTeamOne={currentMatch.inTeam1}
                                isInTeamTwo={currentMatch.inTeam2} />
                </div>
                <MatchCompetitivity isCompetitive={currentMatch.competitive} />
                <MatchDate date={currentMatch.date} time ={currentMatch.time} />
                <MatchLocation address={address} />
            </div>
        </div>
    );
}

UserMatchWithResult.propTypes = {
    currentMatch: UserMatchWithResultPropType.isRequired,
    username: PropTypes.string.isRequired
}

export default UserMatchWithResult;