import React from 'react';
import Proptypes from 'prop-types';

const SportInfo = ({ sportImageUrl, sport }) => {
    console.log(sport);
    return (
       <div className="col-2 col-sm-3">
                <div className="container-fluid pt-2">
                    <div className="row">
                        <div className="col col-xl-4 mr-0 mt-1">
                            <img src={sportImageUrl} className="sport-img" alt="sport-pic" />
                        </div>
                        <div className="col-6 col-xl d-none d-sm-block pl-0">
                            <p className="sport-label">{sport}</p>
                        </div>
                    </div>
                </div>
            </div>  
    );
}

SportInfo.propTypes = {
    sportImageUrl: Proptypes.string.isRequired,
    sport: Proptypes.string.isRequired
}

export default SportInfo;