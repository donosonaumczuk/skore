import React from 'react';
import Proptypes from 'prop-types';

const FilterCheckbox = ({ containerId, labelText, inputStyle, inputId, input }) => {
    const checked = input.value === "true" || input.value === true;
    return (
        <div className="form-check form-check-inline" id={containerId}>
            <input {...input} className={inputStyle} type="checkbox"
                    id={inputId} checked={checked} />
            <label className="form-check-label" htmlFor={inputId}>{labelText}</label>
         </div>
    );
}

FilterCheckbox.propTypes = {
    containerId: Proptypes.string.isRequired,
    labelText: Proptypes.string.isRequired,
    inputStyle: Proptypes.string.isRequired,
    inputId: Proptypes.string.isRequired,
}

export default FilterCheckbox;