import React from 'react';
import PropTypes from 'prop-types';
import SportInfo from '../../../../match/SportInfo';
import SportPlayers from '../SportPlayers';
import SportPropTypes from '../../../../../proptypes/SportPropType';

const getSportImageUrl = links => {
    let sportImageUrl;
    links.forEach(link => {
        if (link.rel === "image") {
            sportImageUrl = link.href;
        }
    });
    return sportImageUrl;
}

const Sport = ({ sport, isLogged, isLiked, likeSport, dislikeSport }) => {
    const sportImageUrl = getSportImageUrl(sport.links);
    return (
        <div className="row p-2 mt-2 ml-5 mr-5 match-card rounded-border">
            <div className="col">
                <div className="row mb-4">
                    <SportInfo sportImageUrl={sportImageUrl} sport={sport.displayName} />
                    <SportPlayers playersQuantity={sport.playerQuantity} />
                    {/* <SportLike isLogged={isLogged} isLiked={isLiked} 
                                likeSport={likeSport} dislikeSport={dislikeSport} /> */}
                </div>
            </div>
        </div>
    );
}

Sport.propTypes = {
    sport: SportPropTypes.isRequired,
    isLogged: PropTypes.string,
    isLiked: PropTypes.bool,
    likeSport: PropTypes.func.isRequired,
    dislikeSport: PropTypes.func.isRequired
}

export default Sport;