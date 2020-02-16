import React from 'react';
import Proptypes from 'prop-types';

const MatchPageSport = ({ sportImageUrl, sport }) => { 
    return (
        <div className="row mt-2">
            <div className="col offset-4">
                <div className="row">
                    <div className="col-1 mr-2 ml-2">
                        <img src={sportImageUrl} className="sport-img mr-2" alt="sport-pic" />
                    </div>
                    <div className="col">
                        <p className="sport-label">{sport}</p>
                    </div>
                </div>
            </div>
        </div>  
    );
}

MatchPageSport.propTypes = {
    sportImageUrl: Proptypes.string.isRequired,
    sport: Proptypes.string.isRequired
}

export default MatchPageSport;