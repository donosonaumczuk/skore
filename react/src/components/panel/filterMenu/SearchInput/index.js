import React from 'react';
import Proptypes from 'prop-types';
import SearchButton from '../SearchButton';

const SearchInput = ({ containerId, labelText, inputStyle, inputId,
                        inputType, input, placeholderText, submitting }) => {
    return (
        <div className="row my-auto" id={containerId}>
            <div className="offset-4 col-3 my-auto">
                <input {...input} className="form-control filter-input"
                        type={inputType} id={inputId} placeholder={placeholderText} />             
            </div>
            <div className="col-1 my-auto">
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