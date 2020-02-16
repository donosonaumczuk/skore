import React from 'react';
import PropTypes from 'prop-types';
import HomeMatchPropType from '../../../proptypes/HomeMatchPropType';

//TODO replace with Action Button
const MatchButton = ({ buttonStyle, handleClick, buttonText, fontAwesome,
                        currentMatch }) => {
    return (
        <button className={buttonStyle} onClick={(e) => handleClick(e, currentMatch)} >
            <i className={fontAwesome}></i>
            {buttonText}
        </button>
    );
}

MatchButton.propTypes = {
    buttonStyle: PropTypes.string.isRequired,
    handleClick: PropTypes.func.isRequired,
    buttonText: PropTypes.string.isRequired,
    fontAwesome: PropTypes.string.isRequired,
    currentMatch: HomeMatchPropType.isRequired
}

export default MatchButton;