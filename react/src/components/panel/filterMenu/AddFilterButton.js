import React from 'react';
import Proptypes from 'prop-types';

const AddFilterButton = ({ buttonStyle, handleClick, buttonText }) => {
    return (
        <div className="row mb-4 text-center">
                <div className="col">
                    <button className={buttonStyle} onClick={(e) => handleClick()}>
                        <i className="fas fa-plus mr-1"></i>
                        {buttonText}
                    </button>
                </div>
        </div>
    )
}

AddFilterButton.propTypes = {
    buttonStyle: Proptypes.string.isRequired,
    handleClick: Proptypes.func.isRequired,
    buttonText: Proptypes.string.isRequired
}

export default AddFilterButton;