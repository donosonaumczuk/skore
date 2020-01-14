import React from 'react';
import Proptypes from 'prop-types';

const MatchLocation = ({ address }) => {
    console.log(address)
  
    return (
         <div className="row">
            <div className="col">
                <p>
                    <span className="location-icon mr-2 fas fa-map-marker-alt"></span>
                    {address}
                </p>
            </div>
        </div>
    );
}

MatchLocation.propTypes = {
    address: Proptypes.string.isRequired
}

export default MatchLocation;