import React from 'react';
import LocationPropType from '../../../../../proptypes/LocationPropType';
import Utils from '../../../../utils/Utils';

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

const MatchPageLocation = ({ address }) => {
    const addressString = Utils.addressToString(address);
    const googleLink = getGoogleLink(address);
    return (
         <div className="row text-center mb-5 mt-3">
            <div className="col">
                <i className="location-icon mr-2 fas fa-map-marker-alt"></i>
                <a className="address-text skore-link" target="_blank"
                    rel="noopener noreferrer" href={googleLink}>
                    {addressString}
                </a>
            </div>
        </div>
    );
}

MatchPageLocation.propTypes = {
    address: LocationPropType.isRequired
}

export default MatchPageLocation;