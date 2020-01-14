import React from 'react';
import Proptypes from 'prop-types';
import CreatorInfo from './CreatorInfo';
import SportInfo from './SportInfo';
import GameResult from './GameResult';

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
            </div>
        </div>
    );
}

UserMatch.propTypes = {
    currentMatch: Proptypes.object.isRequired //TODO implement custom proptype
}

export default UserMatch;