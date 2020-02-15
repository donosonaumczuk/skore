import React from 'react';
import PropTypes from 'prop-types';

const MatchTitle = ({ matchTitle }) => {
    return (
        <div className="row text-center">
            <div className="col">
                <h1 className="match-name">{matchTitle}</h1>
            </div>
        </div>
    );
}

MatchTitle.propTypes = {
    matchTitle: PropTypes.string.isRequired
}

export default MatchTitle;