import React from 'react';
import LocationPropType from '../../../proptypes/LocationPropType';

const MatchLocation = ({ address }) => {
    //TODO make toString of address
    return (
         <div className="row">
            <div className="col">
                <p>
                    <span className="location-icon mr-2 fas fa-map-marker-alt"></span>
                    {address.street}
                </p>
            </div>
        </div>
    );
}

MatchLocation.propTypes = {
    address: LocationPropType.isRequired
}

export default MatchLocation;