import React from 'react';
import Proptypes from 'prop-types';

const SearchInput = ({ containerId, labelText, inputStyle, inputId,
                        inputType, input, placeholderText }) => {
    return (
        <div className="row mb-4" id={containerId}>
            <label>{labelText}</label>
            <input {...input} className={inputStyle} type={inputType} 
                    id={inputId} placeholder={placeholderText} />
        </div>
    );
}

FilterInput.propTypes = {
    containerId: Proptypes.string.isRequired,
    labelText: Proptypes.string.isRequired,
    inputStyle: Proptypes.string.isRequired,
    inputId: Proptypes.string.isRequired,
    inputType: Proptypes.string.isRequired,
    placeholderText: Proptypes.string.isRequired,

}

export default SearchInput;