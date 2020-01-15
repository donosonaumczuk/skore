import React from 'react';
import Proptypes from 'prop-types';

const joinMatch = () => {
    // TODO implement when we have endpoint
}

const cancelMatch = () => {
    // TODO implement when we have endpoint
}

const deleteMatch = () => {
    // TODO implement when we have endpoint
}

const MatchAvailability = ({ currentMatch }) => {
    const currentPlayers = 2;
    const totalPlayers = 2;
    const isCompetitive = currentMatch.competitive;
    // let button = getButton(isCompetitive)
    return (
        <div className="offset-1 col-4 col-sm-3">
            <div className="row text-center">
                    <div className="col">
                            <i className="name-label fas fa-users mr-2"></i>
                            { `${currentPlayers} / ${totalPlayers}`} 
                    </div>
            </div>
            <div className="row text-center">
                    <div className="col mt-xl-2 ml-xl-4">
                            getButton(match) 
                    </div>
            </div>
        </div>
    );
}

MatchAvailability.propTypes = {
    currentMatch: Proptypes.object.isRequired
}

export default MatchAvailability;
