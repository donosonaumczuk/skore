
import React from 'react';
import PropTypes from 'prop-types';

const ActionButton = ({ buttonStyle, handleClick, buttonText, fontAwesome, data }) => {
    return (
        <button className={buttonStyle} onClick={(e) => handleClick(e, data)} >
            <i className={fontAwesome}></i>
            {buttonText}
        </button>
    );
}

ActionButton.propTypes = {
    buttonStyle: PropTypes.string.isRequired,
    handleClick: PropTypes.func.isRequired,
    buttonText: PropTypes.string,
    fontAwesome: PropTypes.string.isRequired,
}

export default ActionButton;