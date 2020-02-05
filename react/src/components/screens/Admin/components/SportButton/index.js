import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

const SportButton = ({ buttonStyle, buttonUrl, iStyle, buttonLabel }) => {
    return (
        <div className = "col">
            <div className="row mb-4 mt-4">
                <div className="col-2 col-sm-3">
                    <Link className={buttonStyle} to={buttonUrl} role="button">
                        <i className={iStyle}></i>
                        {buttonLabel}
                    </Link>
                </div>
            </div>
        </div>
    );
}

SportButton.propTypes = {
    buttonStyle: PropTypes.string.isRequired,
    buttonUrl: PropTypes.string.isRequired,
    iStyle: PropTypes.string.isRequired,
    buttonLabel: PropTypes.string.isRequired,
}

export default SportButton;