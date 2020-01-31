import React from 'react';
import Proptypes from 'prop-types';

const FilterInput = ({ containerId, labelText, inputStyle, inputId, inputType, input }) => {
    return (
        <div className="row mb-4" id={containerId}>
            <label>{labelText}</label>
            <input {...input} className={inputStyle} type={inputType} id={inputId}/>
        </div>
    );
}

FilterInput.propTypes = {
    containerId: Proptypes.string.isRequired,
    labelText: Proptypes.string.isRequired,
    inputStyle: Proptypes.string.isRequired,
    inputId: Proptypes.string.isRequired,
    inputType: Proptypes.string.isRequired,
}

export default FilterInput;