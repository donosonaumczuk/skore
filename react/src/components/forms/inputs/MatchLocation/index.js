import React from 'react';
import PropTypes from 'prop-types';
import LocationInput from '../LocationInput';
import CreateMatchValidator from '../../validators/CreateMatchValidator';

const MatchLocation = ({ updateLocationAndState, input, meta, errorMessage, location }) => {
    if( !errorMessage) {
        errorMessage = meta.submitFailed ? CreateMatchValidator.validateLocation(location) : null;
    }
    return (
        <React.Fragment>
            <LocationInput updateLocation={updateLocationAndState} inputInfo={input} />
                {errorMessage &&
                    <span className="invalid-feedback d-block">
                        {errorMessage}
                    </span>}
        </React.Fragment>
    );
}

MatchLocation.propTypes = {
    updateLocationAndState: PropTypes.func.isRequired,
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
    errorMessage: PropTypes.string,
    location: PropTypes.object.isRequired
}

export default MatchLocation