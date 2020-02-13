import React from 'react';
import PropTypes from 'prop-types';

const SubLocationInput = ({ divStyle, label, id, value })  => {
    return (
        <div className={divStyle}>
            <label htmlFor={id}>{label}</label>
            <input type="text" className="form-control" id={id} 
                    value={value} readOnly={true} />
        </div>
    );
}

SubLocationInput.propTypes = {
    divStyle: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
}
export default SubLocationInput;