import React from 'react';
import PropTypes from 'prop-types';

const MatchButton = ({ buttonStyle, handleClick, buttonText, fontAwesome }) => {
    return (
        <button className={buttonStyle} to="" onClick={() => handleClick()} >
            <i className={fontAwesome}></i>
            {buttonText}
        </button>
    );
}

MatchButton.propTypes = {
    buttonStyle: PropTypes.string.isRequired,
    handleClick: PropTypes.func.isRequired,
    buttonText: PropTypes.string.isRequired,
    fontAwesome: PropTypes.string.isRequired
}

export default MatchButton;