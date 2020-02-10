import React from 'react';
import PropTypes from 'prop-types';
import LocationInput from '../LocationInput';

const updateLocation = (home, changeFieldsValue, touchField) => {
    changeFieldsValue('matchLocation', home);
    touchField('matchLocation');
}

const MatchLocation = ({ input, meta, changeFieldsValue, touchField }) => {
    return (
        <React.Fragment>
            <LocationInput updateLocation={updateLocation} inputInfo={input}
                            changeFieldsValue={changeFieldsValue}
                            touchField={touchField} />
                {meta.touched && meta.error &&
                    <span className="invalid-feedback d-block">
                        {meta.error}
                    </span>}
        </React.Fragment>
    );
}

MatchLocation.propTypes = {
    changeFieldsValue: PropTypes.func.isRequired,
    touchField: PropTypes.func.isRequired,
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
}

export default MatchLocation