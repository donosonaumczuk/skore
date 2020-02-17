import React from 'react';
import LocationPropType from '../../../proptypes/LocationPropType';
import Utils from '../../utils/Utils';

const getGoogleLink = address => {
    const { country, state, city, street } = address;
    let baseUrl = `https://www.google.com/maps/search/?api=1&query=${street}`;
    if (city && city.length > 0) {
        baseUrl =`${baseUrl}+${city}`;
    }
    if (state && state.length > 0) {
        baseUrl =`${baseUrl}+${state}`;
    }
    if (country && country.length > 0) {
        baseUrl =`${baseUrl}+${country}`;
    }
    return baseUrl;
}

const MatchLocation = ({ address }) => {
    const addressString = Utils.addressToString(address);
    const googleLink = getGoogleLink(address);
    return (
         <div className="row">
            <div className="col pb-2">
                <i className="location-icon mr-2 fas fa-map-marker-alt"></i>
                <a className="address-text skore-link" target="_blank"
                    rel="noopener noreferrer" href={googleLink} 
                    onClick={(e) => e.stopPropagation()}>
                    {addressString}
                </a>
            </div>
        </div>
    );
}

MatchLocation.propTypes = {
    address: LocationPropType.isRequired
}

export default MatchLocation;