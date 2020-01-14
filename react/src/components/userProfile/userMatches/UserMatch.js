import React from 'react';
import Proptypes from 'prop-types';
import CreatorInfo from './CreatorInfo';
import SportInfo from './SportInfo';
import GameResult from './GameResult';
import MatchCompetitivity from './MatchCompetitivity';
import MatchDate from './MatchDate';
import MatchLocation from './MatchLocation';

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

const UserMatch = ({ currentMatch, username }) => {
    const imageUrls = getImageUrls(currentMatch.links);
    const creatorImageUrl = imageUrls.creatorImageUrl;
    const sportImageUrl = imageUrls.sportImageUrl;
    const address = currentMatch.location;
    return (
        <div className="row p-2 mt-2 match-card rounded-border" >
            <div className="col">
                <div className="row mb-4">
                    <CreatorInfo creatorImageUrl={creatorImageUrl} creator={currentMatch.creator}
                                    title={currentMatch.title}/>
                    <SportInfo sportImageUrl={sportImageUrl} sport={currentMatch.sportName}/>
                    <GameResult gameResult={currentMatch.results} username={username}
                         teamOne={currentMatch.team1.players} teamTwo={currentMatch.team2.players}/>
                </div>
                <MatchCompetitivity isCompetitive={currentMatch.competitive} />
                <MatchDate date={currentMatch.date} time ={currentMatch.time} />
                <MatchLocation address={address} />
            </div>
        </div>
    );
}

UserMatch.propTypes = {
    currentMatch: Proptypes.object.isRequired //TODO implement custom proptype
}

export default UserMatch;