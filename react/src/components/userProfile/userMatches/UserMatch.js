import React from 'react';
import Proptypes from 'prop-types';
import CreatorInfo from './CreatorInfo';

const getCreatorImageUrl = links => {
    let creatorImageUrl;
    links.map(link => {
        if (link.rel === "creatorImage") {
            creatorImageUrl = link.href;
        }
        return link;
    })
    return creatorImageUrl;
}

const UserMatch = ({ currentMatch }) => {
    const creatorImageUrl = getCreatorImageUrl(currentMatch.links);
    return (
        <div className="row p-2 mt-2 match-card rounded-border" >
            <div className="col">
                <div className="row mb-4">
                    <CreatorInfo creatorImageUrl={creatorImageUrl} creator={currentMatch.creator}
                                    title={currentMatch.title}/>
                    {/* <SportInfo />
                    <Result /> */}
                </div>
            </div>
        </div>
    );
}

UserMatch.propTypes = {
    currentMatch: Proptypes.object.isRequired //TODO implement custom proptype
}

export default UserMatch;