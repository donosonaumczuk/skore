import React from 'react';
import Proptypes from 'prop-types';
import SearchButton from '../SearchButton';

const SearchInput = ({ containerId, labelText, inputStyle, inputId,
                        inputType, input, placeholderText, submitting }) => {
    return (
        <div className="row mb-4 center" id={containerId}>
            <div className ="col-2">
                    <label>{labelText}</label>
            </div>
            <div className="col-3">
                <input {...input} className="form-control filter-input mb-2" 
                        type={inputType} id={inputId} placeholder={placeholderText} />             
            </div>
            <div className="col-1">
                <SearchButton submitting={submitting} />
            </div>
        </div>
    );
}

SearchInput.propTypes = {
    containerId: Proptypes.string.isRequired,
    labelText: Proptypes.string.isRequired,
    inputStyle: Proptypes.string.isRequired,
    inputId: Proptypes.string.isRequired,
    inputType: Proptypes.string.isRequired,
    placeholderText: Proptypes.string.isRequired,

}

export default SearchInput;