import React from 'react';
import { Link } from 'react-router-dom';
import Proptypes from 'prop-types';

const PosterWithButton = ({ posterStyle, message, buttonText, buttonUrl }) => {
    return (
        <div className={`col text-center p-4 ${posterStyle}`}>
            <p>{message}</p>
            <Link id="create-match-btn" className="btn btn-white-succ" to={buttonUrl} role="button">
                {buttonText}
            </Link>
        </div>
    );
}

PosterWithButton.propTypes = {
    posterStyle: Proptypes.string.isRequired,
    message: Proptypes.string.isRequired,
    buttonText: Proptypes.string.isRequired,
    buttonUrl: Proptypes.string.isRequired,
}

export default PosterWithButton;