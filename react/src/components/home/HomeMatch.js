import React from 'react';
import CreatorInfo from './../match/CreatorInfo'
import SportInfo from './../match/SportInfo';
import MatchCompetitivity from './../match/MatchCompetitivity';
import MatchDate from './../match/MatchDate';
import MatchLocation from './../match/MatchLocation';
import MatchDuration from '../match/MatchDuration';
import MatchAvailability from '../match/MatchAvailability';
import HomeMatchPropType from '../../proptypes/HomeMatchProptype';

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

const handleClick = () => {
    //TODO handle click on match redirect to its page when created
}

const HomeMatch = ({ currentMatch }) => {

    const imageUrls = getImageUrls(currentMatch.links);
    const creatorImageUrl = imageUrls.creatorImageUrl;
    const sportImageUrl = imageUrls.sportImageUrl;
    const address = currentMatch.location;

    return (
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
    );
}

HomeMatch.propTypes = {
    currentMatch: HomeMatchPropType.isRequired
}

export default HomeMatch;