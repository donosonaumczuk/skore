import React from 'react';
import HomeMatchPropType from '../../../../proptypes/HomeMatchPropType';
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
    })
    return {
        creatorImageUrl: creatorImageUrl,
        sportImageUrl: sportImageUrl
    };
}

const handleClick = () => {
    //TODO handle click on match redirect to its page when created
}

const HomeMatchContainer = ({ currentMatch }) => {
    const imageUrls = getImageUrls(currentMatch.links);
    const creatorImageUrl = imageUrls.creatorImageUrl;
    const sportImageUrl = imageUrls.sportImageUrl;

    return (
        <HomeMatch currentMatch={currentMatch} creatorImageUrl={creatorImageUrl}
                    sportImageUrl={sportImageUrl} handleClick={handleClick} />
    );
}

HomeMatch.propTypes = {
    currentMatch: HomeMatchPropType.isRequired
}

export default HomeMatchContainer;