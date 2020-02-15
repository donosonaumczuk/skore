import React from 'react';
import PropTypes from 'prop-types';
import HomeMatchPropType from '../../../../../proptypes/HomeMatchPropType';
import HomeMatch from './layout';

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
    });
    return {
        creatorImageUrl: creatorImageUrl,
        sportImageUrl: sportImageUrl
    };
}

const HomeMatchContainer = ({ currentMatch, handleMatchClick, joinMatch, cancelMatch,
                                deleteMatch }) => {
    const imageUrls = getImageUrls(currentMatch.links);
    const creatorImageUrl = imageUrls.creatorImageUrl;
    const sportImageUrl = imageUrls.sportImageUrl;
    return (
        <HomeMatch currentMatch={currentMatch} creatorImageUrl={creatorImageUrl}
                    sportImageUrl={sportImageUrl} handleClick={handleMatchClick}
                    joinMatch={joinMatch} cancelMatch={cancelMatch}
                    deleteMatch={deleteMatch} />
    );
}

HomeMatch.propTypes = {
    currentMatch: HomeMatchPropType.isRequired,
    handleMatchClick: PropTypes.func,
    joinMatch: PropTypes.func.isRequired,
    cancelMatch: PropTypes.func.isRequired,
    deleteMatch: PropTypes.func.isRequired
}

export default HomeMatchContainer;