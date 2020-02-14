import React from 'react';
import LocationPropType from '../../../proptypes/LocationPropType';
import Utils from '../../utils/Utils';



const MatchLocation = ({ address }) => {
    const addressString = Utils.addressToString(address);
    return (
         <div className="row">
            <div className="col">
                <p>
                    <span className="location-icon mr-2 fas fa-map-marker-alt"></span>
                    {addressString}
                </p>
            </div>
        </div>
    );
}

MatchLocation.propTypes = {
    address: LocationPropType.isRequired
}

export default MatchLocation;