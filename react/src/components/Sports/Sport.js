import React from 'react';
import Proptypes from 'prop-types';
import SportInfo from '../match/SportInfo';
import SportPlayers from './SportPalyers';

const getSportImageUrl = links => {
    let sportImageUrl;
    links.forEach(link => {
        if (link.rel === "image") {
            sportImageUrl = link.href;
        }
    });
    return sportImageUrl;
}
const Sport = ({ sport }) => {
    //TODO maybe make image bigger and name bigger, add more staff to sport
    const sportImageUrl = getSportImageUrl(sport.links);
    return (
        <div className="row p-2 mt-2 match-card rounded-border">
            <div className="col">
                <div className="row mb-4">
                    <SportInfo sportImageUrl={sportImageUrl} sport={sport.displayName} />
                    <SportPlayers playersQuantity={sport.playerQuantity} />
                </div>
            </div>
        </div>
    );
}

Sport.propTypes = {
    sport: Proptypes.object.isRequired//TODO add custom sport proptype
}

export default Sport;