import React from 'react';
import PropTypes from 'prop-types';
import LocationInput from './LocationInput';

const MatchLocation = ({ updateLocationAndState, meta }) => {
    return (
        <React.Fragment>
            <LocationInput updateLocation={updateLocationAndState} />
                {meta.submitFailed && meta.error &&
                    <span className="invalid-feedback d-block">
                        {meta.error}
                    </span>}
        </React.Fragment>
    );
}

MatchLocation.propTypes = {
    updateLocationAndState: PropTypes.func.isRequired,
    meta: PropTypes.object.isRequired
}

export default MatchLocation