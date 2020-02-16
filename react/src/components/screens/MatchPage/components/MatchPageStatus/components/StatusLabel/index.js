import React from 'react';
import PropTypes from 'prop-types';

const StatusLabel = ({ text }) => {
    return (
        <div className="row text-center mb-3">
            <div className="col playing-label">
                {text}
            </div>
        </div>
    );
}

StatusLabel.propTypes = {
    text: PropTypes.string.isRequired
}

export default StatusLabel;