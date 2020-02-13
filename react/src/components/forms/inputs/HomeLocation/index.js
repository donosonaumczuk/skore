import React from 'react';
import PropTypes from 'prop-types';
import LocationInput from '../LocationInput';

const updateLocation = (home, changeFieldsValue, touchField) => {
    const street = home.street ? home.street : "";
    const number = home.number ? home.number : "";
    const newHome = { ...home, "street": `${street} ${number}` };
    changeFieldsValue('home', newHome);
    touchField('home');
}

const HomeLocation = ({ input, meta, changeFieldsValue, touchField }) => {
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

HomeLocation.propTypes = {
    changeFieldsValue: PropTypes.func.isRequired,
    touchField: PropTypes.func.isRequired,
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
}

export default HomeLocation;