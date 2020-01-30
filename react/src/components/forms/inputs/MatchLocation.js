import React from 'react';
import PropTypes from 'prop-types';
import LocationInput from './LocationInput';

const MatchLocation = ({ updateLocationAndState, errorMessage, meta }) => {
    return (
        <React.Fragment>
            <LocationInput updateLocation={updateLocationAndState} />
            <span className="invalid-feedback d-block">
                {meta.submitFailed && errorMessage}
            </span>
        </React.Fragment>
    );
}

MatchLocation.propTypes = {
    updateLocationAndState: PropTypes.func.isRequired,
    errorMessage: PropTypes.string.isRequired,
    meta: PropTypes.object.isRequired
}

export default MatchLocation